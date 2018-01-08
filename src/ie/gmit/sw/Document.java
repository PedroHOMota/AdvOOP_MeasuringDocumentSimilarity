package ie.gmit.sw;

import java.util.HashSet;

/**
 * Base class to be saved on database
 * @author Pedro Mota
 *
 */
public class Document
{
	private String docName;
	private long docID;
	private HashSet<String> shingleList;
	
	/**
	 * Class' Constructor
	 * @param docID Id of the document
	 * @param docName Name of the document
	 */
	public Document(long docID,String docName)
	{
		this.docName = docName;
		this.docID = docID;
		this.shingleList = new HashSet<String>();
	}

	/**
	 * Return document's name
	 * @return document's name
	 */
	public String getDocName()
	{
		return docName;
	}
	/**
	 * Return document's id
	 * @return document's id
	 */
	public long getDocID() {
		return docID;
	}
	/**
	 * Setter
	 * @param shingleList HashSet of shingles
	 */
	public void setShingleList(HashSet<String> shingleList)
	{
		this.shingleList=shingleList;
	}

	/**
	 * Getter 
	 * @return Document's shingles hashset
	 */
	public HashSet<String> getShingleList() {
		return shingleList;
	}
	

	
}
