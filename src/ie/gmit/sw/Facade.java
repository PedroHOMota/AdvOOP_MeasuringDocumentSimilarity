package ie.gmit.sw;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.db4o.ObjectSet;

/**
 * Facade class responsible for hiding all details and complications of Parsing the stream and calculating the similarity
 * @author Pedro Mota
 *
 */
public class Facade 
{
	private static DataBaseConnector db;
	
	/**
	 * Method responsible to parse the stream, make shingles and create a document
	 * @param txtFile Stream containing the file to be parsed
	 * @param jobNumber The number that will be used as an id for the document
	 * @param title The document's title
	 * @param SHINGLE_SIZE The size of shingles that will be used to calculate the similarity between documents
	 * @return Returns an document with a list on shingles
	 * @throws IOException If the stream can't be parsed
	 */
	public static Document doComputeTextFile(InputStream txtFile,long jobNumber,String title,int SHINGLE_SIZE) throws IOException 
	{
		HashSet<String> hs=ShingleMaker.MakeShingles(TextFileParser.ParseFile(txtFile),SHINGLE_SIZE);
		Document doc=new Document(jobNumber, title);
		doc.setShingleList(hs);
		
		return doc;
	}
	/**
	 * Query for all documents on the database to do the similarity comparison 
	 * @param doc Document uploaded by the user to be compared against the ones in the database
	 * @param docID Document's ID
	 * @param dbP Path to where th database is located
	 * @param K_SHINGLESIZE Determines how many shingles will be used on MinHas 
	 * @return Returns an array containing  
	 */
	public static String[] CalculateSimilarity(HashSet<String> doc,long docID,String dbP, long K_SHINGLESIZE) //Spawns a new thread to calculate the jaccard distance and minhash for each document
	{
		if(db==null)
			db=new DataBaseConnector(dbP);
		
		ObjectSet o = db.search();
		LinkedBlockingQueue<Integer> q=new LinkedBlockingQueue<>();
		HashSet<String> newDocument=(HashSet<String>) doc.clone();
		String[] result= new String[o.size()+1];
		int i=1;
		
		if(o.size()==0)
		{
			result = new String[1];
			result[0] = "No document on DB to compare to";		
			return result;
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
					result[i]=d.getDocName()+"  Jaccard: "+(int)(jaccard*100)+"%\nMinHash: "+(int)(minHash*100)+"%";	
					i++;
				/*}
			}.start();*/
		}
		result[0] =Long.toString(docID);
		return result;
	}
	/**
	 * Delegates the saving to the appropriate class
	 * @param doc Document to be saved on database
	 */
	public static void SaveDocumentToDatabase(Document doc)
	{
		db.insertDoc(doc);
	}
	/**
	 * Delegates the search of the database to the appropriate class
	 * @return ObjectSet containing all documents on database
	 */
	public static ObjectSet RetrieveFromDatabase()
	{
		return db.search();
	}
}
