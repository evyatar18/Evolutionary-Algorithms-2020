package ex1.chess;

import java.util.Random;

import genetic_base.experiment.ChromoCreator;

public class ChessChromoCreator implements ChromoCreator<ChessChromo> {

	private final int numberOfQueens;

	private final Random random = new Random();

	public ChessChromoCreator(int numberOfQueens) {
		this.numberOfQueens = numberOfQueens;
	}

	@Override
	public ChessChromo createChromo() {
		byte[] queens = new byte[numberOfQueens];

		for (int i = 0; i < queens.length; ++i) {
			queens[i] = (byte) random.nextInt(numberOfQueens);
		}

		return new ChessChromo(queens);
	}

}
