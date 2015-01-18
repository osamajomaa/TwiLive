import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import twitter4j.GeoLocation;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;


public class Database {
	
	public  DB db;
	public Map<String, String> Languages;
	public int LANGS_COUNT;
	String Server;
	int PORT;
	String dbName;
	public Database(String server, int port, String dbname) {
		Server = server;
		PORT = port;
		dbName = dbname;
	}
	
	public void createDB(String server, int port, String dbName) {
		
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(server, port);
		} catch (UnknownHostException e) {
			System.out.println("Error. Unknown Host!");
			System.exit(0);
		}
		db = mongoClient.getDB(dbName);
	}
	
	public void createCollection(String collName) {
		DBObject options = new BasicDBObject();
		options.put("autoIndexId", true);
		db.createCollection(collName, options);
	}
	
	public void createUniqueIndex(String collName, String fieldName) {
		DBCollection coll = db.getCollection(collName);
		DBObject index = new BasicDBObject();
		index.put(fieldName,1);
		DBObject nameIndexOptions = new BasicDBObject();
		nameIndexOptions.put("unique", true);
		nameIndexOptions.put("sparse", true);		
		coll.createIndex(index, nameIndexOptions);
	}
	
	
	public void addTweet(Tweet tweet) {
		DBCollection tweets = db.getCollection("Tweet");
		BasicDBList hashtags = new BasicDBList();
		BasicDBList links = new BasicDBList();
		for(String hashtag: tweet.getHashTags())
			hashtags.add(new BasicDBObject("HashTag", hashtag));
		for(String url: tweet.getLinks())
			links.add(new BasicDBObject("Link", url));
		BasicDBObject tweetObj = new BasicDBObject("TweetText", tweet.getText())
							.append("UserName", tweet.getUserName())
							.append("Lang", tweet.getLang())
							.append("HashTags", hashtags)
							.append("Links", links)
							.append("Date", tweet.getCreateDate())
							.append("Location", new BasicDBObject("Lat", tweet.getLoc().Latitude).append("Long", tweet.getLoc().Longitude));
		
		try {
			tweets.insert(tweetObj);
		} catch(DuplicateKeyException exc) {
			//If user already exists, do nothing..
		}
	}

}