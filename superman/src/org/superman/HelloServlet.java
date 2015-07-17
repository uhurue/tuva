package org.superman;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet {
    
    @javax.ejb.EJB
    private HelloBean bean;
    
    @javax.inject.Inject
    private HelloPojo pojo;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");

	    final java.io.Writer writer = response.getWriter();
	    writer.append("<html>");
	    writer.append("<body>");
	    writer.append("<H1>Hello Servlet</H1>");
	    writer.append("<H1>Hello "+ bean.from() +" ^^</H1>");
	    writer.append("<H1>Hello "+ pojo.from() +" ^^</H1>");
	    writer.append("</body>");
	    writer.append("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
