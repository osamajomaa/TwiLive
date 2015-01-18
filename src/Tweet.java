import java.text.SimpleDateFormat;
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
	Double Longitude;
	Double Latitude;
	Location(Double lng, Double ltd) {
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
	
	Tweet(User user, Set<String> hashtags, Set<String> urls,
				String text, String lang, GeoLocation loc, Date date) {
		this.UserName = user.getScreenName();
		this.HashTags = hashtags;
		this.Links = urls;
		this.Text = text;
		this.Lang = lang;
		this.createDate = date;
		if (loc != null)
			this.Loc = new Location(loc.getLongitude(), loc.getLatitude());
		else
			this.Loc = new Location(null, null);
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
