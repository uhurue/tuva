package yktest;

//�ڹ��� call by reference ����
//���۷����� ����ǹǷ� ���� ��ü�� ������ �ٲ��.
//resetAge() ȣ������ age ������ ���ο����� 22�̴�.

public class TestPassParam
{

    public static void main(String[] args)
    {
        DDD one = new DDD("ykkim", 33);
        TestPassParam me = new TestPassParam();

        System.out.println("before call: one = " + one);
        me.resetAge(one);
        System.out.println("after  call: one = " + one);
    }
    private DDD resetAge(DDD someone)
    {
        someone.age = 22;
        System.out.println("during call: one = " + someone);
        return someone;
    }
}

class DDD
{
    String name;
    int age;
    DDD(String name, int age)
    {
        this.name = name;
        this.age = age;
    }
    @Override
    public String toString()
    {
        return "DDD [name=" + name + ", age=" + age + "]";
    }
    
}