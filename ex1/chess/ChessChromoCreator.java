package ex1.chess;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChessChromoCreator implements Supplier<List<ChessChromo>> {

	private final int populationSize;
	private final int numberOfQueens;
	
	private final Random random = new Random();

	public ChessChromoCreator(int populationSize, int numberOfQueens) {
		this.populationSize = populationSize;
		this.numberOfQueens = numberOfQueens;
	}
	
	@Override
	public List<ChessChromo> get() {
		List<ChessChromo> l = IntStream.range(0, populationSize).mapToObj((x) -> create()).collect(Collectors.toList());
		return l;
	}

	private ChessChromo create() {
		byte[] queens = new byte[numberOfQueens];
		
		for (int i = 0; i < queens.length; ++i) {
			queens[i] = (byte) random.nextInt(numberOfQueens);
		}
		
		return new ChessChromo(queens);
	}
	
}
