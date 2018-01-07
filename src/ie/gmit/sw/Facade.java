package ie.gmit.sw;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public class Facade 
{
	private static DataBaseConnectorTest db=new DataBaseConnectorTest();
	
	public static Document doCompute(InputStream txtFile,long jobNumber,String title,int SHINGLE_SIZE) throws IOException
	{
		HashSet<String> hs=ShingleMaker.MakeShingles(TextFileParser.ParseFile(txtFile),SHINGLE_SIZE);
		Document doc=new Document(jobNumber, title);
		doc.setShingleList(hs);
		
		return doc;
	}
	public static void SaveDocumentToDatabase(Document doc)
	{
		db.insertDoc(doc);
	}
}
