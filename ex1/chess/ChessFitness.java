package ex1.chess;

import genetic_base.FitnessMeter;

public class ChessFitness implements FitnessMeter<ChessChromo> {

	@Override
	public double fitness(ChessChromo chromo) {
		// number of collisions might be 0
		return 100.0 / (chromo.numberOfCollisions() + 1);
	}

}
