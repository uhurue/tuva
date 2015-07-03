import java.util.BitSet;

/*

*/

public class TCast1
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        TCast1 me = new TCast1();
        me.test1();
    }

    public void test1()
    {
        float ff = 1000.1f;
        float ffres = 0.0f;
        long ll = 1000L;
        long ll2 = 1000L;
        int ii = 100;
        
        ffres = (ff - ll);
        System.out.println("1-1: " + ffres);
        ffres = (ff - (float)ll);
        System.out.println("1-2: " + ffres);
        
        ffres = (ff - ll)*ii;
        System.out.println("2-1: " + ffres);
        ffres = (ff - (float)ll)*ii;
        System.out.println("2-2: " + ffres);
        
        ffres = (ff - ll)*ii/ll2;
        System.out.println("3-1: " + ffres);
        ffres = (ff - (float)ll)*ii/ll2;
        System.out.println("3-2: " + ffres);
    }

}
