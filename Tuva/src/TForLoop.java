import java.util.BitSet;

/*
java - for loop에서 가능한 object 객체 참조하지말것

아래 테스트에서 bitset은 참조 메커니즘이 다르므로 단순 loop 속도가 나오는 것 같음. bitset 사용 권장됨.
테스트시 맨 처음 돌리는 loop에서 실행 시간이 늘어나므로, 모든 테스트를 단번에 수행하고 동시에 하지 않았음.
1번: 3.4초
2번: 6.3초
3번: 3.5초
*/

public class TForLoop
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        TForLoop me = new TForLoop();
        
        System.out.println("Integer.MAX_VALUE = " + Integer.MAX_VALUE);
        Integer testNumber = 1420000000;
        Long result;
        
        if(args[0].equals("1"))
        {
          result = me.testForLoop1(testNumber);
          System.out.println("laptime simple number = " + result);
        }
        else if(args[0].equals("2"))
        {
            result = me.testForLoop2(testNumber);
            System.out.println("laptime Object Array = " + result);
        }
        else
        {
            result = me.testForLoop3(testNumber);
            System.out.println("laptime bit set = " + result);
        }
    }

    public Long testForLoop1(Integer loopCount)
    {
        Integer i=0;
        Long start = System.currentTimeMillis();
        for(i=0; i<loopCount; i++)
        {
            //
        }
        Long stop = System.currentTimeMillis();
        return stop-start;
    }
    
    public Long testForLoop2(Integer loopCount)
    {
        Integer i=0;
        char [] temp = new char[loopCount];
        Long start = System.currentTimeMillis();
        for(i=0; i<temp.length; i++)
        {
            //
        }
        Long stop = System.currentTimeMillis();
        return stop-start;
    }
    public Long testForLoop3(Integer loopCount)
    {
        BitSet bs = new BitSet(loopCount);
        Integer i=0;
        Long start = System.currentTimeMillis();
        for(i=0; i<bs.size(); i++)
        {
            //
        }
        Long stop = System.currentTimeMillis();
        return stop-start;
    }
}
