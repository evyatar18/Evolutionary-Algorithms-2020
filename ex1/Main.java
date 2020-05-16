package ex1;

import java.time.Duration;
import java.time.Instant;

import ex1.chess.ChessExperiment;
import ex1.chess.RandomQueenGuesser;
import ex1.textguesser.Text;
import ex1.textguesser.TextExperiment;

public class Main {
	
	private static final String text = "to be or not to be that is the question."
			+ " whether tis nobler in the mind to suffer."
			+ " the slings and arrows of outrageous fortune."
			+ " or to take arms against a sea of troubles and by opposing end them."
			+ " to die to sleep. no more."
			+ " and by a sleep to say we end."
			+ " the heartache and the thousand natural shocks.";
	
	public static void main(String[] args) {
		final int numberOfQueens = 8;
		
		runAndMeasure("chess evolution", () -> ChessExperiment.run(numberOfQueens, false));
		printSpaces();
		runAndMeasure("chess brute force", () -> RandomQueenGuesser.guess(numberOfQueens));
		printSpaces();
		
		runAndMeasure("text evolution", () -> TextExperiment.run(new Text(text), false));
	}
	
	private static void printSpaces() {
		for (int i = 0; i < 5; i++) {
			System.out.println("==================");
		}
	}
	
	private static void runAndMeasure(String name, Runnable r) {
		Instant start = Instant.now();
		r.run();
		Instant end = Instant.now();
		
		Duration d = Duration.between(start, end);
		
		System.out.println();
		System.out.println();
		System.out.printf(">>>>>>> %s took: %dms\n", name, d.toMillis());
	}
}
