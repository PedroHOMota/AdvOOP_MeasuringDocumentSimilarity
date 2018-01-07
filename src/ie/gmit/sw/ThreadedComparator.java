package ie.gmit.sw;

public class ThreadedComparator implements Runnable
{

	private Document newDocument;
	private Document documentOnDB;
	public ThreadedComparator(int jobID,Document newDocument,Document documentOnDB)
	{
	//	this.newDocument=
	}
	public void run(int jobID,Document newDocument,Document documentoOnDB) 
	{
		//Access Db and retrieve the data
		//Give a copy of the object
		//Control access to db file with a blockingqueue?
		
		//Each interaction of DB's object set spawns a thread
		//Thread puts the return on a blocking queue
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
	
}
