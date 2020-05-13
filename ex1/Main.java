package ex1;

import ex1.chess.ChessChromo;
import ex1.chess.ChessExperiment;
import ex1.chess.RandomQueenGuesser;

public class Main {
	
	private static final String text = "to be or not to be that is the question."
			+ " whether tis nobler in the mind to suffer."
			+ " the slings and arrows of outrageous fortune."
			+ " or to take arms against a sea of troubles and by opposing end them."
			+ " to die to sleep. no more."
			+ " and by a sleep to say we end."
			+ " the heartache and the thousand natural shocks.";
	
	public static void main(String[] args) {
		
//		TextExperiment.run(new Text(text));
		
		ChessExperiment.run(12);
		ChessChromo chessChromo = RandomQueenGuesser.guess(12);
		System.out.println(chessChromo);
		
		
	}
}
