package ex1.chess;

import java.util.Random;

import genetic_base.Crossover;

public class ChessUniformCrossover implements Crossover<ChessChromo> {

	private final Random randomizer = new Random();

	public ChessUniformCrossover() {
	}

	@Override
	public ChromosomePair<ChessChromo> crossover(ChessChromo parent1, ChessChromo parent2) {
		if (parent1.numberOfQueens() != parent2.numberOfQueens()) {
			throw new RuntimeException("Chess chromosomes must have the same number of queens.");
		}

		int childLen = parent1.numberOfQueens();

		byte[] child1 = new byte[childLen];
		byte[] child2 = new byte[childLen];
		
		for (int i = 0; i < childLen; ++i) {
			byte queen1 = parent1.getQueenPositions()[i];
			byte queen2 = parent2.getQueenPositions()[i];
			
			// switch values in 0.5 probability
			if (randomizer.nextBoolean()) {
				byte temp = queen1;
				queen1 = queen2;
				queen2 = temp;
			}
			
			child1[i] = queen1;
			child2[i] = queen2;
		}

		return new ChromosomePair<ChessChromo>(new ChessChromo(child1), new ChessChromo(child2));
	}

}
