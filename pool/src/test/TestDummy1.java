package test;

import org.junit.Test;

public class TestDummy1 {

	String message;
	
	@Test
	public void testMessage()
	{
		message = this.getClass().getName() + " testMessage() called()";
		System.out.println(message);
	}

}
