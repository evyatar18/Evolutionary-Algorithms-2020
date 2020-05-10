package ex1.textguesser;

import genetic_base.GenerationListener;
import genetic_base.GeneticUtils;
import genetic_base.Population;
import global.Variable;

public class TextMutationChanger2 implements GenerationListener<TextChromosome> {

	private final Variable<Double> mutationRate;
	private final double original;
	private final TextFitness fitness;

	public TextMutationChanger2(Variable<Double> mutationRate, TextFitness fitness) {
		this.mutationRate = mutationRate;
		this.original = mutationRate.get();
		this.fitness = fitness;
	}
		
	@Override
	public void onPopulationChange(Population<TextChromosome> newPopulation, boolean lastPopulation) {
		double avgFitness = GeneticUtils.averageFitness(newPopulation);
		
		double avgInvalidText = (fitness.bestFitness() - avgFitness) / fitness.bestFitness();
		
//		double bestInvalidText = (fitness.bestFitness() - newPopulation.getFitness(newPopulation.best()));
		
		
//		if (bestInvalidText < 0.1 && avgInvalidText < 0.1) {
			mutationRate.set(avgInvalidText / 5);
//		}
	}

}
