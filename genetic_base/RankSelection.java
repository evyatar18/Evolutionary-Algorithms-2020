package genetic_base;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import genetic_base.Crossover.ChromosomePair;

public class RankSelection<T extends Chromosome> implements Selection<T> {

	private final Function<Integer, Double> rankToNewFitness;

	/**
	 * Create an instance of {@link RankSelection}
	 * @param rankToNewFitness given the rank ranging [0, populationSize), where the 0-th rank is of the
	 * 	best chromosome and populationSize-1-th rank is of the worst chromosome
	 * 	return a new fitness based on that ranking
	 */
	public RankSelection(Function<Integer, Double> rankToNewFitness) {
		this.rankToNewFitness = rankToNewFitness;
	}
	
	private class RankedFitness implements FitnessMeter<T> {
		
		private final Chromosome[] sortedChromos;
		private final Comparator<Chromosome> chromoComp;

		private RankedFitness(Chromosome[] sortedChromos, Comparator<Chromosome> chromoComp) {
			this.sortedChromos = sortedChromos;
			this.chromoComp = chromoComp;
		}
		
		
		@Override
		public double fitness(T chromo) {
			int index = Arrays.binarySearch(sortedChromos, chromo, chromoComp);
			return rankToNewFitness.apply(index);
		}
		
	}
	
	private RouletteWheelSelection<T> rouletteWheel;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setPopulation(Population<T> population) {
		Chromosome[] sortedChromos = population.getChromosomes().toArray(x -> new Chromosome[x]);
		rouletteWheel = new RouletteWheelSelection<T>();
		rouletteWheel.setPopulation(new Population<T>(population.getChromosomes(),
				new RankedFitness(sortedChromos, (ch1, ch2) ->
				population.getComparator().compare((T) ch1, (T) ch2))));
	}

	@Override
	public ChromosomePair<T> select() {
		return rouletteWheel.select();
	}

	public static final Function<Integer, Double> EXPONENTIAL_FITNESS = i -> Math.pow(2, -i);
	public static final Function<Integer, Double> HARMONIC_FITNESS = i -> 1.0 / (i + 1);
}
