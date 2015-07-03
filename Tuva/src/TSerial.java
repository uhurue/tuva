
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class TSerial
{
    /**
     * @param args
     */
    static class InnerA implements Serializable
    {
        private static final Long serialVersionUID = 1L;
        String name;
        Integer index;
        
        public InnerA()
        {
        }
        public InnerA(String name, Integer index)
        {
            this.name = name;
            this.index = index;
        }
        @Override
        public String toString()
        {
            return "InnerA [name=" + name + ", index=" + index + "]";
        }
    }
    public static void main(String[] args)
    {
        TSerial me = new TSerial();
        me.testInnerSerializable();
    }
    
    public void testInnerSerializable()
    {
        ArrayList<InnerA> inList = new ArrayList<InnerA>();
        InnerA in = new InnerA("name1", 5001);
        inList.add(in);
        in = new InnerA("name2", 5002);
        inList.add(in);
        inList.add(new InnerA("name3", 5003));
        inList.add(new InnerA("name4", 5004));
        InnerA out = null;
        String filename = "C:\\Users\\oblab\\Desktop\\object.dat";

        writeObject(inList, filename);

        Object obj = readObject(filename);
        ArrayList<Object> objList = new ArrayList<Object>();
        
        if(obj instanceof ArrayList<?>)
        {
            objList = (ArrayList<Object>)obj;
            for(Object iter:objList)
            {
                if(iter instanceof InnerA)
                {
                    out = (InnerA)iter;
                    System.out.println("InnerA object = " + out);
                }
            }
        }
        else if(obj instanceof InnerA)
        {
            out = (InnerA)obj;
            System.out.println("result1=" + out);
        }
    }
    public void writeObject(Object object, String filename)
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(oos!=null)
                {
                    oos.close();
                }
                if(fos!=null)
                {
                    fos.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public Object readObject(String filename)
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object object = null;
        try
        {
            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();
        }
        catch(ClassNotFoundException e) //readObject()
        {
            e.printStackTrace();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(ois!=null)
                {
                    ois.close();
                }
                if(fis!=null)
                {
                    fis.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return object;
    }
}
