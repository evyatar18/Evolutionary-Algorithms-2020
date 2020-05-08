package genetic_base;

public interface GenerationListener<T extends Chromosome> {

	void onPopulationChange(Population<T> newPopulation, boolean lastPopulation);
}
