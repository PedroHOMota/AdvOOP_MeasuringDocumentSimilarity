package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShingleMaker 
{
	public static HashSet<String> MakeShingles(ArrayList<String> txtFile,int SHINGLESIZE) throws IOException
	{
		ArrayList<String> parsedText = new ArrayList<String>();
		int nRows=SHINGLESIZE;
		if(parsedText.size()<SHINGLESIZE)
			nRows=parsedText.size();
		
		int i=0;
		HashSet<String> hashShingle=new HashSet<>();
		Pattern p = Pattern.compile("\\S+\\s*\\S+\\s*\\S+"); //Matches 3 words, 2 space,
		
		while(i<nRows)
		{
			Matcher m = p.matcher(parsedText.get(i));
			
			while(m.find())
			{
	            hashShingle.add(m.group(0).replaceAll("[ ]", "").toLowerCase());
	        }
			
			i++;
		}
		
		return hashShingle;
		
		
	}
}
