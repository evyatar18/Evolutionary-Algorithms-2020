package ex2;

import genetic_base.experiment.Chromosome;

public class GuessChromo implements Chromosome {

	public final double positiveChance;
	
	public GuessChromo(double positiveChance) {
		this.positiveChance = positiveChance;
	}
}
