package genetic_base;

import java.util.Collection;

import genetic_base.experiment.Chromosome;

public class Elitism<T extends Chromosome> implements PopulationTransformer<T> {

	private final int numOfTaken;
	
	public Elitism() {
		this(2);
	}
	
	public Elitism(int numOfTaken) {
		this.numOfTaken = numOfTaken;
	}
	
	@Override
	public Collection<T> transform(Population<T> pop) {
		return pop.getChromosomes().subList(0, Math.min(numOfTaken, pop.size()));
	}

}
