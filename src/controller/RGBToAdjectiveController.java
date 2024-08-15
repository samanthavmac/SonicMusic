package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import model.RGBColour;

//RGBToNameController
//FCP - Sonic Music
//Samantha Mac
//June 6, 2024

// This class uses the DataMuse API to find words associated with words
public class RGBToAdjectiveController {
	// Fields
	public static HashMap<RGBColour, ArrayList<String>> dominantColourAdjectives; // Store color names and their adjectives

	// Constructor
	public RGBToAdjectiveController(ArrayList<RGBColour> dominantColours) {
		// Initialize Hashmap
		dominantColourAdjectives = new HashMap<>();

		// Iterate through dominant colours
		for (RGBColour rgbValue : dominantColours) {
			// Convert RGB to colour name
			String dominantColourName = convertRGBToName(rgbValue.getRed(), rgbValue.getGreen(), rgbValue.getBlue());

			// Initialize array list of strings
			String[] words = dominantColourName.split(" ");

			// Store current adjectives associated with colour
			ArrayList<String> tempAdjectives = new ArrayList<>();

			// Iterate through
			for (String currWord : words) {
				tempAdjectives.addAll(convertNameToAdjectives(currWord));

				// Store the color name and its adjectives in the HashMap
				dominantColourAdjectives.put(rgbValue, tempAdjectives);
			}
		}

	}

	// User ColorAPI to find closest HEX/name to RGB value
	private String convertRGBToName(int r, int g, int b) {
		// Create API engpoint
		String urlString = String.format("https://www.thecolorapi.com/id?rgb=%d,%d,%d", r, g, b);

		try {
			URL url = new URL(urlString); // Convert string to URL

			// Resource: Making an HTTP GET request in Java
			// https://www.digitalocean.com/community/tutorials/java-httpurlconnection-example-java-http-request-get-post

			// Open HTTP connection
			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			connect.setRequestMethod("GET"); // Make GET request (there are various HTTP requests you can make)
			int responseCode = connect.getResponseCode(); // Response code indicates how successful the API call was

			// https://www.baeldung.com/java-http-request
			if (responseCode == 200) { // If successful
				// Create a BufferedReader to read the response
				BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				// The HTTP request will return response string
				// This must be converted into usable infromation
				StringBuilder response = new StringBuilder(); // Build response string

				String inputLine;
				while ((inputLine = reader.readLine()) != null) {
					response.append(inputLine); // Add HTTP response to response string
				}
				// Close the BufferedReader
				reader.close();

				// Convert the response string to a JSON object
				JSONObject root = new JSONObject(response.toString());

				// TheColorAPI displays JSONObjects with various "fields"
				// extract the particular field of "Colour Name"
				return root.getJSONObject("name").getString("value");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null; // Return nothing if conversion is not possible
	}

	// Use the DataMuse API to retrieve adjectives from the colour name
	private ArrayList<String> convertNameToAdjectives(String word) {
		ArrayList<String> adjectives = new ArrayList<>();
		
		// Create endpoint to retrieve information from DataMuse
		String urlString = "https://api.datamuse.com/words?rel_jjb=" + word;

		try {
			URL url = new URL(urlString); // Convert string to URL

			// Resource: Making an HTTP GET request in Java
			// https://www.digitalocean.com/community/tutorials/java-httpurlconnection-example-java-http-request-get-post

			// Open HTTP connection
			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			connect.setRequestMethod("GET"); // Make GET request (there are various HTTP requests you can make)
			int responseCode = connect.getResponseCode(); // Response code indicates how successful the API call was

			// https://www.baeldung.com/java-http-request
			if (responseCode == 200) { // If successful
				// Create a BufferedReader to read the response
				BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));

				// The HTTP request will return response string
				// This must be converted into usable infromation
				StringBuilder response = new StringBuilder(); // Build response string

				String inputLine;
				while ((inputLine = reader.readLine()) != null) {
					response.append(inputLine); // Add HTTP response to response string
				}
				// Close the BufferedReader
				reader.close();

				// Convert the response string to a JSON array
				// For each query, multiple adjectives are returned
				// Store all adjectives in an array
				JSONArray jsonArray = new JSONArray(response.toString());

				// Iterate through array of adjectives
				for (int i = 0; i < jsonArray.length(); i++) {
					// Get object within array
					JSONObject adjective = jsonArray.getJSONObject(i);
					// The API scores how descriptive the adjective is
					int score = adjective.getInt("score");
					// Only select the best adjectives
					if (score > 1000) {
						// Add adjective to final list of words
						adjectives.add(adjective.getString("word"));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return adjectives;
	}
}
