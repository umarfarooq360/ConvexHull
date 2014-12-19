
/**
 * Write a description of class TimeTester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TimeTester
{
    static LehmerGen gen = new LehmerGen(12345);

    public static void main(String[] args) {
        long tStart = System.currentTimeMillis();
        double tmp = gen.next();
        while( Math.abs(tmp - 12345 ) > 0.5) {
            tmp = gen.next();
            //System.out.println(tmp);
        }
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        
        System.out.print("time : " + elapsedSeconds);
    }
}
