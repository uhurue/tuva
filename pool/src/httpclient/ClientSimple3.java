package httpclient;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

class ClientCore implements Runnable
{
	ClientCore(Integer var)
	{
		this.var = var;
	}
	
	Integer var = 1;
	
	public void run()
	{
		int i = 0;
		while(i++<10)
		{
			System.out.println(String.format("thread var = %d / count = %d", var, i));
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
public class ClientSimple3
{
	public final static void main(String[] args) throws Exception
	{
		ClientCore c1 = new ClientCore(1);
		ClientCore c2 = new ClientCore(2);
		ClientCore c3 = new ClientCore(3);
		Thread t1 = new Thread(c1);
		Thread t2 = new Thread(c2);
		Thread t3 = new Thread(c3);
		t1.start();
		t2.start();
		t3.start();
	}
//	public final static void main(String[] args) throws Exception
//	{
//		String uri = args[0];
//		int reqNum = Integer.parseInt(args[1]);
//		int reqTerm = Integer.parseInt(args[2]);
//		
//		try
//		{
//			new ClientSimple3().request(uri, reqNum, reqTerm);
//		}
//		catch (Exception e)
//		{
//			System.out.println("ERROR:" + e.getMessage());
//		}
//	}
	
	public void request(String uri, int reqNum, int reqTerm) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		System.out.println(String.format("request to %s '%d' times with term '%d' ms", uri, reqNum, reqTerm));
		
		try
		{
			int i =0, tick=0;
			HttpGet httpget;
			httpget = new HttpGet(uri);
			//httpget = new HttpGet("http://www.google.co.kr/");
			
			//for(i=0; i<reqNum; i++)
			while(true)
			{
				usleep(reqTerm);
				tick = (int)(Math.random()*10)%2;
				if(tick==0)
				{
					continue;
				}
				System.out.println("request " + httpget.getURI());
				// response handler
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httpget, responseHandler);
				//System.out.println("----------------------------------------");
				System.out.println(responseBody);
				System.out.println("----------------------------------------");

				if(reqNum!=0 && reqNum<++i)
				{
					break;
				}
			}
		}
		finally
		{
			httpclient.getConnectionManager().shutdown();
		}
	}

	public static void usleep(int msec)
	{
		try
		{
			Thread.sleep(msec);
		}
		catch(InterruptedException e)
		{
		}
	}
}