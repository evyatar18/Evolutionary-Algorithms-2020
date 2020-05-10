package genetic_base.listeners;

import java.io.OutputStream;
import java.io.PrintWriter;

import genetic_base.Chromosome;
import genetic_base.GenerationListener;
import genetic_base.GeneticUtils;
import genetic_base.Population;

public class FitnessReporter<T extends Chromosome> implements GenerationListener<T> {
	
	private static final String outFmt = "Best: %f, Average: %f";
	private final PrintWriter writer;
	
	public FitnessReporter(OutputStream writeTo) {
		this.writer = new PrintWriter(writeTo);
	}
	
	@Override
	public void onPopulationChange(Population<T> newPopulation,
			boolean lastPopulation) {
		writer.write(String.format(outFmt,
				GeneticUtils.bestFitness(newPopulation),
				GeneticUtils.averageFitness(newPopulation)
			)
		);
		writer.write('\n');
		writer.flush();
	}
	
}
