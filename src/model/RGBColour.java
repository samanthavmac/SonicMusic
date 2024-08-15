package model;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

// This class creates a colour object
public class RGBColour {
	// Fields
	private int red;
	private int green;
	private int blue;

	// Constructor
	public RGBColour(int red, int green, int blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	// Setters and getters
	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	// Utility methods
	// Method to convert to java.awt.Color
    public java.awt.Color toAWTColor() {
        return new java.awt.Color(red, green, blue);
    }
    
	@Override
	public String toString() {
		return "RGBColour [red=" + red + ", green=" + green + ", blue=" + blue + "]";
	}
}
