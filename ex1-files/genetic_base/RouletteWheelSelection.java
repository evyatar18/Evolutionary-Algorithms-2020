package genetic_base;

import java.util.List;
import java.util.Random;

import genetic_base.experiment.Chromosome;
import genetic_base.experiment.Selection;
import genetic_base.experiment.Crossover.ChromosomePair;

public class RouletteWheelSelection<T extends Chromosome> implements Selection<T> {

	private final Random random = new Random();
	
	private Population<T> population;
	private double totalFitness;

	@Override
	public void setPopulation(Population<T> population) {
		this.population = population;
		this.totalFitness = population
				.getChromosomes()
				.stream()
				.map((chromo) -> population.getFitness(chromo))
				.reduce((x, y) -> x + y)
				.get();
	}
	
	private T selectOne() {
		double chosen = random.nextDouble() * totalFitness;
		List<T> chromos = population.getChromosomes();
		
		double accumulated = 0;
		
		for (int i = 0; i < chromos.size(); ++i) {
			T current = chromos.get(i);
			accumulated += population.getFitness(current);
			
			if (accumulated > chosen) {
				return current;
			}
		}
		
		return null;
	}

	@Override
	public ChromosomePair<T> select() {
		return new ChromosomePair<T>(selectOne(), selectOne());
	}

}
