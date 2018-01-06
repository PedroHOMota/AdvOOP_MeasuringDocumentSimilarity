package ie.gmit.sw;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ServicePollHandler extends HttpServlet 
{
	private int SHINGLELIST_SIZE;
	private int SHINGLE_SIZE;
	private String DB_PATH;
	private int BLOCKINGQUEUE_SIZE;
	
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		SHINGLELIST_SIZE=Integer.parseInt(ctx.getInitParameter("SHINGLELIST_SIZE"));
		SHINGLE_SIZE=Integer.parseInt(ctx.getInitParameter("SHINGLE_SIZE"));
		BLOCKINGQUEUE_SIZE=Integer.parseInt(ctx.getInitParameter("BLOCKINGQUEUE_SIZE"));
		DB_PATH=ctx.getInitParameter("BLOCKINGQUEUE_SIZE");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setContentType("text/html"); 
		PrintWriter out = resp.getWriter(); 
		
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		int counter = 1;
		if (req.getParameter("counter") != null){
			counter = Integer.parseInt(req.getParameter("counter"));
			counter++;
		}

		out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
		out.print("</head>");		
		out.print("<body>");
		out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
		out.print("<H3>Document Title: " + title + "</H3>");
		out.print("<b><font color=\"ff0000\">A total of " + counter + " polls have been made for this request.</font></b> ");
		out.print("Place the final response here... a nice table (or graphic!) of the document similarity...");
		
		out.print("<form name=\"frmRequestDetails\">");
		out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
		out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("<input name=\"counter\" type=\"hidden\" value=\"" + counter + "\">");
		out.print("</form>");								
		out.print("</body>");	
		out.print("</html>");	
		
		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 5000);"); //Refresh every 5 seconds
		out.print("</script>");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		doGet(req, resp);
 	}
}