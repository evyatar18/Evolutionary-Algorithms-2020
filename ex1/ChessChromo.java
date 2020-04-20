package ex1;

import genetic_base.Chromosome;

public class ChessChromo implements Chromosome {

	private final byte[] queenPositions;

	public ChessChromo(byte[] queenPositions) {
		// check legality
		for (byte queenPos : queenPositions) {
			if (queenPos < 0 || queenPos >= queenPositions.length) {
				throw new RuntimeException("Illegal queen position " + queenPos + ".");
			}
		}

		this.queenPositions = queenPositions;
	}

	public byte[] getQueenPositions() {
		return queenPositions;
	}

	public int numberOfQueens() {
		return queenPositions.length;
	}

	private int numberOfCollisions = -1;

	private static class BoardPoint {
		final int row;
		final int col;

		private BoardPoint(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}

	/**
	 * Get the number of collisions between queens.
	 * @return number of collisions
	 */
	public int numberOfCollisions() {
		if (numberOfCollisions >= 0) {
			return numberOfCollisions;
		}

		int collisions = 0;

		for (int i = 0; i < queenPositions.length; ++i) {
			BoardPoint firstQueen = new BoardPoint(i, queenPositions[i]);

			// test if from the next queen there are collisions
			for (int j = i + 1; j < queenPositions.length; ++j) {
				BoardPoint secondQueen = new BoardPoint(j, queenPositions[j]);

				collisions += numberOfCollisions(firstQueen, secondQueen);
			}
		}

		return (numberOfCollisions = collisions);
	}

	private int numberOfCollisions(BoardPoint firstQueen, BoardPoint secondQueen) {
		// check for obvious collisions (same row, same col)
		if (firstQueen.row == secondQueen.row || firstQueen.col == secondQueen.col) {
			return 1;
		}
		
		/* check for diagonal collisions */
		int diffX = firstQueen.col - secondQueen.col;
		int diffY = firstQueen.row - secondQueen.row;
		
		if (diffX == diffY || diffX == -diffY) {
			return 1;
		}
		
		return 0;
	}
}
