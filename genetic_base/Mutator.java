package genetic_base;

public interface Mutator<T extends Chromosome> {

	T mutate(T chromo);
}
