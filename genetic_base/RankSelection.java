package genetic_base;

import java.util.Arrays;
import java.util.Comparator;

import genetic_base.Crossover.ChromosomePair;

public class RankSelection<T extends Chromosome> implements Selection<T> {

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
			return (1.0 / (index + 1));
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

}
