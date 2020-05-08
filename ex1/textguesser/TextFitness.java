package ex1.textguesser;

import genetic_base.FitnessMeter;

public class TextFitness implements FitnessMeter<TextChromosome> {

	private final Text targetText;

	public TextFitness(Text targetText) {
		this.targetText = targetText;
	}
	
	@Override
	public double fitness(TextChromosome chromo) {
		return targetText.numberOfEqualities(chromo.getText());
	}

}
