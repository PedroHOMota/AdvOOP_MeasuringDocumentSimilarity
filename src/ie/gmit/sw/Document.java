package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashSet;

public class Document
{
	public long docID;
	private HashSet<String> shingleList;
	
	public Document(long docID)
	{
		this.docID = docID;
		this.shingleList = new HashSet<String>();
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
