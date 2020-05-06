package genetic_base;

import java.util.List;
import java.util.function.Supplier;

public class Experiment<T extends Chromosome> implements FitnessMeter<T>, Mutator<T>,
	Crossover<T>, Selection<T> {

	public final Supplier<List<T>> initialPopulationCreator;
	
	private final FitnessMeter<T> fitness;
	
	private final Selection<T> selection;
	
	private final Mutator<T> mutator;
	private final Crossover<T> crossover;
	
	public Experiment(Supplier<List<T>> initialPopulationCreator, FitnessMeter<T> fitness,
			Mutator<T> mutator, Crossover<T> crossover, Selection<T> selection) {
		this.initialPopulationCreator = initialPopulationCreator;

		this.fitness = fitness;
		
		this.mutator = mutator;
		this.crossover = crossover;
		this.selection = selection;
	}
	
	@Override
	public ChromosomePair<T> crossover(T parent1, T parent2) {
		return crossover.crossover(parent1, parent2);
	}
	@Override
	public T mutate(T chromo) {
		return mutator.mutate(chromo);
	}
	@Override
	public double fitness(T chromo) {
		return fitness.fitness(chromo);
	}

	@Override
	public void setPopulation(Population<T> population) {
		selection.setPopulation(population);
	}

	@Override
	public ChromosomePair<T> select() {
		return selection.select();
	}

	public Population<T> createInitialPopulation() {
		return new Population<T>(initialPopulationCreator.get(), fitness);
	}
}
