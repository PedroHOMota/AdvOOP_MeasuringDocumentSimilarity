package ie.gmit.sw;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB. The file size in bytes after which the file will be temporarily stored on disk. The default size is 0 bytes.
                 maxFileSize=1024*1024*10,      // 10MB. The maximum size allowed for uploaded files, in bytes
                 maxRequestSize=1024*1024*50)   // 50MB. he maximum size allowed for a multipart/form-data request, in bytes.
public class ServiceHandler2Testing extends HttpServlet {
	
	private static long jobNumber = 0;

	private static int SHINGLE_SIZE;
	private static String DB_PATH;
	//private static int BLOCKINGQUEUE_SIZE;
	private static DataBaseConnector db;
	private static LinkedBlockingQueue<Document> inQueue;
	private static LinkedBlockingQueue<String[]> outQueue; 
	private static ExecutorService threadPool;
	private static long K_SHINGLESIZE;
	private static boolean keepAlive=true;
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		SHINGLE_SIZE=Integer.parseInt(ctx.getInitParameter("SHINGLE_SIZE"));
		K_SHINGLESIZE=Integer.parseInt(ctx.getInitParameter("SHINGLE_SIZE"));
		//DB_PATH=ctx.getInitParameter("BLOCKINGQUEUE_SIZE");
		DB_PATH=ctx.getInitParameter("DB_PATH");
		db=new DataBaseConnector(DB_PATH);
		inQueue = new LinkedBlockingQueue<>(Integer.parseInt(ctx.getInitParameter("BLOCKINGQUEUE_SIZE")));
		outQueue = new LinkedBlockingQueue<>(); 
		threadPool = Executors.newFixedThreadPool(Integer.parseInt(ctx.getInitParameter("THREADPOOL_SIZE")));
		
		
		for (int i = 0; i < Integer.parseInt(ctx.getInitParameter("THREADPOOL_SIZE")); i++) 
		{
			threadPool.execute(new Runnable()
			{
				public void run() 
				{
					while(keepAlive)
					{
						System.out.println(this.toString()+" is runining");
						Document doc;
						try {
							System.out.println("Taking from queue");
							
							doc = inQueue.take();
							System.out.println("Preocessing request");
							String[] a=Facade.CalculateSimilarity(doc.getShingleList(), doc.getDocID(),DB_PATH,K_SHINGLESIZE);
							outQueue.put(a);
							for(String n : a)
							{
								System.out.println("SIMILARITY: "+n);
							}
							System.out.println("Saving to db");
							Facade.SaveDocumentToDatabase(doc);
							System.out.println("Saved");
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				}
			});
					
		}

		
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Step 1) Write out the MIME type
		resp.setContentType("text/html"); 
		
		//Step 2) Get a handle on the PrintWriter to write out HTML
		PrintWriter out = resp.getWriter(); 
		
		//Step 3) Get any submitted form data. These variables are local to this method and thread safe...
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		Part part = req.getPart("txtDocument");

		
		//Step 4) Process the input and write out the response. 
		//The following string should be extracted as a context from web.xml 
		out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
		out.print("</head>");		
		out.print("<body>");
		
		//We could use the following to track asynchronous tasks. Comment it out otherwise...
		if (taskNumber == null)
		{
			taskNumber = new String("T" + jobNumber);
			jobNumber++;
			/*Facade fc= new Facade();
			HashSet<String> hs=ShingleMaker.MakeShingles(TextFileParser.ParseFile(part.getInputStream()),SHINGLE_SIZE);
			Document doc=new Document(jobNumber, title);
			doc.setShingleList(hs);
			*/
			try {
				Document doc=Facade.doComputeTextFile(part.getInputStream(), jobNumber, title, SHINGLE_SIZE);
				inQueue.put(doc);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else
		{
			RequestDispatcher dispatcher = req.getRequestDispatcher("/poll");
			if(outQueue.contains(taskNumber));
			{
				Iterator it = outQueue.iterator();
				while(it.hasNext())
				{
					String[] doc = (String[]) it.next();
					if(doc[0]==taskNumber)
					{
						req.setAttribute("calculated", doc);
						it.remove();
						break;
					}
				}
			}
			dispatcher.forward(req,resp);
			//Check out-queue for finished job with the given taskNumber
		}
		
		//Output some headings at the top of the generated page
		out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
		out.print("<H3>Document Title: " + title + "</H3>");
		
		
		//Output some useful information for you (yes YOU!)
		out.print("<div id=\"r\"></div>");
		out.print("<font color=\"#993333\"><b>");
		out.print("<br>This servlet should only be responsible for handling client request and returning responses. Everything else should be handled by different objects.");
		out.print("Note that any variables declared inside this doGet() method are thread safe. Anything defined at a class level is shared between HTTP requests.");				
		out.print("</b></font>");
		
		out.print("<h3>Compiling and Packaging this Application</h3>");
		out.print("Place any servlets or Java classes in the WEB-INF/classes directory. Alternatively package "); 
		out.print("these resources as a JAR archive in the WEB-INF/lib directory using by executing the ");  
		out.print("following command from the WEB-INF/classes directory jar -cf my-library.jar *");
		
		out.print("<ol>");
		out.print("<li><b>Compile on Mac/Linux:</b> javac -cp .:$TOMCAT_HOME/lib/servlet-api.jar WEB-INF/classes/ie/gmit/sw/*.java");
		out.print("<li><b>Compile on Windows:</b> javac -cp .;%TOMCAT_HOME%/lib/servlet-api.jar WEB-INF/classes/ie/gmit/sw/*.java");
		out.print("<li><b>Build JAR Archive:</b> jar -cf jaccard.war *");
		out.print("</ol>");
		
		//We can also dynamically write out a form using hidden form fields. The form itself is not
		//visible in the browser, but the JavaScript below can see it.
		out.print("<form name=\"frmRequestDetails\" action=\"poll\">");
		out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
		out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("</form>");								
		out.print("</body>");	
		out.print("</html>");	
		
		//JavaScript to periodically poll the server for updates (this is ideal for an asynchronous operation)
		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 10000);"); //Refresh every 10 seconds
		out.print("</script>");
		
		out.print("<h3>Uploaded Document</h3>");	
		out.print("<font color=\"0000ff\">");
		
		
		out.print("</font>");	
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
}