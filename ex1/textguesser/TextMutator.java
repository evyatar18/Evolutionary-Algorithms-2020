package ex1.textguesser;

import java.util.Random;

import genetic_base.Mutator;

public class TextMutator implements Mutator<TextChromosome> {

	private final Random random = new Random();
	private final double stdDev;
	
	public TextMutator(double stdDev) {
		this.stdDev = stdDev;
	}
	
	@Override
	public TextChromosome mutate(TextChromosome chromo) {
		StringBuilder mutatedTextBuilder = new StringBuilder();
		
		for (int i = 0; i < chromo.length(); i++) {
			char appended = chromo.get(i);
			appended += (char) (random.nextGaussian() * stdDev);
			appended = (char) (appended % 256);
			
			if (appended < 0) {
				appended += 256;
			}
			
			mutatedTextBuilder.append(appended);
		}
		
		return new TextChromosome(mutatedTextBuilder.toString());
	}

}
