import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class highScore implements Comparable<highScore> {

	private String name;
	private int score = 0;

	public highScore(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public void setName(String aName) {
		name = aName;
	}

	public String getName() {
		return name;
	}

	public void updateScore(int aScore) {
		score += aScore;
	}

	public int getScore() {
		return score;
	}

	/**
	 * Overrides the compareTo method for arrayList to compare the int value of the
	 * objects contained inside and to rearrange the order of the array in order of
	 * object of highest int to lowest int
	 * 
	 * @param passes
	 *            the object type of the arraylist
	 * @return Returns the value of int for Collections to use with the sort method
	 */
	@Override
	public int compareTo(highScore compares) {
		int compareScore = ((highScore) compares).getScore();
		return compareScore - this.score;

	}

	/**
	 * Reads the high score list into a String
	 * 
	 * @return String containing the high score list
	 *
	 */
	public static String highScoreList() {
		// Reads the list into a string
		String line = "";
		String list = "1) ";
		try {
			FileReader fileReader = new FileReader("HighScores.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int i = 1;
			while ((line = bufferedReader.readLine()) != null) {
				i++;
				list = list + (line + "   " + bufferedReader.readLine());
				list = list + ("\n");
				list = list + i + ") ";
			}
			bufferedReader.close();
			fileReader.close();
		} catch (IOException e) {
		}
		return list.substring(0, list.length() - 3);
	}

	/**
	 * Checks if the user has entered a name, if the user has then it will load the
	 * entire list into an array list and check if the score of the current user is
	 * higher than any of the players on the high score list and replaces the entry
	 * if it is higher and then reorders the arraylist and write backs to the file
	 * in highest to lowest top 5 scores
	 * 
	 * @param name
	 *            of the user playing
	 * @param score
	 *            of the user playing
	 *
	 */
	public static void writeHighScore(String name, int score) {
		if (name.equals("")) {
			// do nothing
		} else {
			// Loads the entire list into an array list
			String fileName = "HighScores.txt";
			String line;
			String line2;
			ArrayList<highScore> highScoreList = new ArrayList<highScore>();
			try {
				FileReader fileReader = new FileReader("HighScores.txt");
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				while ((line = bufferedReader.readLine()) != null) {
					line2 = bufferedReader.readLine();
					int s = Integer.parseInt(line2);
					highScoreList.add(new highScore(line, s));
				}
				bufferedReader.close();
			} catch (IOException e) {
			}

			// Check if the new score is higher than any and if so, replace the name and
			// score
			for (int i = 0; i < highScoreList.size(); i++) {
				if (highScoreList.get(i).score < score) {
					highScoreList.get(i).score = score;
					highScoreList.get(i).name = name;
					break;
				}
			}

			// Reorders the arrayList, highest to lowest
			Collections.sort(highScoreList);

			// Write to file
			try {
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
				for (int i = 0; i < highScoreList.size(); i++) {
					System.out.println((highScoreList.get(i).getName().toString()));
					bufferedWriter.write((highScoreList.get(i).getName().toString()));
					bufferedWriter.newLine();
					System.out.println(Integer.toString(highScoreList.get(i).getScore()));
					bufferedWriter.write(Integer.toString(highScoreList.get(i).getScore()));
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
				bufferedWriter.close();
			} catch (IOException e) {
			}
		}
	}

}
