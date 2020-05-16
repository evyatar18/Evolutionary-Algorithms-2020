package ex1.textguesser;

import genetic_base.experiment.Chromosome;

public class TextChromosome implements Chromosome {

	private final String text;

	public TextChromosome(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public int length() {
		return this.text.length();
	}
	
	public char get(int i) {
		return text.charAt(i);
	}
}
