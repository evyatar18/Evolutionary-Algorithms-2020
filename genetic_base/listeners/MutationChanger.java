package genetic_base.listeners;

import genetic_base.Chromosome;
import genetic_base.GenerationListener;
import genetic_base.GeneticUtils;
import genetic_base.Population;
import global.Variable;

public class MutationChanger<T extends Chromosome> implements GenerationListener<T> {

	private final Variable<Double> mutationRate;
	private final double original;
	
	public MutationChanger(Variable<Double> mutationRate) {
		this.mutationRate = mutationRate;
		original = mutationRate.get();
	}
	
	@Override
	public void onPopulationChange(Population<T> newPopulation, boolean lastPopulation) {
		double stdDev = GeneticUtils.stdDevFitness(newPopulation);
		
		mutationRate.set(original);
		
		if (stdDev < 2 && mutationRate.get() < 1) {
			mutationRate.set(mutationRate.get() * 2);
//			System.out.printf("multiplied mutation rate: %f\n", mutationRate.get());
		}
	}

}
