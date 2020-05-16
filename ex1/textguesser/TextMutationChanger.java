package ex1.textguesser;

import java.util.Deque;
import java.util.LinkedList;

import genetic_base.GenerationListener;
import genetic_base.Population;
import global.Variable;

public class TextMutationChanger implements GenerationListener<TextChromosome> {

	private final Variable<Double> mutationRate;
	private final double original;

	public TextMutationChanger(Variable<Double> mutationRate) {
		this.mutationRate = mutationRate;
		this.original = mutationRate.get();
	}
	
	private Deque<Double> bestFitness = new LinkedList<>();
	private static final int QUEUE_SIZE = 10;
	
	@Override
	public void onPopulationChange(Population<TextChromosome> newPopulation, boolean lastPopulation) {
		bestFitness.add(newPopulation.getFitness(newPopulation.best()));
		
		if (bestFitness.size() < QUEUE_SIZE) {
			return;
		}
		
		double first = bestFitness.remove();
		double last = bestFitness.peekLast();
		
		double slope = (last - first) / QUEUE_SIZE;
		System.out.println("slope: " + slope);
		if (slope < 1) {
			mutationRate.set(original * 2);
		}
		else {
			mutationRate.set(original);
		}
	}

}
