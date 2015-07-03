import java.util.concurrent.CopyOnWriteArrayList;


public class TArrayList
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        TArrayList me = new TArrayList();
        me.testArrayList();
    }

    public void testArrayList()
    {
        CopyOnWriteArrayList<String> nums = new CopyOnWriteArrayList<String>();
        nums.add("0");  nums.add("1");
        nums.add("2");  nums.add("3");
        nums.add("4");  nums.add("5");
        nums.add("6");  nums.add("7");
        int i,j;
        
        for(j=0;j<nums.size(); j++)
        {
            System.out.println(String.format("nums[%s] = %s", j, nums.get(j)));
        }
        System.out.println("-------------------");
        i=0;
        for(String ii:nums)
        {
            if(i==2||i==3||i==6)
            {
                nums.remove(ii);
            }
            i++;
        }
        for(j=0;j<nums.size(); j++)
        {
            System.out.println(String.format("nums[%s] = %s", j, nums.get(j)));
        }
    }
}
