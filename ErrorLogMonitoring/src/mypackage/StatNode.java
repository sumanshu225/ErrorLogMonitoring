package mypackage;

public class StatNode extends BaseNode {
    
    public double sum;

    public StatNode(double min,double max,double sum){
        super(min, max);
        this.sum = sum;
    }
}