/**
 * 샘플:http://tomee.apache.org/examples-trunk/simple-rest/README.html 
 */

package dweb.rest;

import static org.junit.Assert.assertEquals;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import org.apache.openejb.jee.SingletonBean;
import org.apache.openejb.junit.ApplicationComposer;

//import org.apache.openejb.junit.EnableServices; //deprecated 됐다. teseting을 쓴다.
import org.apache.openejb.testing.EnableServices;
//import org.apache.openejb.junit.Module; //deprecated
import org.apache.openejb.testing.Module;


@EnableServices(value = "jaxrs") 
@RunWith(ApplicationComposer.class) //테스트할 클래스

public class DummyTest {
	@Module
	public SingletonBean app()
	{
		return (SingletonBean) new SingletonBean (DummyREST.class).localBean();
	}
	
	@Test
	public void get() throws IOException
	{
		final String message = WebClient.create("http://localhost:4204").path("/DummyTest/dummy/").get(String.class);
		assertEquals("Dummy Get", message);
	}
	
	@Test
	public void post() throws IOException
	{
		final String message = WebClient.create("http://localhost:4204").path("DummyTest/dummy/").post("DUMMY POST", String.class);
		assertEquals("dummy post", message);
	}
}
