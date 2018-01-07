package ie.gmit.sw;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

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
		dbo.commit();
	}
	
	public ObjectSet searchAll()
	{
		Query query = dbo.query();
		query.constrain(Document.class);
		ObjectSet<Document> result = query.execute();
		return result;
		
	}

}
