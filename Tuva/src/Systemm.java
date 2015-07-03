import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Systemm
{
    public static void main(String[] args)
    {
        System.out.println("System.nanotime() / 1e9 = " + System.nanoTime() / 1e9 );
        System.out.println("(System.nanoTime() / 1e9) / 60 / 60= " + (System.nanoTime() / 1e9) / 60  );
        try
        {
            System.out.println(new Scanner(new FileInputStream("/proc/uptime")).next());
        }
        catch(FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
