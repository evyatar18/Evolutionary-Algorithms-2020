package genetic_base;

public interface GenerationListener<T extends Chromosome> {

	boolean shouldEnd(Population<T> newPopulation);
}
