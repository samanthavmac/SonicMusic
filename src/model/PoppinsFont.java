package model;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

//Custom Font Object
//FCP - Sonic Music
//Samantha Mac
//June 10, 2024

// Imports the Poppins font for use across the entire project
public class PoppinsFont {
	private static Font poppinsRegular;
	private static Font poppinsRegularBold;
	private static Font poppinsSubtitle;
	private static Font poppinsSemiBold;
	private static Font poppinsBold;
	private static Font poppinsTitle;

	public PoppinsFont() {
		// Initialize regular font
		File poppinsRegular_file = new File("fonts/Poppins/Poppins-Regular.ttf");
		Font regular;

		try {
			regular = Font.createFont(Font.TRUETYPE_FONT, poppinsRegular_file);
			setPoppinsRegular(regular.deriveFont(16f)); // Set font size
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialize regular bold font
		File poppinsRegularBold_file = new File("fonts/Poppins/Poppins-Bold.ttf");
		Font regularBold;

		try {
			regularBold = Font.createFont(Font.TRUETYPE_FONT, poppinsRegularBold_file);
			setPoppinsRegularBold(regularBold.deriveFont(16f)); // Set font size
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Initialize regular font
		File poppinsSubtitle_file = new File("fonts/Poppins/Poppins-Regular.ttf");
		Font subtitle;

		try {
			subtitle = Font.createFont(Font.TRUETYPE_FONT, poppinsSubtitle_file);
			setPoppinsSubtitle(subtitle.deriveFont(13f)); // Set font size
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialize bold font
		File poppinsSemiBold_file = new File("fonts/Poppins/Poppins-SemiBold.ttf");
		Font semibold;

		try {
			semibold = Font.createFont(Font.TRUETYPE_FONT, poppinsSemiBold_file);
			setPoppinsSemiBold(semibold.deriveFont(18f)); // Set font size
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialize bold font
		File poppinsBold_file = new File("fonts/Poppins/Poppins-Bold.ttf");
		Font bold;

		try {
			bold = Font.createFont(Font.TRUETYPE_FONT, poppinsBold_file);
			setPoppinsBold(bold.deriveFont(36f)); // Set font size
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialize title font
		File poppinsTitle_file = new File("fonts/Poppins/Poppins-SemiBold.ttf");
		Font title;

		try {
			title = Font.createFont(Font.TRUETYPE_FONT, poppinsTitle_file);
			setPoppinsTitle(title.deriveFont(36f)); // Set font size
			// SET COLOUR
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Setters and getters
	public static Font getPoppinsRegular() {
		return poppinsRegular;
	}

	public static void setPoppinsRegular(Font poppinsRegular) {
		PoppinsFont.poppinsRegular = poppinsRegular;
	}

	public static Font getPoppinsRegularBold() {
		return poppinsRegularBold;
	}

	public static void setPoppinsRegularBold(Font poppinsRegularBold) {
		PoppinsFont.poppinsRegularBold = poppinsRegularBold;
	}

	public static Font getPoppinsSubtitle() {
		return poppinsSubtitle;
	}

	public static void setPoppinsSubtitle(Font poppinsSubtitle) {
		PoppinsFont.poppinsSubtitle = poppinsSubtitle;
	}

	public static Font getPoppinsSemiBold() {
		return poppinsSemiBold;
	}

	public static void setPoppinsSemiBold(Font poppinsSemiBold) {
		PoppinsFont.poppinsSemiBold = poppinsSemiBold;
	}

	public static Font getPoppinsBold() {
		return poppinsBold;
	}

	public static void setPoppinsBold(Font poppinsBold) {
		PoppinsFont.poppinsBold = poppinsBold;
	}

	public static Font getPoppinsTitle() {
		return poppinsTitle;
	}

	public static void setPoppinsTitle(Font poppinsTitle) {
		PoppinsFont.poppinsTitle = poppinsTitle;
	}
}
