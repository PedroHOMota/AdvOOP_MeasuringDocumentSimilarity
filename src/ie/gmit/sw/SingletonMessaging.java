package ie.gmit.sw;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Singleton class to have in between servlets messaging
 * @author Pedro Mota
 *
 */
public class SingletonMessaging
{
	private static LinkedBlockingQueue<Document> inQueue;
	private static ConcurrentMap<Long,String[]> outQueue; 
	
	private static SingletonMessaging sm=null;
	private SingletonMessaging() {}
	
	/**
	 * Creates an instance of the class if none exists
	 * @param queueSize Size of the in queue
	 * @return An Instance of SingletonMessaging
	 */
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
	/**
	 * Takes an item from queue, uses queue.take() blocking method
	 * @return A document from queue if one exists, otherwise threaded goes to sleep
	 * @throws InterruptedException
	 */
	public Document takeInQ() throws InterruptedException
	{
		return inQueue.take();
	}
	/**
	 * Puts an item to queue, uses queue.put() blocking method
	 * @param d Document to be saved on database
	 * @throws InterruptedException
	 */
	public void putInQ(Document d) throws InterruptedException
	{
		inQueue.put(d);
	}
	/**
	 * Takes an item out of the if its id is found on hashmap
	 * @param jobID Item's id to be searched on queue
	 * @return Returns the item desired item from queue
	 * @throws InterruptedException
	 */
	public String[] takeOutQ(Long jobID) throws InterruptedException //Return null if cant find the job on queue
	{
		System.out.println("Retrieve: "+jobID);
		if(outQueue.containsKey(jobID))
		{
			String[] aux = outQueue.get(jobID);
			return aux;
		}
		
		return null;
	}
	/**
	 * Saves an document on the outqueue
	 * @param jobID Id used to retrieve saved document from hashmap
	 * @param d Document to be put on hashmap
	 * @throws InterruptedException
	 */
	public void putOutQ(Long jobID,String[] d) throws InterruptedException
	{
		System.out.println("Insert: "+jobID);
		outQueue.putIfAbsent(jobID, d);
	}
	
	
}
