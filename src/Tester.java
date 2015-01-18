import java.text.SimpleDateFormat;
import java.util.Date;


public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		Date date = new Date();SimpleDateFormat sdf;
	    sdf = new SimpleDateFormat("hh:mm:ss");
		System.out.println(sdf.format(date));
		*/
		
		
		Database db = new Database("localhost", 27017, "LiveTweets");
		db.createDB();
		
		
		//db.createCollection("Tweet");
		//db.createUniqueIndex("Tweet", "Date")
		
		
		Streamer streamer = new Streamer();
		streamer.grabber(db);
		
	}

}
