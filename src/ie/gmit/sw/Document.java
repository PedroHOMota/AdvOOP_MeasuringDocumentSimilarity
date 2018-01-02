package ie.gmit.sw;

import java.util.ArrayList;

public class Document
{
	public long docID;
	private int[] shingleList;
	
	public Document(long docID)
	{
		this.docID = docID;
		this.shingleList = new int[SHINGLELIST_SIZE];
	}

	public long getDocID() {
		return docID;
	}


	public int[] getShingleList() {
		return shingleList;
	}

	
}
