package ex2;

import java.util.Random;

import genetic_base.experiment.Mutator;

public class GuessMutator implements Mutator<GuessChromo> {

	private double stdDev;
	private Random rand = new Random();
	
	public GuessMutator(double stdDev) {
		this.stdDev = stdDev;
	}
	
	@Override
	public GuessChromo mutate(GuessChromo chromo) {
		double chance = chromo.positiveChance + stdDev * rand.nextGaussian();
		
		chance = Math.max(0.001, chance);
		chance = Math.min(chance, 0.999);
		
		return new GuessChromo(chance);
	}

}
