package genetic_base;

public interface Crossover<T extends Chromosome> {

	public static class ChromosomePair {
		final Chromosome c1;
		final Chromosome c2;
		
		public ChromosomePair(Chromosome c1, Chromosome c2) {
			this.c1 = c1;
			this.c2 = c2;
		}
	}
	
	ChromosomePair crossover(Chromosome parent1, Chromosome parent2);
}
