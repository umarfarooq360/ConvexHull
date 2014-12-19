
/**
 * Write a description of class LehmerGen here.
 * 
 * @author (Omar Farooq) 
 * @version (a version number or a date)
 */
public class LehmerGen
{
    final static double m= 2147483647;
    final static double a= 48271;
    final static double q= m/a;
    final static double r = m%a;

    double  lastValue;
    
    public LehmerGen(double seed){
        lastValue = seed;
    }
    
    public double getNext (double prev){
        double next = (a*prev)%(m);
        if(next > 0 )
            lastValue = next;
        else
            lastValue = next+m;
        return lastValue;    
    }
    
    public double next(){
        return getNext(lastValue);
    }
    
}