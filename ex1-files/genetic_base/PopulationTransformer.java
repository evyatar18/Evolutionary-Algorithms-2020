package genetic_base;

import java.util.Collection;

import genetic_base.experiment.Chromosome;

public interface PopulationTransformer<T extends Chromosome> {

	Collection<T> transform(Population<T> pop);
}
