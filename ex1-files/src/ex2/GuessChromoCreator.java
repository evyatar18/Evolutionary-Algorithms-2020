package ex2;

import java.util.Random;

import genetic_base.experiment.ChromoCreator;

public class GuessChromoCreator implements ChromoCreator<GuessChromo> {

	private final Random rand = new Random();
	
	@Override
	public GuessChromo createChromo() {
		return new GuessChromo(0.1);
	}

}
