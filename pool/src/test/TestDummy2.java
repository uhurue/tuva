package test;

import org.junit.Test;

public class TestDummy2 {

	String message;
	
	@Test
	public void testMessage()
	{
		message = this.getClass().getName() + " testMessage() called()";
		System.out.println(message);
	}
	
	@Test(timeout = 2000)
	public void testTimeoutNormal() throws InterruptedException
	{
		message = this.getClass().getName() + " testTimeoutNormal() called()";
		Thread.sleep(1000L);
		System.out.println(message);
	}

	@Test(timeout = 2000)
	public void testTimeoutOver() throws InterruptedException
	{
		message = this.getClass().getName() + " testTimeoutOver() called()";
		Thread.sleep(3000L);
		System.out.println(message);
	}
	
	@Test
	public void testException() //case null pointer exception 
	{
		message = returnNull();
		System.out.println("message length = " + message.length());
	}

	private String returnNull()
	{
		return null;
	}
}
