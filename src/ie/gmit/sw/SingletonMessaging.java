package ie.gmit.sw;

import java.util.concurrent.LinkedBlockingQueue;

public class SingletonMessaging
{
	private static LinkedBlockingQueue<Document> inQueue;
	private static LinkedBlockingQueue<String[]> outQueue; 
	
	private static SingletonMessaging sm=null;
	private SingletonMessaging() {}
	
	public static SingletonMessaging getInstance(int queueSize)
	{
		if(sm==null)
		{
			sm = new SingletonMessaging();
			inQueue = new LinkedBlockingQueue<>(queueSize);
			outQueue = new LinkedBlockingQueue<>(); 
		}
		
		return sm;
	}
	
	public Document takeInQ() throws InterruptedException
	{
		return inQueue.take();
	}
	
	public void putInQ(Document d) throws InterruptedException
	{
		inQueue.put(d);
	}
	
	public String[] takeOutQ(String jobID) throws InterruptedException //Return null if cant find the job on queue
	{
		for(String[] aux:outQueue)
		{
			if(aux[0]==jobID)
				return aux;
		}
		return null;
	}
	
	public void putOutQ(String[] d) throws InterruptedException
	{
		outQueue.put(d);
	}
	
	
}
