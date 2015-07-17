/**
 * ป๙วร:http://tomee.apache.org/examples-trunk/simple-rest/README.html 
 */
package dweb.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/dummy_rest")
public class DummyREST {
	@GET
	public String message()
	{
		return "Get, path = /dymmy_rest";
	}

	@POST
	public String lowerCase(final String message)
	{
		return message.toLowerCase();
	}
}