package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Playlist Object
//FCP - Sonic Music
//Samantha Mac
//June 8, 2024

// This class outlines the properties of a unique playlist
public class Playlist {
	// Fields
	private BufferedImage playlistCover;
	private String name;
	private ArrayList<Song> songs;
	private int length;

	// Constructor
	public Playlist(String name, ArrayList<Song> songs, int length) {
		super();
		this.name = name;
		this.songs = songs;
		this.length = length;
	}

	// Setters and getters
	public BufferedImage getPlaylistCover() {
		return playlistCover;
	}

	public void setPlaylistCover(BufferedImage playlistCover) {
		this.playlistCover = playlistCover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Song> getSongs() {
		return songs;
	}

	public void setSongs(ArrayList<Song> songs) {
		this.songs = songs;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}	
}
