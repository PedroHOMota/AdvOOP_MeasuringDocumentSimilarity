package ie.gmit.sw;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

/**
 * Singleton Database class
 * @author Pedro Mota
 *
 */
public class DataBase //Singleton
{
	private static DataBase db = null;
	private static ObjectContainer dbo;
	private static String dbPath="";
	
	/**
	 * Empty constructor to prevent instantiation
	 */
	private DataBase()
	{
	}
	/**
	 * Creates an instance of the database if none exists
	 * @param dbPath path to the database file
	 * @return The instance of the database
	 */
	public static DataBase getInstance(String dbPath)
	{
		if(db==null)
		{
			db=new DataBase();
			dbo = Db4o.openFile(dbPath);
		}
		return db;
	}
	
	/**
	 * Returns an Image object that can then be painted on the screen. 
	 * The url argument must specify an absolute {@link URL}. The name
	 * argument is a specifier that is relative to the url argument. 
	 * <p>
	 * This method saves  
	 *
	 * @param  doc  The document to be saved on the database
	 *
	 */
	public void insertDoc(Document doc)
	{
		dbo.store(doc);
		dbo.commit();
	}
	/**
	 * Retrieves all documents on the database
	 * @return An ObjectSet containing all documents in the database
	 */
	public ObjectSet searchAll()
	{
		Query query = dbo.query();
		query.constrain(Document.class);
		ObjectSet<Document> result = query.execute();
		return result;
		
	}

}
