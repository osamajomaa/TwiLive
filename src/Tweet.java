import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.URLEntity;
import twitter4j.User;


class Location {
	double Longitude;
	double Latitude;
	Location(double lng, double ltd) {
		Longitude = lng;
		Latitude = ltd;
	}
}

public class Tweet {
	
	private String UserName;
	private Set<String> HashTags;
	private Set <String> Links;
	private String Text;
	private String Lang;
	private Location Loc;
	private Date createDate;
	
	public Set<String> getAllHashTags(HashtagEntity[] EntityTags) {			
		Set<String> hashtags = new HashSet<String>();
		if (EntityTags == null)
			return hashtags;
		for(int i=0; i<EntityTags.length; i++)
			hashtags.add(EntityTags[0].getText().toLowerCase());
		return hashtags;
	}
	
	public Set<String> getAllURLs(URLEntity[] EntityURLs) {
		Set<String> urls = new HashSet<String>();
		if (EntityURLs == null)
			return urls;
		for(int i=0; i<EntityURLs.length; i++)
			urls.add(EntityURLs[0].getURL());
		return urls;
	}
	
	Tweet(User user, HashtagEntity[] hashtags, URLEntity[] urls,
				String text, String lang, GeoLocation loc, Date date) {
		this.UserName = user.getScreenName();
		this.HashTags = getAllHashTags(hashtags);
		this.Text = text;
		this.Lang = lang;
		this.createDate = date;
		this.Loc = new Location(loc.getLongitude(), loc.getLatitude());
	}
	
	public Set<String> getHashTags() {
		return HashTags;
	}
	
	public String getLang() {
		return Lang;
	}
	
	public Set<String> getLinks() {
		return Links;
	}
	
	public Location getLoc() {
		return Loc;
	}
	
	public String getText() {
		return Text;
	}
	
	public String getUserName() {
		return UserName;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
}
