package model;

//Song Object
//FCP - Sonic Music
//Samantha Mac
//June 7, 2024

// This creates a Song object with all of its identifying information
public class Song {
	// Fields
	private String name;
	private String artist;
	private int duration;
	private int popularity;
	private String trackID;
	private String albumCoverURL;

	// Constructor
	public Song(String name, String artist, int duration, int popularity, String trackID, String albumCoverURL) {
		super();
		this.name = name;
		this.artist = artist;
		this.duration = duration;
		this.popularity = popularity;
		this.trackID = trackID;
		this.albumCoverURL = albumCoverURL;
	}

	// Setters and getters
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public String getTrackID() {
		return trackID;
	}

	public void setTrackID(String trackID) {
		this.trackID = trackID;
	}

	public String getAlbumCoverURL() {
		return albumCoverURL;
	}

	public void setAlbumCoverURL(String albumCoverURL) {
		this.albumCoverURL = albumCoverURL;
	}

	@Override
	public String toString() {
		return "Song [name=" + name + ", artist=" + artist + ", duration=" + duration + ", popularity=" + popularity
				+ ", trackID=" + trackID + ", albumCoverURL=" + albumCoverURL + "]";
	}
}
