package ex2;

import genetic_base.experiment.Crossover;

public class GuessCrossover implements Crossover<GuessChromo> {

	@Override
	public ChromosomePair<GuessChromo> crossover(GuessChromo parent1, GuessChromo parent2) {
		return new ChromosomePair<>(parent1, parent2);
	}

}
