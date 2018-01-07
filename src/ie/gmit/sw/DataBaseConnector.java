package ie.gmit.sw;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class DataBaseConnector
{
	private static DataBase db=null;
	
	DataBaseConnector(String dbPath)
	{
		db=DataBase.getInstance(dbPath);
	}
	public void insertDoc(Document doc)
	{
		db.insertDoc(doc);
	}
	
	public ObjectSet search(long id)
	{
		return db.searchAll();
	}
}
