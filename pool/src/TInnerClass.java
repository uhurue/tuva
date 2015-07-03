
public class TInnerClass
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        TInnerClass me = new TInnerClass();
        me.testLocalClass();
        me.testAnonClass();

    }
    public void testLocalClass()
    {
        foo();
        foo2();
    }
    void foo()
    {
        int a = 300;
        final int b = 400;
        
        /**
         지역 클래스, 외부 클래스의 메소드나 초기화 블럭 안에서 선언되며, 지역변수 규칙을 따른다.
         지역변수처럼 메소드 안에서 선언되며, 외부 클래스의 인스턴스 변수, static변수를 모두 쓸 수 있다. 
         지역 클래스가 포함된 메소드(상위메소드)의 지역변수는 final이 붙은 것만 쓸수 있다. 상위메소드가 실행 종료된 후에도 지역클래스의 인스턴스가 소멸된 지역변수를 참조하는 경우가 있기 때문이라고 한다...? 
         */
        class LocalFoo
        {
            public void goo()
            {
                System.out.println("[LocalFoo] method goo() called");
//              System.out.println("a = " + a); //살리면오류:Cannot refer to a non-final variable a inside an inner class defined in a different method
                System.out.println("[LocalFoo] b = " + b);
            }
        }
        System.out.println("method foo() called");
        LocalFoo foo = new LocalFoo();
        foo.goo();
    }
    
    void foo2()
    {
        //int a = 300;
        final int b = 400;

        class LocalFoo
        {
            public void goo()
            {
                System.out.println("[LocalFoo] method goo() called");
//              System.out.println("a = " + a); //살리면오류:Cannot refer to a non-final variable a inside an inner class defined in a different method
                System.out.println("[LocalFoo] b = " + b);
            }
        }
        System.out.println("method foo2() called");
        LocalFoo foo2 = new LocalFoo();
        foo2.goo();
    }
    
    class Anonymous
    {
        public void abc()
        {
            System.out.println("Anonymous.abc() called");
        }
    }
    public void testAnonClass() //익명 클래스 테스트
    {
        joo();
    }
    public void joo()
    {
        Anonymous an = new Anonymous()
        {
            public void abc() 
            {
                System.out.println("Anonymous.abc() overriding called");
            }
        };
        an.abc(); //overriding 된 method가 호출된다.
        System.out.println("method joo() called");
    }
}