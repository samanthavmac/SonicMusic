package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import view.DashboardFrame;
import model.RGBColour;

//ImageController
//FCP - Sonic Music
//Samantha Mac
//June 1, 2024

public class ImageToRGBController {
	// Fields
	// Controllers
	public static RGBToAdjectiveController adjectiveController;
	
	// Image properties
	private BufferedImage uploadedImage; // Image to analyze
	private int imageWidth;
	private int imageHeight;
	private Color[][] imageRGBRepresentation;
	
	// K-means properties
	private int kCount = 5; // Number of clusters
    // NOTE: Centroids are the "central"/dominant colour
    private ArrayList<RGBColour> centroids; // Track centroids
    private int[][] clusterProperties = new int[4][kCount];
    
    // Image analysis
    private ArrayList<RGBColour> dominantColours; // Final list of dominant colours
    private int[][] pixelClusters; // Track which cluster each pixel belongs to

	// Constructor
	public ImageToRGBController() {
		// Get image from Generator Panel
		uploadedImage = DashboardFrame.generatorPanel.getUploadedImage();
		
		// Call on methods that compose the entire image analysis algorithm
		getDimensions();
		initializeCentroids();
		stabilizeCluster(); // Repeatedly update centroids until a dominant colour is identified
		getAdjectives(dominantColours); // Convert colours to adjectives
	}
	
	private void getDimensions() {
		// Get width and length from current image
        imageHeight = uploadedImage.getHeight();   
		imageWidth = uploadedImage.getWidth();
        pixelClusters = new int[imageHeight][imageWidth]; // Initialize pixelClusters array
	}
		
	// Make centroids random colours
	private void initializeCentroids() {
        centroids = new ArrayList<>(); // Initialize array

        for (int i = 0; i < kCount; i++) {
        	// Generate random centers for each cluster
            int red = (int) (Math.random() * 256);
            int green = (int) (Math.random() * 256);
            int blue = (int) (Math.random() * 256);
            
            centroids.add(new RGBColour(red, green, blue)); // Add to array list
        }
    }
	
	// Iterate through entire image dimensions and reevaluate clusters
	private void scanImagePixels() {
        // Create an RGB representation of the image
		imageRGBRepresentation = new Color[imageHeight][imageWidth];
		
        // Iterate through entire photo dimensions
        for (int row = 0; row < imageHeight; row++) {
            for (int col = 0; col < imageWidth; col++) {
            	// Get particular RGB value of this specific pixel
                Color pixelColour = new Color(uploadedImage.getRGB(col, row)); // Part of buffered image class
                
                // Update RGB representation of the image
                imageRGBRepresentation[row][col] = pixelColour;
                
                // Match particular pixel to its closest centroid
                RGBColour matchingCentroid = matchPixelToCentroid(pixelColour);
                // Track which centroid each pixel is a part of
                pixelClusters[row][col] = centroids.indexOf(matchingCentroid);

            }
        }
	}
	
	// Group each pixel to a particular centroid
	private RGBColour matchPixelToCentroid(Color pixelColour) {
		double bestDistance = -1; // Track which centroid each pixel is closest to
        RGBColour matchingCentroid = null; // The centroid the pixel is closest to

		// Iterate through all centroids
        for (RGBColour centroid : centroids) {
        	// Calculate distance between pixel and each centroid
            double currDistance = calculateRGBDistance(pixelColour, centroid.toAWTColor());
            if (currDistance < bestDistance || bestDistance == -1) {
            	bestDistance = currDistance; // If this is the best distance
                matchingCentroid = centroid; // Update the centroid it matches
            }
        }
        return matchingCentroid;
	}
	
	// Once all pixels have been group, find the average/centre of its respective centroid
	private void updateCentroidCentre() {
		// Create an array that tracks the RGB values and size of each centroid
		// In centroidProperties, there are kcount columns -- each representing a cluster
		// Row 1: Red value
		// Row 2: Green value
		// Row 3: Blue value
		// Row 4: Cluster size
		
		// Initialize centroid properties array
		for (int row = 0; row < clusterProperties.length; row++) {
			for (int col = 0; col < clusterProperties[row].length; col++) {
				clusterProperties[row][col] = 0;
			}
		}
		
		// Finalize properties of each cluster
		// Iterate through entire photo dimensions
        for (int row = 0; row < imageHeight; row++) {
            for (int col = 0; col < imageWidth; col++) {
            	int clusterIndex = pixelClusters[row][col]; // Get which cluster the pixel correlates to
            	// Update respective properties
            	clusterProperties[0][clusterIndex] += imageRGBRepresentation[row][col].getRed(); // Update the image's overall red sum
            	clusterProperties[1][clusterIndex] += imageRGBRepresentation[row][col].getGreen(); // Update green sum
            	clusterProperties[2][clusterIndex] += imageRGBRepresentation[row][col].getBlue(); // Update blue sum
            	clusterProperties[3][clusterIndex]++; // Update occurrence of that particular cluster
            }
        }
        
		// Find the average RGB value/centroid of each cluster
        // Iterate through each cluster
		for (int currCluster = 0; currCluster < kCount; currCluster++) {
			// Check if cluster has any pixels associated with it
			if (clusterProperties[3][currCluster] > 0) {
				// Calculate average RGB values
				int redAverage = clusterProperties[0][currCluster] / clusterProperties[3][currCluster];
                int greenAverage = clusterProperties[1][currCluster] / clusterProperties[3][currCluster];
                int blueAverage = clusterProperties[2][currCluster] / clusterProperties[3][currCluster];
                
                // Update centroid value for each cluster
                centroids.get(currCluster).setRed(redAverage);
                centroids.get(currCluster).setGreen(greenAverage);
                centroids.get(currCluster).setBlue(blueAverage);
			}
		}
	}
	
	// Continuously update the centroids of each cluster as you add pixels
	private void stabilizeCluster() {
		boolean stillStabilizing = true; // Are you still updating the centroid value?
		// Do not let the image stabilize forever
		int maxIterations = 1000;
		int iteration = 0;
		 
		while (stillStabilizing && iteration < maxIterations) {
			// Iterate through photo dimensions
            scanImagePixels();
            
            ArrayList<RGBColour> oldCentroids = new ArrayList<>(); // Temp array
            
            // Remember what the current centroids are
            for (RGBColour centroid : centroids) {
                oldCentroids.add(new RGBColour(centroid.getRed(), centroid.getGreen(), centroid.getBlue()));
            }
            
            // Update current centroid
            updateCentroidCentre();
            
            // Compare old and new centroids to see if they still match
            stillStabilizing = !centroids.equals(oldCentroids);
            iteration++;
		}
		
        // Once stabilized, identify dominant colour (cluster's centroid)
        getFinalCentroids();
	}
	
	// Centroids of finalized cluster become dominant colour
    private void getFinalCentroids() {
        dominantColours = new ArrayList<>(centroids);
    }
    
    // Convert colours to adjectives
    private void getAdjectives(ArrayList<RGBColour> dominantColours) {
    	// Create RGBToNameController
    	adjectiveController = new RGBToAdjectiveController(dominantColours);
    }
	
	// Calculates distance between two RGB values
	private double calculateRGBDistance(Color colorA, Color colorB) {	
		// Calculate difference between each colour
        double redDistance = colorA.getRed() - colorB.getRed();
        double greenDistance = colorA.getGreen() - colorB.getGreen();
        double blueDistance = colorA.getBlue() - colorB.getBlue();
        
        // Find overall distance
        return Math.sqrt(Math.pow(redDistance, 2) + Math.pow(greenDistance, 2) + Math.pow(blueDistance, 2));
	 }
}
