package ie.gmit.sw;

import java.util.HashSet;

public class Document
{
	private String docName;
	private long docID;
	private HashSet<String> shingleList;
	
	public Document(long docID,String docName)
	{
		this.docName = docName;
		this.docID = docID;
		this.shingleList = new HashSet<String>();
	}

	public String getDocName()
	{
		return docName;
	}
	public long getDocID() {
		return docID;
	}

	public void setShingleList(HashSet<String> shingleList)
	{
		this.shingleList=shingleList;
	}

	public HashSet<String> getShingleList() {
		return shingleList;
	}
	

	
}
