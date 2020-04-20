package genetic_base;

public interface FitnessMeter<T extends Chromosome> {
	
	double fitness(T chromo);
}
