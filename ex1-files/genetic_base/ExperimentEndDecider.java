package genetic_base;

import genetic_base.experiment.Chromosome;

public interface ExperimentEndDecider <T extends Chromosome> {

	boolean shouldEnd(Population<T> newPopulation);
}
