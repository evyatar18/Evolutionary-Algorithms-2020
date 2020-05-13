package ex1.chess;

import java.util.Random;

import genetic_base.Mutator;
import global.Variable;

public class ChessMutator2 implements Mutator<ChessChromo> {
	
	private final Random randomizer;
	private final Variable<Double> queenMutationChance;
	
	public ChessMutator2(Variable<Double> queenMutationChance) {
		this.randomizer = new Random();
		this.queenMutationChance = queenMutationChance;
	}
	
	@Override
	public ChessChromo mutate(ChessChromo chromo) {
		// copy the positions array (so we can modify it)
		byte[] pos = chromo.getQueenPositions();
		byte[] cpy = new byte[pos.length];
		
		System.arraycopy(pos, 0, cpy, 0, pos.length);
		pos = cpy;
		
		for (int i = 0; i < pos.length; ++i) {
			if (randomizer.nextDouble() < queenMutationChance.get()) {
				pos[i] = (byte) randomizer.nextInt(chromo.numberOfQueens());
			}
		}
		
		return new ChessChromo(pos);
	}

	
}
