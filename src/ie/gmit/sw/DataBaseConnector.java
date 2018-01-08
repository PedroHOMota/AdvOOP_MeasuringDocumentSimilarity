package ie.gmit.sw;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * Connector to the database
 * @author Pedro Mota
 *
 */
public class DataBaseConnector
{
	private static DataBase db=null;
	/**
	 * Class constructor
	 * @param dbPath database's path
	 */
	DataBaseConnector(String dbPath)
	{
		db=DataBase.getInstance(dbPath);
	}
	/**
	 * Saves a document to database
	 * @param doc Document to be saved
	 */
	public void insertDoc(Document doc)
	{
		db.insertDoc(doc);
	}
	/**
	 * Querys for all documents on database
	 * @return An set with all documents saved on database
	 */
	public ObjectSet search()
	{
		return db.searchAll();
	}
}
