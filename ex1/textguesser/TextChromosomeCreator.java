package ex1.textguesser;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TextChromosomeCreator implements Supplier<List<TextChromosome>> {

	private final int populationSize;
	private final int textLength;
	
	private final Random random = new Random();

	public TextChromosomeCreator(int populationSize, int textLength) {
		this.populationSize = populationSize;
		this.textLength = textLength;
	}
	
	@Override
	public List<TextChromosome> get() {
		List<TextChromosome> l = IntStream.range(0, populationSize).mapToObj((x) -> create()).collect(Collectors.toList());
		return l;
	}

	private TextChromosome create() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < textLength; ++i) {
			sb.append((char) random.nextInt(256));
		}
		
		return new TextChromosome(sb.toString());
	}
}
