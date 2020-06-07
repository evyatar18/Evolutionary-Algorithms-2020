package genetic_base.experiment;

import genetic_base.Population;
import genetic_base.experiment.Crossover.ChromosomePair;

public interface Selection<T extends Chromosome> {

	void setPopulation(Population<T> population);
	
	ChromosomePair<T> select();
}
