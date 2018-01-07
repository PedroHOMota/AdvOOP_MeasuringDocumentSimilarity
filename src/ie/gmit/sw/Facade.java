package ie.gmit.sw;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.db4o.ObjectSet;

public class Facade 
{
	private static DataBaseConnectorTest db=new DataBaseConnectorTest();
	
	public static Document doComputeTextFile(InputStream txtFile,long jobNumber,String title,int SHINGLE_SIZE) throws IOException
	{
		HashSet<String> hs=ShingleMaker.MakeShingles(TextFileParser.ParseFile(txtFile),SHINGLE_SIZE);
		Document doc=new Document(jobNumber, title);
		doc.setShingleList(hs);
		
		return doc;
	}
	public static void CalculateSimilarity(HashSet<String> doc,final float k) //Spawns a new thread to calculate the jaccard distance and minhash for each document
	{
		ObjectSet o = db.search(1);
		LinkedBlockingQueue<Integer> q=new LinkedBlockingQueue<>();
		final HashSet<String> newDocument=doc;
		while(o.hasNext())
		{
			final Document d = (Document) o.next();
			
			new Thread() 
			{
				public void run()
				{
					float jaccard=SimilarityCalculator.CalculateJaccardDistance(newDocument, d.getShingleList());
					float minHash=SimilarityCalculator.CalculateSimilarityMinHash(newDocument, d.getShingleList(),k);
				}
			}.start();
		}
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
