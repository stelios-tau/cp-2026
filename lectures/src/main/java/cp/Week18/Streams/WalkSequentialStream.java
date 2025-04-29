package cp.Week18.Streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import cp.Words;

public class WalkSequentialStream
{
	public static void main(String[] args)
	{
		// word -> number of times it appears over all files
		Map< String, Integer > occurrences = new HashMap<>();
		
		try {
			Files
				.walk( Paths.get( "lectures/data" ) )
				.filter( Files::isRegularFile )
				.forEach( filepath -> computeOccurrences( filepath, occurrences ) );
		} catch( IOException e ) {
			e.printStackTrace();
		}
		
//		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}
	
	private static void computeOccurrences( Path textFile, Map< String, Integer > occurrences )
	{
		try {
			Files.lines( textFile )
				.flatMap( Words::extractWords )
				.map( String::toLowerCase )
				.forEach( s -> occurrences.merge( s, 1, Integer::sum ) );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}
