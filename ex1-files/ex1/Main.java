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
		final int times = 100;
		
		long m1 = runAndMeasure("chess evolution", () -> ChessExperiment.run(numberOfQueens, false), times);
		printSpaces();
		long m2 =runAndMeasure("chess brute force", () -> RandomQueenGuesser.guess(numberOfQueens), times);
		printSpaces();
		
		long m3 = runAndMeasure("text evolution", () -> TextExperiment.run(new Text(text), false), times);
		
		System.out.println("chess evolution: " + m1);
		System.out.println("chess brute force: " + m2);
		System.out.println("text evolution: " + m3);
	}
	
	private static void printSpaces() {
		for (int i = 0; i < 5; i++) {
			System.out.println("==================");
		}
	}
	
	private static long runAndMeasure(String name, Runnable r, int times) {
		Instant start = Instant.now();
		for (int i = 0; i < times; i++) {
			r.run();
		}
		Instant end = Instant.now();
		
		Duration d = Duration.between(start, end);
		
		long avgMillis = d.toMillis()/times;
		
		System.out.println();
		System.out.println();
		System.out.printf(">>>>>>> %s took: %dms\n", name, avgMillis);
		
		return avgMillis;
	}
}
