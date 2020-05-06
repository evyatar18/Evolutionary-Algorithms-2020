package genetic_base;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import genetic_base.Crossover.ChromosomePair;

public class Nature {

	private static final Random random = new Random();
	
	public static <T extends Chromosome> T run(Experiment<T> experiment, GenerationListener<T> listener,
			double mutationChance, double crossoverChance) {
		Population<T> population = experiment.createInitialPopulation();
		T best = population.best();
		
		while (!listener.shouldEnd(population)) {
			List<T> newPopulation = new ArrayList<T>();
			
			while (newPopulation.size() < population.size()) {
				// do selection
				experiment.setPopulation(population);
				ChromosomePair<T> pair = experiment.select();
				
				// do crossover
				if (random.nextDouble() < crossoverChance) {
					pair = experiment.crossover(pair.c1, pair.c2);
				}
				
				// do mutation and add to new population
				for (T chromo : pair) {
					if (random.nextDouble() < mutationChance) {
						chromo = experiment.mutate(chromo);
					}
					
					newPopulation.add(chromo);
				}
			}
			
			population = new Population<T>(newPopulation, experiment);
		}
		
		return best;
	}
}
