package ie.gmit.sw;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.*;
import javax.servlet.http.*;

public class ServicePollHandler extends HttpServlet 
{
	private static SingletonMessaging sm;

	private static LinkedBlockingQueue<String[]> outQueue; 
	
	
	public void init() throws ServletException 
	{
		ServletContext ctx = getServletContext();
		sm= SingletonMessaging.getInstance(Integer.parseInt(ctx.getInitParameter("BLOCKINGQUEUE_SIZE")));
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setContentType("text/html"); 
		PrintWriter out = resp.getWriter(); 
		
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		int counter = 1;
		String rspBody="Preocessing...";
		String[] similarity;
		out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
		out.print("</head>");		
		out.print("<body>");
		
		try 
		{
			similarity=sm.takeOutQ(Long.parseLong(taskNumber));
			for (int i = 0; i < similarity.length; i++) 
			{
				System.out.println(similarity[i]);
			}
			if(similarity!=null)
			{
				rspBody="<style> table, th, td {\n border: 1px solid black; border-collapse: collapse;} </style>";
				rspBody+="<table style=\"width:70%\">";
				rspBody+="<tr>\n" + 
						"    <th>File Name</th>\n" + 
						"    <th>Similarity</th> \n" + 
						"  </tr>";
				for (int i = 1; i < similarity.length; i++) 
				{
					String[] aux=similarity[i].split("\\s\\s");
					System.out.println("Name: "+aux[0]+" P: "+aux[1]);
					rspBody+="<tr>\n" + 
							"    <td>"+aux[0]+"</td>\n" + 
							"    <td align=\"center\">"+aux[1]+"</td> \n" + 
							"  </tr>";
				}
				rspBody+="</table>";

						
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
		out.print("<H3>Document Title: " + title + "</H3>");
		//out.print("<b><font color=\"ff0000\">A total of " + counter + " polls have been made for this request.</font></b> ");
		//out.print("Place the final response here... a nice table (or graphic!) of the document similarity...");
		out.print(rspBody);
		out.print("<form name=\"frmRequestDetails\">");
		out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
		out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
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