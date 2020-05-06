package ex1;

import ex1.chess.ChessChromo;
import ex1.chess.ChessChromoCreator;
import ex1.chess.ChessFitness;
import ex1.chess.ChessMutator;
import ex1.chess.ChessUniformCrossover;
import ex1.textguesser.TextChromosome;
import ex1.textguesser.TextChromosomeCreator;
import ex1.textguesser.TextFitness;
import ex1.textguesser.TextMutator;
import ex1.textguesser.TextUniformCrossover;
import genetic_base.Experiment;
import genetic_base.Nature;
import genetic_base.RouletteWheelSelection;

public class Main {

	public static void main(String[] args) {
//		runChessExperiment();
		runTextExperiment("The New Testament is a collection of Christian texts originally written in "
				+ "the Koine Greek language, at different times by various different authors. While the "
				+ "Old Testament canon varies somewhat between different Christian denominations, "
				+ "the 27-book canon of the New Testament has been almost universally recognized "
				+ "within Christianity since at least Late Antiquity. Thus, in almost "
				+ "all Christian traditions today, the New Testament consists of 27 books:");
	}
	
	final static ChessFitness chessFitness = new ChessFitness();
	
	private static void runChessExperiment() {
		Experiment<ChessChromo> chessExperiment = new Experiment<ChessChromo>(
				new ChessChromoCreator(10, 8), 
				new ChessFitness(),
				new ChessMutator(8),
				new ChessUniformCrossover(),
				new RouletteWheelSelection<ChessChromo>());
		
		Nature.run(chessExperiment, (gen) -> {
			System.out.println("current best:");
			printChessInfo(gen.best());
			
			return gen.best().numberOfCollisions() == 0;
		}, 0.01, 0.75); 
	}
	
	private static void printChessInfo(ChessChromo chromo) {
		System.out.println("chromo: " + chromo);
		System.out.println("number of queens: " + chromo.numberOfQueens());
		System.out.println("number of collisons: " + chromo.numberOfCollisions());
		System.out.println("fitness: " + chessFitness.fitness(chromo));
	}

	private static void runTextExperiment(String text) {
		Experiment<TextChromosome> textExperiment = new Experiment<TextChromosome>(
				new TextChromosomeCreator(5, text.length()), 
				new TextFitness(text),
				new TextMutator(10),
				new TextUniformCrossover(),
				new RouletteWheelSelection<TextChromosome>());
		
		Nature.run(textExperiment, (gen) -> {
			System.out.println("current best:");
			printTextInfo(gen.best(), gen.getFitness(gen.best()));
			
			return gen.best().getText().contentEquals(text);
		}, 0.1, 0.75); 

	}

	private static void printTextInfo(TextChromosome chromo, double fitness) {
		System.out.println("chromo: " + chromo.getText());
		System.out.println("fitness: " + fitness);
	}
}
