package ex1.textguesser;

import java.util.Random;

import genetic_base.ChromoCreator;

public class TextChromosomeCreator implements ChromoCreator<TextChromosome> {

	
	private final Random random = new Random();
	private final Text targetText;
	
	public TextChromosomeCreator(Text targetText) {
		this.targetText = targetText;
	}
	
	@Override
	public TextChromosome createChromo() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < targetText.length(); ++i) {
			sb.append(targetText.get(random.nextInt(
					targetText.numberOfDifferentCharacters()))
			);
		}
		
		return new TextChromosome(sb.toString());

	}
}
