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
public class ServiceHandler extends HttpServlet {
	
	private static long jobNumber = 0;

	private static int SHINGLE_SIZE;
	private static String DB_PATH;
	//private static int BLOCKINGQUEUE_SIZE;
	private static DataBaseConnector db;
	private static LinkedBlockingQueue<Document> inQueue;
	private static LinkedBlockingQueue<String[]> outQueue;
	private static SingletonMessaging sm;
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
		/*inQueue = new LinkedBlockingQueue<>(Integer.parseInt(ctx.getInitParameter("BLOCKINGQUEUE_SIZE")));*/
		outQueue = new LinkedBlockingQueue<>(); 
		sm= SingletonMessaging.getInstance(Integer.parseInt(ctx.getInitParameter("BLOCKINGQUEUE_SIZE")));
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
							
							//doc = inQueue.take();
							doc=sm.takeInQ();
							System.out.println(doc.getDocID());
							System.out.println("Preocessing request");
							String[] d=Facade.CalculateSimilarity(doc.getShingleList(), doc.getDocID(),DB_PATH,K_SHINGLESIZE);
							sm.putOutQ(doc.getDocID(),d);
							for (int j = 1; j < d.length; j++)
							{
								System.out.println("SIMILARITY: "+d[j]);
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
		resp.setContentType("text/html"); 
		
		PrintWriter out = resp.getWriter(); 
		
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		Part part = req.getPart("txtDocument");

		out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
		out.print("</head>");		
		out.print("<body>");
		
		if (taskNumber == null)
		{
			taskNumber = Long.toString(jobNumber);
			try {
				Document doc=Facade.doComputeTextFile(part.getInputStream(), jobNumber, title, SHINGLE_SIZE);
				sm.putInQ(doc);//inQueue.put(doc);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			jobNumber++;
			
		}
				
		out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
		out.print("<H3>Document Title: " + title + "</H3>");
		
		out.println("<p>Processing...</p>");
		out.print("<div id=\"r\"></div>");
				
		out.print("<form name=\"frmRequestDetails\" action=\"poll\">");
		out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
		out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("</form>");								
		out.print("</body>");	
		out.print("</html>");	
		
		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 10000);"); //Refresh every 10 seconds
		out.print("</script>");
		
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
}