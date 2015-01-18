import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Place;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class Streamer {
	
	private String CONSUMER_KEY;
	private String SECRET_KEY;
	private String ACCESS_TOKEN;
	private String SECRET_TOKEN;
	private Configuration Config;
	private Map<String,String> Languages;
	
	public Streamer() {
		loadLocales();
		loadCredentials();
        Config = buildConfigs().build();
	}
	
	public void loadLocales() {
		File fin = new File("utils/java_langs.txt");
		Languages = new HashMap<String,String>();
		Scanner reader = null;
		try {
			reader = new Scanner(fin);
			while (reader.hasNextLine()) {
				String[] arr = reader.nextLine().split("\\s+");
				Languages.put(arr[0], arr[1]);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found! Will exit now");
			System.exit(0);
		}
	}

	private void loadCredentials() {
		File fin = new File("utils/creds.txt");
		Scanner reader = null;
		try {
			reader = new Scanner(fin);
			CONSUMER_KEY = reader.nextLine();
			SECRET_KEY = reader.nextLine();
			ACCESS_TOKEN = reader.nextLine();
			SECRET_TOKEN = reader.nextLine();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found! Will exit now");
			System.exit(0);
		}
	}
	
	public Set<String> getAllHashTags(HashtagEntity[] EntityTags) {			
		Set<String> hashtags = new HashSet<String>();
		if (EntityTags == null)
			return hashtags;
		for(int i=0; i<EntityTags.length; i++)
			hashtags.add(EntityTags[0].getText().toLowerCase());
		return hashtags;
	}
	
	public List<String> getAllURLs(URLEntity[] EntityURLs) {
		List<String> urls = new ArrayList<String>();
		if (EntityURLs == null)
			return urls;
		for(int i=0; i<EntityURLs.length; i++)
			urls.add(EntityURLs[0].getURL());
		return urls;
	}
	
	
	private ConfigurationBuilder buildConfigs() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true)
    	  .setOAuthConsumerKey(CONSUMER_KEY)
    	  .setOAuthConsumerSecret(SECRET_KEY)
    	  .setOAuthAccessToken(ACCESS_TOKEN)
    	  .setOAuthAccessTokenSecret(SECRET_TOKEN);
    	return cb;
	}
	
	public void grabber() {
		TwitterStreamFactory tf = new  TwitterStreamFactory(Config);
		TwitterStream twitterStream = tf.getInstance();
        StatusListener listener = new StatusListener() {
        	int TwitterCount = 0;
            @Override
            public void onStatus(Status status) {
            	
            	TwitterCount++;
        		User user = status.getUser();
            	Set<String> hashTags = getAllHashTags(status.getHashtagEntities());
            	List<String> urls = getAllURLs(status.getURLEntities());
            	String tweet = status.getText();
            	String lang = Languages.get(status.getLang());
            	GeoLocation location = status.getGeoLocation();
            	//List<String> mentions = getAllMentions(db, status.getUserMentionEntities());
            	
            	addUserToDB(user, mentions, db);
            	addHashTagToDB(hashTags, user, status.getLang(), status.getGeoLocation(), db);
            	addLinkToDB(user, urls, db);
            	
            	Set<String> hashTags = getAllHashTags(status.getHashtagEntities());
            	if (/*hashTags.contains(keywords)*/ status.getText().toLowerCase().contains(keywords) && Languages.contains(status.getLang())) {
            		TwitterCount++;
            		User user = status.getUser();
                	List<String> urls = getAllURLs(status.getURLEntities());
                	Date date = status.getCreatedAt();
                	
                	System.out.println("Tweet #"+TwitterCount);
                	System.out.println("=======================================");
                	System.out.println("User: "+user.getScreenName());
                	
                	System.out.println("Tweet:");
                	System.out.println("---------");
                	System.out.println(status.getText());
                	/*
                	for(String ht : hashTags)
                		System.out.println(ht);
                	*/
                	System.out.println("URLs:");
                	System.out.println("---------");
                	for(String url : urls)
                		System.out.println(url);
                	System.out.println("Creation Date: "+date);
                	System.out.println();
            	}     	
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.sample();
        
	}
	
	public static void main(String[] args) {
		List<String> langs = new ArrayList<String>();
		langs.add("en");
		langs.add("fr");
		String keyword = "the";
		Streamer streamer = new Streamer(langs, keyword);
		streamer.grabber();
	}
}