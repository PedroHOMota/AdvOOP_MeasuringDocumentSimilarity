package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 * Parses stream and removes certain characters  
 * @author Pedro Mota
 *
 */
public class TextFileParser 
{
	/**
	 * Parses a stream line by line and removes certain characters for shingling later
	 * @param txtFile InputStream contained a text file
	 * @return An ArrayList with parsed text
	 * @throws IOException
	 */
	public static ArrayList<String> ParseFile(InputStream txtFile) throws IOException
	{
		ArrayList<String> parsedText = new ArrayList<String>();
		String line="";
		BufferedReader reader = new BufferedReader(new InputStreamReader(txtFile));
		
		while((line=reader.readLine())!=null)
		{
			parsedText.add(line.replaceAll("[^ 0-9a-zA-Z]", ""));
		}
		
		return parsedText;
		
	}
}
