package ie.gmit.sw;

import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

public class SimilarityCalculator 
{

	public static float CalculateJaccardDistance(HashSet<String> newDocument,HashSet<String> documentOnDB)
	{
		float currentsize=newDocument.size();
		newDocument.retainAll(documentOnDB); //modifies the hashset calling emoving what is not on the other set
										//DO this against set from db
		System.out.println(newDocument);
		return newDocument.size()/(currentsize+documentOnDB.size()-newDocument.size());
	
	}
	
	public static float CalculateSimilarityMinHash(HashSet<String> newDocument,HashSet<String> documentOnDB,float k)
	{
		//HashSet<String> hashes = new HashSet<String>(newDocument); //Use of the hashse as it has a O(1) complexity
													 //After the shingles are made the order they appear on the set doesn't matter
													//As all shingle will be compareted to each other
		//hashes.retainAll(documentOnDB);
		
		HashSet<Integer> hashes = new HashSet<Integer>();
		Random r = new Random();
		for (int i = 0; i < k; i++)
		{ //Create k random integers
			hashes.add(r.nextInt());
		}
		
		HashSet<Integer> nD=CalculateMinHash(newDocument, hashes);
		HashSet<Integer> dOnDb=CalculateMinHash(documentOnDB, hashes);
		for(int i:nD)
		{
			System.out.println(i);
		}
		nD.retainAll(dOnDb);
		return nD.size()/k;
	
	}
	
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
