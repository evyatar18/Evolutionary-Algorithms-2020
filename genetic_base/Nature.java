package genetic_base;

import java.util.ArrayList;
import java.util.Collection;
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
	
	@SuppressWarnings("unchecked")
	public Population<T> run(int populationSize, Variable<Double> mutationChance,
			Variable<Double> crossoverChance, Collection<PopulationTransformer<T>> transformers) {
		return run(populationSize, mutationChance, crossoverChance,
				(PopulationTransformer<T>[]) 
				transformers.toArray(x -> new PopulationTransformer[x]));
	}
	
	@SafeVarargs
	public final Population<T> run(int populationSize, Variable<Double> mutationChance,
			Variable<Double> crossoverChance,
			PopulationTransformer<T>... transformers) {
		Population<T> population = experiment.createInitialPopulation(populationSize);
		
		while (!endDecider.shouldEnd(population)) {
			notifyListeners(population, false);
			List<T> newPopulation = new ArrayList<T>();
			
			for (int i = 0; transformers != null && i < transformers.length &&
					newPopulation.size() < populationSize; i++) {
				newPopulation.addAll(transformers[i].transform(population));
			}
			
			newPopulation.addAll(normalRun(population, mutationChance, crossoverChance,
					populationSize - newPopulation.size()));
			
			population = new Population<T>(newPopulation, experiment);
		}
		
		notifyListeners(population, true);
		
		return population;
	}
	
	private Collection<T> normalRun(Population<T> current, Variable<Double> mutationChance,
			Variable<Double> crossoverChance, int neededChromos) {
		List<T> out = new ArrayList<>(neededChromos);
		experiment.setPopulation(current);
		
		while (out.size() < neededChromos) {
			// do selection
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
				
				out.add(chromo);
			}
		}
		
		return out;
	}
	
}
