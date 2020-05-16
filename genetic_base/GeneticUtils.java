package genetic_base;

import genetic_base.experiment.Chromosome;

public class GeneticUtils {

	public static <T extends Chromosome> double bestFitness(final Population<T> population) {
		return population.getFitness(population.best());
	}
	
	public static <T extends Chromosome> double averageFitness(final Population<T> population) {
		double sum = population.getChromosomes()
				.stream()
				.map((chromo) -> population.getFitness(chromo))
				.reduce(0.0, (x, y) -> x + y);
		return sum / population.size();
	}
	
	public static <T extends Chromosome> double stdDevFitness(final Population<T> population) {
		double sumOfSquares = population.getChromosomes()
				.stream()
				.map(chromo -> {
					double fitness = population.getFitness(chromo);
					return fitness * fitness;
				})
				.reduce(0.0, (x, y) -> x + y);
		
		double sumOfSquaresAvged = sumOfSquares / population.size();
		
		double avg = averageFitness(population);
		double avgSquared = avg * avg;
		
		return Math.sqrt(sumOfSquaresAvged - avgSquared);
	}
}
