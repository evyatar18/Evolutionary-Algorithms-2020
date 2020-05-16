package ex1.textguesser;

import genetic_base.experiment.FitnessMeter;

public class TextFitness implements FitnessMeter<TextChromosome> {

	private final Text targetText;

	public TextFitness(Text targetText) {
		this.targetText = targetText;
	}
	
	@Override
	public double fitness(TextChromosome chromo) {
		double numOfEqs = targetText.numberOfEqualities(chromo.getText()); 
//		return numOfEqs*numOfEqs*numOfEqs;
		return numOfEqs;
	}
	
	@Override
	public double bestFitness() {
		return targetText.length();
	}

}
