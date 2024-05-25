package mypackage;

public class ResultNode extends BaseNode{
    
    public double mean;

    public ResultNode(double min,double max,double mean){
        super(min, max);
        this.mean = mean;
    }
}
