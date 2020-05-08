package genetic_base;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genetic_base.Crossover.ChromosomePair;
import global.Variable;

public class Nature<T extends Chromosome> {

	private static final Random random = new Random();
	
	private final Experiment<T> experiment;
	private final List<GenerationListener<T>> listeners;
	private final ExperimentEndDecider<T> endDecider;
	
	public Nature(Experiment<T> experiment, ExperimentEndDecider<T> endDecider) {
		this.experiment = experiment;
		this.listeners = new ArrayList<>();
		this.endDecider = endDecider;
	}
	
	public void addListener(GenerationListener<T> listener) {
		listeners.add(listener);
	}
	
	public void removeListener(GenerationListener<T> listener) {
		listeners.remove(listener);
	}
	
	private void notifyListeners(Population<T> populationChange,
			boolean lastPopulation) {
		for (GenerationListener<T> listener : listeners) {
			listener.onPopulationChange(populationChange, lastPopulation);
		}
	}
	
	public T run(int populationSize,
			Variable<Double> mutationChance,
			Variable<Double> crossoverChance) {
		Population<T> population = experiment.createInitialPopulation(populationSize);
		T best = population.best();
		
		while (!endDecider.shouldEnd(population)) {
			notifyListeners(population, false);
			List<T> newPopulation = new ArrayList<T>();
			
			while (newPopulation.size() < population.size()) {
				// do selection
				experiment.setPopulation(population);
				ChromosomePair<T> pair = experiment.select();
				
				// do crossover
				if (random.nextDouble() < crossoverChance.get()) {
					pair = experiment.crossover(pair.c1, pair.c2);
				}
				
				// do mutation and add to new population
				for (T chromo : pair) {
					if (random.nextDouble() < mutationChance.get()) {
						chromo = experiment.mutate(chromo);
					}
					
					newPopulation.add(chromo);
				}
			}
			
			population = new Population<T>(newPopulation, experiment);
		}
		
		notifyListeners(population, true);
		
		return best;
	}
	
}
