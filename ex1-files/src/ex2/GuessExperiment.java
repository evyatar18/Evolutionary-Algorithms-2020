package ex2;

import genetic_base.Elitism;
import genetic_base.ExperimentEndDecider;
import genetic_base.Nature;
import genetic_base.Population;
import genetic_base.RankSelection;
import genetic_base.experiment.Experiment;
import global.Variable;

public class GuessExperiment {

	public static void run(GuessRepository repo) {
		final GuessFitness fitness = new GuessFitness(repo);
		final GuessMutator mutator = new GuessMutator(0.01);
		final GuessCrossover crossover = new GuessCrossover();	

		Experiment<GuessChromo> exp = new Experiment<>(new GuessChromoCreator(),
				fitness,
				mutator,
				crossover,
				new RankSelection<>(RankSelection.EXPONENTIAL_FITNESS)
				);
		
		Nature<GuessChromo> nature = new Nature<>(exp,
				// the end condition
				new ExperimentEndDecider<GuessChromo>() {
					int iters = 0;
					
					@Override
					public boolean shouldEnd(Population<GuessChromo> newPopulation) {
						return iters++ > 30000;
					}
				});
		
		int N = 100;
		nature.addListener(GuessExperiment::generationListener);
		nature.run(N, new Variable<>(0.1), new Variable<>(0.75), new Elitism<>());
	}
	
	private static void generationListener(Population<GuessChromo> newPopulation, boolean lastPopulation) {
		GuessChromo best = newPopulation.best();
		double fitness = newPopulation.getFitness(best);
		
		System.out.printf("Best chromo with chance: %f, fitness: %f", best.positiveChance, fitness);
		System.out.println();
	}
}
