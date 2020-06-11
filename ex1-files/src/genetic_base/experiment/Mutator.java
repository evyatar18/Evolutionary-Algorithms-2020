package genetic_base.experiment;

public interface Mutator<T extends Chromosome> {

	T mutate(T chromo);
}
