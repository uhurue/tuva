import java.util.ArrayList;


public class ConcurrentModification
{
    public static void main(String[] args)
    {
        ConcurrentModification me = new ConcurrentModification();
        me.test1();
    }

    public void test1()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        int i;
        for(i=0; i<10; i++)
        {
            list.add(new Integer(1));
            list.add(new Integer(2));
            list.add(new Integer(3));
        }
        int size = list.size();
        System.out.println(String.format("list=%s, size=%d", list.toString(), size));

        try
        {
            for(i=0; i<size; i++)
            {
                System.out.println(String.format("i=%d, value=%d", i, list.get(i)));
                if(list.get(i).equals(2))
                {
                    System.out.println("remove try");
                    list.remove(i);
                    System.out.println("remove done");
                }
            }
        }
        catch (Exception e)
        {
            
        }
    }
}
