package yktest;

public class TestException
{

    public static void main(String[] args)
    {
        TestException me = new TestException();
        try
        {
            int seed = (int)(Math.random()*10);
            System.out.println("test seed = " + seed);
            me.badFunctionA(seed);
        }
        catch(Exception e)
        {
            System.out.println("exception in main(): " + e.getMessage());
        }
    }

    public void badFunctionA(int arg) throws Exception
    {
        try
        {
            if(arg>5) //난수로 나온 숫자가 5초과면 1번 exception, 이하면 2번 exception  
            {
                throw new Exception("exception in badFunctionA()"); //exception point 1
            }
            badSubFunctionA();                                      //exception point 2
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            System.out.println("run finally block"); //1,2 어느 포인트에서 exception이어도 여기는 실행됨
        }
    }
    
    public void badSubFunctionA() throws Exception
    {
        throw new Exception("exception in badSubFunctionA()");
    }
}
