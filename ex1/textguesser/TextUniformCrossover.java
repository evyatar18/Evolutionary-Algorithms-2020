package ex1.textguesser;

import java.util.Random;

import genetic_base.Crossover;

public class TextUniformCrossover implements Crossover<TextChromosome> {

	private final Random random = new Random();
	
	@Override
	public ChromosomePair<TextChromosome> crossover(TextChromosome parent1, TextChromosome parent2) {
		if (parent1.length() != parent2.length()) {
			throw new RuntimeException("Parents must be of same length.");
		}
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		for (int i = 0; i < parent1.length(); ++i) {
			char ch1 = parent1.get(i);
			char ch2 = parent2.get(i);
			
			// switch values in 0.5 probability
			if (random.nextBoolean()) {
				char temp = ch1;
				ch1 = ch2;
				ch2 = temp;
			}
			
			sb1.append(ch1);
			sb2.append(ch2);
		}

		return new ChromosomePair<TextChromosome>(
				new TextChromosome(sb1.toString()),
				new TextChromosome(sb2.toString())
				);
	}

}
