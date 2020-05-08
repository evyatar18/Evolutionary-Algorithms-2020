package genetic_base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Population<T extends Chromosome>{

	private final List<T> chromos;
	private final Map<T, Double> fitness;
	
	public Population(List<T> chromos, FitnessMeter<T> fitness) {
		this.chromos = chromos;
		this.fitness = new HashMap<>();
		
		calculateFitness(chromos, fitness);
		sortByFitness();
	}

	private void sortByFitness() {
		chromos.sort((ch1, ch2) -> -Double.compare(getFitness(ch1), getFitness(ch2)));
	}

	private void calculateFitness(List<T> chromos, FitnessMeter<T> fitness) {
		for (T chromo : chromos) {
			this.fitness.put(chromo, fitness.fitness(chromo));
		}
	}
	
	public double getFitness(T ch) {
		return fitness.get(ch);
	}
	
	public List<T> getChromosomes() {
		return this.chromos;
	}
	
	public T best() {
		return this.chromos.get(0);
	}

	public int size() {
		return this.chromos.size();
	}
}
