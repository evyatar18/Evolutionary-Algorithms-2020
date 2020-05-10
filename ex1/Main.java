package ex1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.IntStream;

import ex1.chess.ChessChromo;
import ex1.chess.ChessChromoCreator;
import ex1.chess.ChessFitness;
import ex1.chess.ChessMutator;
import ex1.chess.ChessUniformCrossover;
import ex1.textguesser.Text;
import ex1.textguesser.TextChromosome;
import ex1.textguesser.TextChromosomeCreator;
import ex1.textguesser.TextDoublePointCrossover;
import ex1.textguesser.TextFitness;
import ex1.textguesser.TextMutationChanger2;
import ex1.textguesser.TextMutator;
import genetic_base.Elitism;
import genetic_base.Experiment;
import genetic_base.GeneticUtils;
import genetic_base.Nature;
import genetic_base.Population;
import genetic_base.RankSelection;
import genetic_base.listeners.FitnessReporter;
import global.Variable;

public class Main {

	private static final String text2 = "The New Testament is a collection of Christian texts originally written in "
			+ "the Koine Greek language, at different times by various different authors. While the "
			+ "Old Testament canon varies somewhat between different Christian denominations, "
			+ "the 27-book canon of the New Testament has been almost universally recognized "
			+ "within Christianity since at least Late Antiquity. Thus, in almost "
			+ "all Christian traditions today, the New Testament consists of 27 books:";
	
	private static final String text = "to be or not to be that is the question."
			+ " whether tis nobler in the mind to suffer."
			+ " the slings and arrows of outrageous fortune."
			+ " or to take arms against a sea of troubles and by opposing end them."
			+ " to die to sleep. no more."
			+ " and by a sleep to say we end."
			+ " the heartache and the thousand natural shocks.";
	
	public static void main(String[] args) {
		runChessExperiment(8);
//		runTextExperiment(text);
//		System.out.println(calculateExpectedNumberOfIterations(new Text(text)));
	}
	
	private static void runChessExperiment(int numberOfQueens) {
		Experiment<ChessChromo> chessExperiment = new Experiment<ChessChromo>(
				new ChessChromoCreator(numberOfQueens), 
				new ChessFitness(),
				new ChessMutator(2),
				new ChessUniformCrossover(),
				new RankSelection<ChessChromo>(RankSelection.HARMONIC_FITNESS));
		
		Nature<ChessChromo> nature = new Nature<>(chessExperiment, (pop) -> 
				pop.best().numberOfCollisions() == 0);
		
		nature.addListener(Main::printChessInfo);
		
		try (FileOutputStream fos = new FileOutputStream("chess_experiment.txt")) {
			nature.addListener(new FitnessReporter<ChessChromo>(fos));
			nature.run(50, new Variable<>(0.1), new Variable<>(0.75));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int numItersChess = 0;
	
	private static void printChessInfo(Population<ChessChromo> pop, boolean ended) {
		if (numItersChess % 10 == 0 || ended) {
			ChessChromo chromo = pop.best();
			System.out.println("iteration: " + numItersChess);
			System.out.println("chromo: " + chromo);
			System.out.println("number of queens: " + chromo.numberOfQueens());
			System.out.println("number of collisons: " + chromo.numberOfCollisions());
			System.out.println("fitness: " + pop.getFitness(chromo));
		}
		numItersChess++;
	}
	
	private static int calculateExpectedNumberOfIterations(Text text) {
		int diffChars = text.numberOfDifferentCharacters();
		int chars = text.length();
		int combos = chars * diffChars;
		
		return (int) IntStream.range(0, text.length())			
			.mapToDouble(i -> (combos / (chars - (double) i)))
			.sum();
	}

	private static void runTextExperiment(String text) {
		Text target = new Text(text);
		TextFitness fitness = new TextFitness(target);
		
		// in average, do 1 character change per mutation
		Variable<Double> characterMutationRate = new Variable<Double>(
				1.2 / text.length());
		
		Experiment<TextChromosome> textExperiment = new Experiment<TextChromosome>(
				new TextChromosomeCreator(target), 
				fitness,
				new TextMutator(target, characterMutationRate),
				new TextDoublePointCrossover(),
				new RankSelection<TextChromosome>(RankSelection.EXPONENTIAL_FITNESS));
		
		Nature<TextChromosome> nature = new Nature<>(textExperiment,
				(pop) -> pop.best().getText().contentEquals(text));
		
		nature.addListener(Main::printTextInfo);
		
		try (FileOutputStream fos = new FileOutputStream("text_experiment.txt")) {
			nature.addListener(new FitnessReporter<TextChromosome>(fos));
			
			int N = 35;
			Variable<Double> mutationRate = new Variable<Double>(1.0);
			Variable<Double> crossoverRate = new Variable<>(0.75);
			
			Population<TextChromosome> lastPop = nature.run(N,
					mutationRate,
					crossoverRate);
			
			System.out.println(target.numberOfDifferentCharacters());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int generationNumber = 0;

	private static void printTextInfo(Population<TextChromosome> pop, boolean lastPop) {
		if (generationNumber % 100 == 0 || lastPop) {
			System.out.println("Current generation: " + generationNumber);
			TextChromosome chromo = pop.best();
			System.out.println("best chromo: " + chromo.getText());
			System.out.println("best fitness: " + pop.getFitness(chromo));
			System.out.println("avg fitness: " + GeneticUtils.averageFitness(pop));
			
			System.out.println("gen*pop size = " + (generationNumber * pop.size()));
		}
		
		generationNumber++;
	}
}
