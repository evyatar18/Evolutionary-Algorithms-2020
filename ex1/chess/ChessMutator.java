package ex1.chess;

import java.util.Random;

import genetic_base.Mutator;

public class ChessMutator implements Mutator<ChessChromo> {
	
	private final Random randomizer;
	private final double stdDev;
	
	public ChessMutator(double stdDev) {
		this.randomizer = new Random();
		this.stdDev = stdDev;
	}
	
	@Override
	public ChessChromo mutate(ChessChromo chromo) {
		// copy the positions array (so we can modify it)
		byte[] pos = chromo.getQueenPositions();
		byte[] cpy = new byte[pos.length];
		
		System.arraycopy(pos, 0, cpy, 0, pos.length);
		pos = cpy;
		
		for (int i = 0; i < pos.length; ++i) {
			cpy[i] += (byte) (randomizer.nextGaussian() * stdDev);
			cpy[i] = (byte) (cpy[i] % pos.length);
			
			if (cpy[i] < 0) {
				cpy[i] += pos.length;
			}
		}
		
		return new ChessChromo(cpy);
	}

	
}
