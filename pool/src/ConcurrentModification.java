import java.util.ArrayList;
import java.util.Iterator;

//Iterating through a list, avoiding ConcurrentModificationException when removing in loop
//  http://stackoverflow.com/questions/223918/iterating-through-a-list-avoiding-concurrentmodificationexception-when-removing
//Removing elements on a List while iterating through it: 
//  http://codereview.stackexchange.com/questions/64011/removing-elements-on-a-list-while-iterating-through-it
//JAVA document의 관련 부분
//  http://docs.oracle.com/javase/tutorial/collections/interfaces/collection.html

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
        
        ArrayList<Integer> list2 = (ArrayList<Integer>)list.clone();
        ArrayList<Integer> list3 = (ArrayList<Integer>)list.clone();

        try
        {
            for(i=0; i<size; i++) //java.lang.IndexOutOfBoundsException: Index: 20, Size: 20
            {
                System.out.println(String.format("i=%d, value=%d", i, list.get(i)));
                if(list.get(i).equals(2))
                {
                    list.remove(i);
                }
            }
            System.out.println("result = " + list);
        }
        catch (Exception e)
        {
            System.out.println("result = " + list);
            System.out.println("error: " + e);
        }

        try
        {
            for(Integer num: list2) //이 구문에서는 java.util.ConcurrentModificationException
            {
                System.out.println(String.format("num=%d", num));
                if(num.equals(2))
                {
                    list2.remove(num);
                }
            }
            System.out.println("result = " + list2);
        }
        catch (Exception e)
        {
            System.out.println("error: " + e);
        }
        try
        {
            Integer number;
            for(Iterator<Integer> it = list3.iterator(); it.hasNext();)
            {
                number = it.next();
                if(number.equals(2))
                {
                    it.remove();
                }
            }
            System.out.println("result = " + list3);
        }
        catch (Exception e)
        {
            System.out.println("error: " + e);
        }
    }
}
