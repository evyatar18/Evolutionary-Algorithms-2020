package genetic_base.experiment;

import genetic_base.Population;

public class Experiment<T extends Chromosome> implements FitnessMeter<T>, Mutator<T>,
	Crossover<T>, Selection<T> {

	public final ChromoCreator<T> initialPopulationCreator;
	
	private final FitnessMeter<T> fitness;
	
	private final Selection<T> selection;
	
	private final Mutator<T> mutator;
	private final Crossover<T> crossover;
	
	public Experiment(ChromoCreator<T> initialPopulationCreator, FitnessMeter<T> fitness,
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

	public Population<T> createInitialPopulation(int size) {
		return new Population<T>(initialPopulationCreator.createChromos(size), fitness);
	}
}
