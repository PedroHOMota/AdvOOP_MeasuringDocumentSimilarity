package ie.gmit.sw;

import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

/**
 * Calculates Jaccard and MinHash similarity between two documents
 * @author Pedro Mota
 *
 */
public class SimilarityCalculator 
{

	/**
	 * Calculates jaccard similarity between two documents
	 * @param newDocument An HashSet containing shingles of a document that will be compared against another
	 * @param documentOnDB An HashSet containing shingles of a document that will be compared against another
	 * @return
	 */
	public static float CalculateJaccardDistance(HashSet<String> newDocument,HashSet<String> documentOnDB)
	{
		float currentsize=newDocument.size();
		newDocument.retainAll(documentOnDB); //Removes what is not on second set from the first
		return newDocument.size()/(currentsize+documentOnDB.size()-newDocument.size());
	
	}
	/**
	 * Calculates using minHash similarity between two documents
	 * @param newDocument An HashSet containing shingles of a document that will be compared against another
	 * @param documentOnDB An HashSet containing shingles of a document that will be compared against another
	 * @param k Number of shingles that will be used for comparison
	 * @return
	 */
	public static float CalculateSimilarityMinHash(HashSet<String> newDocument,HashSet<String> documentOnDB,float k)
	{
		HashSet<Integer> hashes = new HashSet<Integer>(); //Calculate the hashes here so both set have the same randoms
		Random r = new Random();
		for (int i = 0; i < k; i++)
		{ //Create k random integers
			hashes.add(r.nextInt());
		}
		
		HashSet<Integer> nD=CalculateMinHash(newDocument, hashes);
		HashSet<Integer> dOnDb=CalculateMinHash(documentOnDB, hashes);
		
		nD.retainAll(dOnDb);
		return nD.size()/k;
	
	}
	/**
	 * Generates the integer hashes to be used for comparison
	 * @param document HashSet with the shingles
	 * @param hashes HashSet of random int values
	 * @return
	 */
	private static HashSet<Integer> CalculateMinHash(HashSet<String> document,HashSet<Integer> hashes) //Adapted from John's notes
	{
		HashSet<Integer> shingles = new HashSet<Integer>();
		
		//XOR the integer word values with the hashes
		for (Integer hash : hashes)
		{
			 int min = Integer.MAX_VALUE;
			 for (String word : document)
			 {
				 int minHash = word.hashCode() ^ hash; //Bitwise XOR the string hashCode with the hash
				 if (minHash < min) min = minHash;
			 }
			 shingles.add(min); //Only store the shingle with the minimum hash for each hash function
		}
		
		return shingles;
	}
	
}
