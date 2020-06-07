package ex1.chess;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import genetic_base.Nature;
import genetic_base.Population;
import genetic_base.RankSelection;
import genetic_base.experiment.Experiment;
import genetic_base.listeners.FitnessReporter;
import global.Variable;

public class ChessExperiment {

	public static void run(int numberOfQueens, boolean silent) {
		Experiment<ChessChromo> chessExperiment = new Experiment<ChessChromo>(
				new ChessChromoCreator(numberOfQueens), 
				new ChessFitness(),
				new ChessMutator2(new Variable<>(2.0 / numberOfQueens)),
				new ChessDoublePointCrossover(),
				new RankSelection<>(RankSelection.EXPONENTIAL_FITNESS)
			);
		
		Nature<ChessChromo> nature = new Nature<>(chessExperiment, (pop) -> 
				pop.best().numberOfCollisions() == 0);
		
		if (!silent) {
			nature.addListener(ChessExperiment::printChessInfo);
		}
		
		try (FileOutputStream fos = new FileOutputStream("chess_experiment.txt")) {
			nature.addListener(new FitnessReporter<ChessChromo>(fos));
			nature.run(80, new Variable<>(0.3), new Variable<>(0.9));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int numIters = 0;
	
	private static void printChessInfo(Population<ChessChromo> pop, boolean ended) {
		if (numIters % 1000 == 0 || ended) {
			ChessChromo chromo = pop.best();
			System.out.println("iteration: " + numIters);
			System.out.println("chromo: " + chromo);
			System.out.println("number of queens: " + chromo.numberOfQueens());
			System.out.println("number of collisons: " + chromo.numberOfCollisions());
			System.out.println("fitness: " + pop.getFitness(chromo));
			
			System.out.println("pop size * generations = " + (pop.size() * numIters));
		}
		numIters++;
	}

}
