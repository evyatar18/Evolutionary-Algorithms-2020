package genetic_base;

public interface ExperimentEndDecider <T extends Chromosome> {

	boolean shouldEnd(Population<T> newPopulation);
}
