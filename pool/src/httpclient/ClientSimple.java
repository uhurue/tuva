package httpclient;

import java.util.concurrent.*;

import org.apache.http.*;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.*;
import org.apache.http.conn.*;
import org.apache.http.conn.routing.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.*;
import org.apache.http.util.*;

public class ClientSimple
{
	public final static void main(String[] args) throws Exception
	{
		String uri = args[0];
		int reqNum = Integer.parseInt(args[1]);
		int reqTerm = Integer.parseInt(args[2]);
		
		try
		{
			//new ClientSimple().request1(uri, reqNum, reqTerm);
			new ClientSimple().request2(uri, reqNum, reqTerm);
		}
		catch (Exception e)
		{
			System.out.println("ERROR:" + e.getMessage());
		}
	}
	
	public void request1(String uri, int reqNum, int reqTerm) throws Exception
	{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		if(reqNum==0)
		{
			System.out.println(String.format("request to %s infinite times with term '%d' ms", uri, reqTerm));
		}
		else
		{
			System.out.println(String.format("request to %s '%d' times with term '%d' ms", uri, reqNum, reqTerm));
		}
		
		int i =0, tick=0;
		HttpGet httpGet = new HttpGet(uri);
		CloseableHttpResponse response;
		HttpEntity entity;

		while(true)
		{
			usleep(reqTerm);
			tick = (int)(Math.random()*10)%2;
			if(tick==0)
			{
				continue;
			}
			System.out.println("request " + httpGet.getURI());
			response = httpclient.execute(httpGet);
			System.out.println("--> response status  = " + response.getStatusLine());
			// response handler
			try
			{
				entity = response.getEntity();
				EntityUtils.consume(entity);
			}
			catch(Exception e)
			{
				System.out.println("  --> http fail:" + e.getMessage());
			}
			finally
			{
				//Thread.sleep(5000); //테스트에만 썼다. close 지연시키려고.
				response.close();
			}

			System.out.println("----------------------------------------");
			if(reqNum!=0 && reqNum<++i)
			{
				break;
			}
		}
	}
	
	/*
	 * 
	 */
	public void request2(String uri, int reqNum, int reqTerm) throws Exception
	{
		HttpClientContext context = HttpClientContext.create();
		HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
		HttpRoute route = new HttpRoute(new HttpHost(uri, 80));
		// Request new connection. This can be a long process
		if(reqNum==0)
		{
			System.out.println(String.format("request to %s infinite times with term '%d' ms", uri, reqTerm));
		}
		else
		{
			System.out.println(String.format("request to %s '%d' times with term '%d' ms", uri, reqNum, reqTerm));
		}
		int i =0, tick=0;
		
		while(true)
		{
			usleep(reqTerm);
			tick = (int)(Math.random()*10)%2;
			if(tick==0)
			{
				continue;
			}
			System.out.println("111111111");
			ConnectionRequest connRequest = connManager.requestConnection(route, null);
			// Wait for connection up to 10 sec
			HttpClientConnection conn = connRequest.get(10, TimeUnit.SECONDS);
			System.out.println("222222222");
			try
			{
				if (!conn.isOpen()) // If not open
				{
					System.out.println("333333333");
					// establish connection based on its route info
					connManager.connect(conn, route, 1000, context);
					// and mark it as route complete
					connManager.routeComplete(conn, route, context);
				}
				// Do useful things with the connection.
				System.out.println("request " + uri);
				System.out.println("--> response status  = " + conn.receiveResponseHeader().getStatusLine());
			}
			finally
			{
				connManager.releaseConnection(conn, null, 1, TimeUnit.MINUTES);
			}

			System.out.println("----------------------------------------");
			if(reqNum!=0 && reqNum<++i)
			{
				break;
			}
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