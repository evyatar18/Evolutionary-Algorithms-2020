package ex1.textguesser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import genetic_base.Experiment;
import genetic_base.GeneticUtils;
import genetic_base.Nature;
import genetic_base.Population;
import genetic_base.RankSelection;
import genetic_base.listeners.FitnessReporter;
import global.Variable;

public class TextExperiment {
	
	public static void run(Text text) {
		TextFitness fitness = new TextFitness(text);
		
		// in average, do 1 character change per mutation
		Variable<Double> characterMutationRate = new Variable<Double>(
				1.0 / text.length());
		
		Experiment<TextChromosome> textExperiment = new Experiment<TextChromosome>(
				new TextChromosomeCreator(text), 
				fitness,
				new TextMutator(text, characterMutationRate),
				new TextUniformCrossover(),
//				new RankSelection<TextChromosome>((i, n) -> Math.pow(n-i, 2))
				new RankSelection<TextChromosome>(RankSelection.EXPONENTIAL_FITNESS)
		);
		
		Nature<TextChromosome> nature = new Nature<>(textExperiment,
				// the end condition
				(pop) -> text.numberOfEqualities(pop.best().getText()) == text.length());
		
		nature.addListener(TextExperiment::printTextInfo);
		
		try (FileOutputStream stats = new FileOutputStream("text_experiment.txt");
				FileOutputStream result = new FileOutputStream("text_result")) {
			nature.addListener(new FitnessReporter<TextChromosome>(stats));
			
			int N = 35;
			Variable<Double> mutationRate = new Variable<Double>(1.0);
			Variable<Double> crossoverRate = new Variable<>(0.75);
			
			Population<TextChromosome> lastPop = nature.run(N,
					mutationRate,
					crossoverRate);
			
			result.write(lastPop.best().getText().getBytes());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int generationNumber = 0;

	private static void printTextInfo(Population<TextChromosome> pop, boolean lastPop) {
		if (generationNumber % 100 == 0 || lastPop) {
			System.out.println("Current generation: " + generationNumber);
			TextChromosome chromo = pop.best();
			System.out.println("best chromo: " + chromo.getText());
			System.out.println("best fitness: " + pop.getFitness(chromo));
			System.out.println("avg fitness: " + GeneticUtils.averageFitness(pop));
			
			System.out.println("gen*pop size = " + (generationNumber * pop.size()));
		}
		
		generationNumber++;
	}

}
