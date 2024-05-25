package mypackage;

public class LogEntry {
    public final long TimeStamp;

    public final String LogType;

    public final double Severity;

    public  LogEntry(long TimeStamp,String LogType,double Severity){
        this.TimeStamp = TimeStamp;
        this.LogType = LogType;
        this.Severity = Severity;
    }
}