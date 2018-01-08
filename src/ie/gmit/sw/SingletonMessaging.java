package ie.gmit.sw;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

public class SingletonMessaging
{
	private static LinkedBlockingQueue<Document> inQueue;
	private static ConcurrentMap<Long,String[]> outQueue; 
	
	private static SingletonMessaging sm=null;
	private SingletonMessaging() {}
	
	public static SingletonMessaging getInstance(int queueSize)
	{
		if(sm==null)
		{
			sm = new SingletonMessaging();
			inQueue = new LinkedBlockingQueue<>(queueSize);
			outQueue = new ConcurrentHashMap<Long,String[]>();
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
		if(outQueue.containsKey(jobID))
			return outQueue.get(jobID);
		
		return null;
	}
	
	public void putOutQ(Long jobID,String[] d) throws InterruptedException
	{
		outQueue.putIfAbsent(jobID, d);
	}
	
	
}
