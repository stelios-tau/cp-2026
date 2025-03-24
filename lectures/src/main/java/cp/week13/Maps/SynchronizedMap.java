package cp.week13.Maps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import cp.week13.Words;

public class SynchronizedMap
{
	public static void main()
	{
		// word -> number of times that it appears over all files
		Map< String, Integer > occurrences = new HashMap<>();
		
		List< String > filenames = List.of(
			"lectures/text1.txt",
			"lectures/text2.txt",
			"lectures/text3.txt",
			"lectures/text4.txt",
			"lectures/text5.txt",
			"lectures/text6.txt",
			"lectures/text7.txt",
			"lectures/text8.txt",
			"lectures/text9.txt",
			"lectures/text10.txt"
		);
		
		CountDownLatch latch = new CountDownLatch( filenames.size() );
		
		filenames.stream()
			.map( filename -> new Thread( () -> {
				computeOccurrences( filename, occurrences );
				latch.countDown();
			} ) )
			.forEach( Thread::start );

		try {
			latch.await();
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}
		
//		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}
	
	private static void computeOccurrences( String filename, Map< String, Integer > occurrences )
	{
		try {
			Files.lines( Paths.get( filename ) )
				.flatMap( Words::extractWords )
				.map( String::toLowerCase )
				.forEach( s -> {
					synchronized( occurrences ) {
						occurrences.merge( s, 1, Integer::sum );
					}
				} );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}
