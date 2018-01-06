package ie.gmit.sw;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class DataBaseConnectorTest
{
	private ObjectContainer db = null;	
	
	private void connectDB()
	{
		db= Db4o.openFile("jaccardDB");
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
