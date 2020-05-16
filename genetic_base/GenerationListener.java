package genetic_base;

import genetic_base.experiment.Chromosome;

public interface GenerationListener<T extends Chromosome> {

	void onPopulationChange(Population<T> newPopulation, boolean lastPopulation);
}
