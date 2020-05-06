package ex1.textguesser;

import genetic_base.FitnessMeter;

public class TextFitness implements FitnessMeter<TextChromosome> {

	private final String targetText;

	public TextFitness(String targetText) {
		this.targetText = targetText;
	}
	
	@Override
	public double fitness(TextChromosome chromo) {
		int equalities = 0;
		
		for (int i = 0; i < targetText.length(); ++i) {
			if (chromo.get(i) == targetText.charAt(i)) {
				equalities++;
			}
		}
		
		int eq = equalities + 1;
		
		return Math.exp(eq);
	}

}
