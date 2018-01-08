package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Creates shingles from an ArrayList
 * @author Pedro Mota
 *
 */
public class ShingleMaker 
{
	/**
	 * Creates X words shingles
	 * @param txtFile File used as a base for the shingles
	 * @param SHINGLESIZE Number of words that will used for each shingle
	 * @return A HashSet with X words shingles
	 * @throws IOException
	 */
	public static HashSet<String> MakeShingles(ArrayList<String> txtFile,int SHINGLESIZE) throws IOException
	{
		String pattern="";
		
		for (int i = 0; i < SHINGLESIZE-1; i++) //-1 to prevent the pattern to match an unnecessary space
		{
			pattern+="\\S+\\s*";
		}
		pattern+="\\S+";
		int i=0;
		HashSet<String> hashShingle=new HashSet<>();
		Pattern p = Pattern.compile("\\S+\\s*\\S+\\s*\\S+"); //Matches 3 words, 2 space,
		
		while(i<txtFile.size())
		{
			Matcher m = p.matcher(txtFile.get(i));
			
			while(m.find())
			{
				hashShingle.add(m.group(0).replaceAll("[ ]", "").toLowerCase());
	        }
			
			i++;
		}
		
		return hashShingle;
		
		
	}
}
