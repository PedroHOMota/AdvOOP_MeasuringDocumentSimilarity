package ie.gmit.sw;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class DataBaseConnectorTest
{
	private static void db
	private ObjectContainer queryResult = null;	
	private static String dbPath="";
	
	DataBaseConnectorTest(String dbPath)
	{
		this.dbPath=dbPath;
	}
	/*public void setdbPath(String path) 
	{
		dbPath=path;
	}*/
	private void connectDB()
	{
		db= Db4o.openFile(dbPath);
	}
	
	private void closeDB()
	{
		db.close();
	}
	
	public void insertDoc(Document doc)
	{
		connectDB();
		db.store(doc);
		db.close();
	}
	
	public ObjectSet search(long id)
	{
		connectDB();
		ObjectSet result=db.queryByExample(new Document(id,""));
		return result;	
	}
}
