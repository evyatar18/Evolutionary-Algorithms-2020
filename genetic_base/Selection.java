package genetic_base;

import genetic_base.Crossover.ChromosomePair;

public interface Selection<T extends Chromosome> {

	void setPopulation(Population<T> population);
	
	ChromosomePair<T> select();
}
