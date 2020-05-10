package ex1.textguesser;

import java.util.Random;

import genetic_base.Crossover;

public class TextDoublePointCrossover implements Crossover<TextChromosome> {

	private final Random random = new Random();
	
	@Override
	public ChromosomePair<TextChromosome> crossover(TextChromosome parent1, TextChromosome parent2) {
		if (parent1.length() != parent2.length()) {
			throw new RuntimeException("Parents must be of same length.");
		}
		
		final int len = parent1.length();
		final String txt1 = parent1.getText();
		final String txt2 = parent2.getText();
		
		int firstSlicePoint = random.nextInt(len);
		int secondSlicePoint = firstSlicePoint + random.nextInt(len - firstSlicePoint);
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		sb1.append(txt1.substring(0, firstSlicePoint));
		sb2.append(txt2.substring(0, firstSlicePoint));
		
		sb1.append(txt2.substring(firstSlicePoint, secondSlicePoint));
		sb2.append(txt1.substring(firstSlicePoint, secondSlicePoint));
		
		sb1.append(txt1.substring(secondSlicePoint));
		sb2.append(txt2.substring(secondSlicePoint));
		
		
		return new ChromosomePair<TextChromosome>(
				new TextChromosome(sb1.toString()),
				new TextChromosome(sb2.toString())
		);
	}

}
