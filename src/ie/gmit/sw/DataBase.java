package ie.gmit.sw;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class DataBase //Singleton
{
	private static DataBase db = null;
	private static ObjectContainer dbo;
	private static String dbPath="";
	
	private DataBase(String dbPath)
	{
		this.dbPath=dbPath;
	}
	
	public static DataBase getInstance(String dbPath)
	{
		if(db==null)
		{
			db=new DataBase(dbPath);
			dbo = Db4o.openFile(dbPath);
		}
		return db;
	}
	
	public void insertDoc(Document doc)
	{
		dbo.store(doc);
	}
	
	public ObjectSet search(long id)
	{
		ObjectSet result=dbo.queryByExample(new Document(id,""));
		return result;	
	}

}
