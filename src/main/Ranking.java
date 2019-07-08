package main;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public final class Ranking {
	
	private static final Ranking INSTANCE = new Ranking();
	private ArrayList<Register> scores = new ArrayList<Register>();
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private Ranking() {
		try {
			File file = new File("highscores.ser");
			if (!file.exists()) {
				file.createNewFile();
			}
			input = new ObjectInputStream(new FileInputStream(file));
			while (true) {
				scores.add((Register) input.readObject());
			}
		} catch (EOFException endOfFile) {
			this.closeInput();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void writeScores() {
		try {
			output = new ObjectOutputStream(new FileOutputStream("highscores.ser"));
			
			for (Register r : scores) {
				output.writeObject(r);
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void closeInput() {
		if (input != null) {
			try {
				input.close();
			} catch(IOException io) {
				//
			}
		}
	}
	
	public static Ranking getInstance() {
		return INSTANCE;
	}
	
	public void addRegister(Register register) {
		scores.add(register);
		scores.sort(null);
		Collections.reverse(scores);
		if (scores.size() > 5) {
			scores = new ArrayList<Register>(scores.subList(0, 5));
		}
		this.writeScores();
	}

	public ArrayList<Register> getScores() {
		return scores;
	}
	
}
