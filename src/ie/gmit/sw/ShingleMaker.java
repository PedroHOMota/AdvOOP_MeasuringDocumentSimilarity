package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class ShingleMaker 
{
	public static HashSet<Integer> MakeShingles(ArrayList<String> txtFile) throws IOException
	{
		ArrayList<String> parsedText = new ArrayList<String>();
		int nRows=SHINGLEMAXLIST;
		if(parsedText.size()<SHINGLEMAXLIST)
			nRows=parsedText.size();
		
		int i=0;
		HashSet<Integer> hashShingle=new HashSet<>();
		while(i<nRows)
		{
			String[] aux=parsedText.get(i).split("[ ]");
			String shingleAux="";
			shingleAux = aux[i]+aux[i+1]+aux[i+2];
			hashShingle.add(shingleAux.hashCode());
			i+=3;
		}
		
		return hashShingle;
		
		
	}
}
