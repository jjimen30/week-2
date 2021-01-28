package javaReview;

import java.util.Arrays;

import file.FileInput;
import file.FileOutput;

public class JavaReview2 {

	private static final String NEWLINE = "\n";
	private static String path = "/Users/jorgejimenez/eclipse-workspace/java review 2/src/resources/SLCDecember2020Temperatures.csv";
	private static String outputPath = "/Users/jorgejimenez/eclipse-workspace/java review 2/src/resources/TemperaturesReport.txt";

	private static String[][] data;

	private static int LINE = 62;
	private static int COLUMN_DAY = 4;
	private static int COLUMN_HIGH = 5;
	private static int COLUMN_LOW = 4;
	private static int COLUMN_VARIANCE = 8;

	private static FileOutput out;
	private static FileInput in;
	private static StringBuilder sb;
	private static String line;

	public static void main(String[] arg) {

		out = new FileOutput(outputPath);
		in = new FileInput(path);

		data = in.getCSVData();

		// Create the reports.	
		reportOne();
		reportTow();

		System.out.println(sb.toString());
		out.write(sb.toString());
		in.close();
		out.close();
	}

	//  Lists all days and temperatures in a tabular format.
	private static void reportOne() {
		sb = new StringBuilder();
		String title = "December 2020: Temperatures in Utah \n";
		int highestIndex = 0;
		int lowestIndex = 0;
		line = createLine(LINE, '-');

		float sumHigh = 0;
		float sumLow = 0;

		// Add the title.
		sb.append(line);
		sb.append(title);
		sb.append(line);

		// Create the header for the table. Print out and add to file.
		String header = createRow("Day  High Low  Variance".split("\s+"), COLUMN_DAY, COLUMN_HIGH, COLUMN_LOW,
				COLUMN_VARIANCE);

		sb.append(header);
		sb.append(line);

		int low = 2;
		int high = 1;
		// Create each row entry. 
		for (int i = 0; i < data.length; i++) {

			// Check for the highest and lowest.
			if (Integer.parseInt(data[i][high]) > Integer.parseInt(data[highestIndex][high]))
				highestIndex = i;

			if (Integer.parseInt(data[i][low]) < Integer.parseInt(data[lowestIndex][low]))
				lowestIndex = i;

			// Add the variance to the array.
			data[i] = Arrays.copyOf(data[i], data[i].length + 1);
			data[i][data[i].length - 1] = variance(Integer.parseInt(data[i][high]), Integer.parseInt(data[i][low]));

			// Get the row and add it to the StringBuilder.
			String rowString = createRow(data[i], COLUMN_DAY, COLUMN_HIGH, COLUMN_LOW, COLUMN_VARIANCE);
			sb.append(rowString);

			sumHigh += Float.parseFloat(data[i][high]);
			sumLow += Float.parseFloat(data[i][low]);

		}

		sb.append(line);

		// The final report at the end.
		String highest = String.format("December Highest Temperature: 12/%s: %s Average Hi: %.1f",
				data[highestIndex][0], data[highestIndex][1], sumHigh / data.length);

		String lowest = String.format("December Lowest Temperature: 12/%s: %s Average Lo: %.1f", data[lowestIndex][0],
				data[lowestIndex][2], sumLow / data.length);

		sb.append(highest + NEWLINE);
		sb.append(NEWLINE);
		sb.append(lowest + NEWLINE);

	}

	private static void reportTow() {

		sb.append(line);
		sb.append("Graph" + NEWLINE);

		sb.append(line);
		sb.append("      1   5    10   15   20   25   30   35   40   45   50\n");
		sb.append("      |   |    |    |    |    |    |    |    |    |    |\n");
		sb.append(line);

		int low = 2;
		int high = 1;
		int date = 0;
		// Iterate through the data to the create the second report.
		for (int i = 0; i < data.length; i++) {

			sb.append(String.format("%-3sHI %s\n", data[i][date], createLine(Integer.parseInt(data[i][high]), '+')));
			sb.append(String.format("%-3sLO %s\n", "", createLine(Integer.parseInt(data[i][low]), '-')));
			//			sb.append("   LO " + createLine(Integer.parseInt(data[i][high]), '+') + NEWLINE);
		}

		sb.append(line);
		sb.append("      |   |    |    |    |    |    |    |    |    |    |\n");
		sb.append("      1   5    10   15   20   25   30   35   40   45   50\n");
		sb.append(line + NEWLINE);
	}

	// --------------------------------------------
	// SOME HELPER METHODS
	// --------------------------------------------

	// Calculates the variance between two numbers.
	private static String variance(int a, int b) {
		if ((a - b) < 0)
			return Integer.toString(b - a);

		return Integer.toString(a - b);
	}

	// Takes a String array and and builds a row for the table. 
	private static void printRow(String[] elements, int... widths) {

		System.out.print(createRow(elements, widths));
	}

	// Creates a row based on the string array passed in and with the corresponding column widths.
	private static String createRow(String[] elements, int... widths) {

		StringBuilder sb = new StringBuilder();
		int totalWidth = 0;

		for (int i = 0; i < elements.length; i++) {
			totalWidth += widths[i];
			sb.append(String.format("%-" + widths[i] + "s ", elements[i]));
		}

		sb.append(NEWLINE);

		return sb.toString();
	}

	// Prints the amount of dashes passed in.
	private static void printLine(int size, char c) {
		System.out.print(createLine(size, c));
	}

	// Creates a line of a size and characters c.
	private static String createLine(int size, char c) {
		char[] l = new char[size];
		Arrays.fill(l, c);
		StringBuilder sb = new StringBuilder();
		sb.append(l);
		sb.append(NEWLINE);
		return sb.toString();
	}
}
