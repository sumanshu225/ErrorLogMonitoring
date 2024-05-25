import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import mypackage.*;

public class App {

    private static final Map<String,LogTypeManager> map = new HashMap<>();
    private static final String ALL= "ALL";
    private static final String INPUT_FILE_NAME  = "C:\\project\\ErrorLogMonitoring\\src\\input.txt";  
    private static final String OUTPUT_FILE_NAME  = "C:\\project\\ErrorLogMonitoring\\src\\output.txt";  

    public static void main(String[] args) throws Exception {
        clearLogFile();
        map.put(ALL, new LogTypeManager());

        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.isBlank())
                    continue;
                handleCommand(line);
            }
            System.out.println("Sucessful");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleCommand(String cmd) throws IOException{
        
        String[] parts = cmd.split(" ",2);
        ResultNode result = null;
        String[] cmdString = new String[0];
        String errorLogType = null;

        int cmdCode = Integer.parseInt(parts[0]);
        switch (cmdCode) {
            case 1 -> {
                String[] entryParts = parts[1].split(";");
                long timestamp = Long.parseLong(entryParts[0].trim());
                String logType = entryParts[1].trim();
                double severity = Double.parseDouble(entryParts[2].trim());

                if(!map.containsKey(logType))
                    map.put(logType, new LogTypeManager());

                map.get(logType).add(timestamp, logType, severity);
                map.get(ALL).add(timestamp, logType, severity);
                logToFile("No output");
            }
            case 2 -> {
                errorLogType = parts[1].trim();
                result = map.get(errorLogType).get();
                logToFile(String.format("Min: %.6f, Max: %.6f, Mean: %.6f", result.min, result.max, result.mean));
            }
            case 3 -> {
                cmdString = parts[1].split(" ");
                if(cmdString[0].startsWith("BEFORE")){
                    result = map.get(ALL).getBefore(Long.parseLong(cmdString[1].trim())); 
                }else{
                    result = map.get(ALL).getAfter(Long.parseLong(cmdString[1].trim())); 
                }
                logToFile(String.format("Min: %.6f, Max: %.6f, Mean: %.6f", result.min, result.max, result.mean));
            }
            case 4 ->{
                cmdString = parts[1].split(" ");
                errorLogType = cmdString[1].trim();
                if(cmdString[0].startsWith("BEFORE")){
                    result = map.get(errorLogType).getBefore(Long.parseLong(cmdString[2].trim())); 
                }else{
                    result = map.get(errorLogType).getAfter(Long.parseLong(cmdString[2].trim())); 
                }
                logToFile(String.format("Min: %.6f, Max: %.6f, Mean: %.6f", result.min, result.max, result.mean));
            }
            default -> throw new AssertionError();
        }
    }

    private static void logToFile(String message) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME,true))) {
            writer.write(message);
            writer.newLine();
        }
    }

    private static void clearLogFile() throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
