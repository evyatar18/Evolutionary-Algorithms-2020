package ex1.textguesser;

import java.util.Random;

import genetic_base.Mutator;
import global.Variable;

public class TextMutator implements Mutator<TextChromosome> {

	private final Random random = new Random();
	private final Text targetText;
	private final Variable<Double> charMutationChance;
	
	public TextMutator(Text targetText, Variable<Double> charMutationChance) {
		this.targetText = targetText;
		this.charMutationChance = charMutationChance;
	}
	
	
	@Override
	public TextChromosome mutate(TextChromosome chromo) {
		StringBuilder mutatedTextBuilder = new StringBuilder();
		
		for (int i = 0; i < chromo.length(); i++) {
			char appended;
			
			// mutate this character
			if (random.nextDouble() < charMutationChance.get()) {
				appended = targetText.get(
						random.nextInt(targetText.numberOfDifferentCharacters()));
			}
			
			// don't mutate
			else {
				appended = chromo.get(i);
			}
			
			mutatedTextBuilder.append(appended);
		}
		
		return new TextChromosome(mutatedTextBuilder.toString());
	}

}
