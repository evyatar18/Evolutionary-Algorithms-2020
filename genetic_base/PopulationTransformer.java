package genetic_base;

import java.util.Collection;

public interface PopulationTransformer<T extends Chromosome> {

	Collection<T> transform(Population<T> pop);
}
