package ie.gmit.sw;

import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

public class SimilarityCalculator 
{

	public float CalculateJaccardDistance(HashSet<Integer> newDocument,HashSet<Integer> documentOnDB)
	{
		float currentsize=newDocument.size();
		newDocument.retainAll(documentOnDB); //modifies the hashset calling emoving what is not on the other set
										//DO this against set from db
		
		return newDocument.size()/(currentsize+documentOnDB.size()-newDocument.size());
	
	}
	
	public float CalculateSimilarityMinHash(HashSet<Integer> newDocument,HashSet<Integer> documentOnDB,float k)
	{
		HashSet<Integer> hashes = new HashSet<Integer>(newDocument); //Use of the hashse as it has a O(1) complexity
													 //After the shingles are made the order they appear on the set doesn't matter
													//As all shingle will be compareted to each other
		hashes.retainAll(documentOnDB);
		
		newDocument=CalculateMinHash(newDocument, k);
		documentOnDB=CalculateMinHash(documentOnDB, k);
		
		newDocument.retainAll(documentOnDB);
		return newDocument.size()/k;
	
	}
	
	private HashSet<Integer> CalculateMinHash(HashSet<String> document,int k) //Adapted from John's notes
	{
		HashSet<Integer> hashes = new HashSet<Integer>();
		HashSet<Integer> shingles = new HashSet<Integer>();
		
		Random r = new Random();
		for (int i = 0; i < k; i++)
		{ //Create k random integers
			hashes.add(r.nextInt());
		}
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
