package ie.gmit.sw;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.db4o.ObjectSet;

public class Facade 
{
	private static DataBaseConnector db;
	
	public static Document doComputeTextFile(InputStream txtFile,long jobNumber,String title,int SHINGLE_SIZE) throws IOException
	{
		HashSet<String> hs=ShingleMaker.MakeShingles(TextFileParser.ParseFile(txtFile),SHINGLE_SIZE);
		Document doc=new Document(jobNumber, title);
		doc.setShingleList(hs);
		
		return doc;
	}
	public static String[] CalculateSimilarity(HashSet<String> doc,long docID,String dbP, long K_SHINGLESIZE) //Spawns a new thread to calculate the jaccard distance and minhash for each document
	{
		if(db==null)
			db=new DataBaseConnector(dbP);
		
		ObjectSet o = db.search(1);
		LinkedBlockingQueue<Integer> q=new LinkedBlockingQueue<>();
		HashSet<String> newDocument=(HashSet<String>) doc.clone();
		String[] result= new String[o.size()];
		int i=0;
		
		if(o.size()==0)
		{
			result = new String[1];
			result[0] = "No document on DB to compare to";		
		}
		while(o.hasNext())
		{
			Document d = (Document) o.next();
			
			/*new Thread() 
			{
				public void run()
				{*/
					float jaccard=SimilarityCalculator.CalculateJaccardDistance(newDocument, d.getShingleList());
					float minHash=SimilarityCalculator.CalculateSimilarityMinHash(newDocument, d.getShingleList(),K_SHINGLESIZE);
					System.out.println("Jaccard: "+jaccard+"/nMinHash: "+minHash);
					result[i]="Jaccard: "+jaccard+"/nMinHash: "+minHash;	
				/*}
			}.start();*/
		}
		
		return result;
	}
	public static void SaveDocumentToDatabase(Document doc)
	{
		db.insertDoc(doc);
	}
	public static ObjectSet RetrieveFromDatabase(long id)
	{
		return db.search(id);
	}
}
