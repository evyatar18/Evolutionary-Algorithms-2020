package genetic_base;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface ChromoCreator<T extends Chromosome> {

	T createChromo();
	
	default List<T> createChromos(int size) {
		List<T> l = IntStream.range(0, size)
				.mapToObj((x) -> createChromo())
				.collect(Collectors.toList());
		return l;
	}
}
