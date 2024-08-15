package model;

//User Object
//FCP - Sonic Music
//Samantha Mac
//June 7, 2024

// This creates a User object to store the user's personal information
public class User {
	// Fields
	private String username;
	private String password;
	private String name;
	
	// Constructor
	public User(String username, String password, String name) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
	}

	// Setters and getters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", name=" + name + "]";
	}	
	
}
