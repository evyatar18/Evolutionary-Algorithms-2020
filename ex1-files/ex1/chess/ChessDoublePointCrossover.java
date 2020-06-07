package ex1.chess;

import java.util.Random;

import genetic_base.experiment.Crossover;

public class ChessDoublePointCrossover implements Crossover<ChessChromo> {

	private final Random randomizer = new Random();

	@Override
	public ChromosomePair<ChessChromo> crossover(ChessChromo parent1, ChessChromo parent2) {
		if (parent1.numberOfQueens() != parent2.numberOfQueens()) {
			throw new RuntimeException("Chess chromosomes must have the same number of queens.");
		}
		
		byte[] p1 = parent1.getQueenPositions();
		byte[] p2 = parent2.getQueenPositions();

		int childLen = parent1.numberOfQueens();

		int firstPoint = randomizer.nextInt(childLen - 1);
		int secondPoint = firstPoint + randomizer.nextInt(childLen - firstPoint);
		
		byte[] child1 = new byte[childLen];
		byte[] child2 = new byte[childLen];
		
		for (int i = 0; i < childLen; ++i) {
			if (i < firstPoint || i > secondPoint) {
				child1[i] = p1[i];
				child2[i] = p2[i];
			} else {
				child1[i] = p2[i];
				child2[i] = p1[i];
			}
		}

		return new ChromosomePair<ChessChromo>(new ChessChromo(child1), new ChessChromo(child2));
	}

}
