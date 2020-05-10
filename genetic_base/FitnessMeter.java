package genetic_base;

public interface FitnessMeter<T extends Chromosome> {
	
	double fitness(T chromo);
	
	default double bestFitness() {
		return Double.POSITIVE_INFINITY; 
	}
}
