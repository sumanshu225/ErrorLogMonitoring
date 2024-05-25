package mypackage;

import java.util.*;

public class LogTypeManager {
    
    private final List<LogEntry> lst = new ArrayList<>();
    private final TreeMap<Long,Integer> map = new TreeMap<>();

    private final List<StatNode> prefixStats = new ArrayList<>();
    private final List<StatNode> suffixStats = new ArrayList<>();

    // Worst case - O(N)
    public void add(long timeStamp,String logType,double severity){

        lst.add(new LogEntry(timeStamp, logType, severity));
        map.put(timeStamp, lst.size()-1);

        // For prefix stats O(1)
        addPrefixStats(timeStamp,logType,severity);

        // For suffix stats O(N)
        addSuffixStats(timeStamp,logType,severity);
    }

    // O(1)
    public ResultNode get(){
        StatNode lastNode = prefixStats.get(prefixStats.size()-1);

        return new ResultNode(
            lastNode.min,
            lastNode.max,
            lastNode.sum/prefixStats.size()
        );  
    }

    // O(1)
    public ResultNode getBefore(long timeStamp){

        Map.Entry<Long, Integer> entry = map.floorEntry(timeStamp);

        if(entry==null){
            return new ResultNode(0, 0, 0);
        }

        int ind = entry.getValue();
        StatNode currentNode = prefixStats.get(ind);

        return new ResultNode(
            currentNode.min,
            currentNode.max,
            currentNode.sum/(ind+1)
        );  
    }

    // O(1)    
    public ResultNode getAfter(long timeStamp){

        Map.Entry<Long, Integer> entry = map.ceilingEntry(timeStamp);

        if(entry==null){
            return new ResultNode(0, 0, 0);
        }
        
        int ind = entry.getValue();
        int size = suffixStats.size();
        StatNode currentNode = suffixStats.get(ind);
        double lastNodeSum = suffixStats.get(size-1).sum;
        double sum = (lastNodeSum - currentNode.sum + lst.get(ind).Severity)/(size-ind);

        return new ResultNode(
            currentNode.min,
            currentNode.max,
            sum
        );  
    }

    private void addPrefixStats(long timeStamp,String logType,double severity){
        if(prefixStats.isEmpty()){
            prefixStats.add(new StatNode(severity, severity, severity));
        }else{
            StatNode prev = prefixStats.get(prefixStats.size()-1);
            prefixStats.add(new StatNode(
                Math.min(prev.min,severity),
                Math.max(prev.max,severity),
                prev.sum+severity
            ));
        }
    }

    private void addSuffixStats(long timeStamp, String logType, double severity) {
        if(suffixStats.isEmpty()){
            suffixStats.add(new StatNode(severity, severity, severity));
        }else{
            StatNode prev = suffixStats.get(suffixStats.size()-1);
            suffixStats.add(new StatNode(
                severity,
                severity,
                prev.sum+severity
            ));
            adjustMinSuffixStats();
            adjustMaxSuffixStats();
        }
    }

    private void adjustMinSuffixStats(){
        
        int lastInd = suffixStats.size()-1;
        double currentMin = suffixStats.get(lastInd).min;

        lastInd--;
        while(lastInd>=0){
            StatNode prev = suffixStats.get(lastInd);
            if(prev.min<currentMin)
                break;
            prev.min = currentMin;
            lastInd--;
        }
    }

    private void adjustMaxSuffixStats(){
        
        int lastInd = suffixStats.size()-1;
        double currentMax = suffixStats.get(lastInd).max;

        lastInd--;
        while(lastInd>=0){
            StatNode prev = suffixStats.get(lastInd);
            if(prev.max>currentMax)
                break;
            prev.max = currentMax;
            lastInd--;
        }
    }
}