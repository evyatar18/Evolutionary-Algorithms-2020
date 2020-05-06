package genetic_base;

import java.util.Iterator;

public interface Crossover<T extends Chromosome> {

	public class ChromosomePair<T> implements Iterable<T> {
		final T c1;
		final T c2;
		
		public ChromosomePair(T c1, T c2) {
			this.c1 = c1;
			this.c2 = c2;
		}

		@Override
		public Iterator<T> iterator() {
			
			
			return new Iterator<T>() {
				int i = 0;
				@Override
				public boolean hasNext() {
					return i < 2;
				}

				@Override
				public T next() {
					i++;
					
					if (i == 1)
						return c1;
					if (i == 2)
						return c2;
					
					return null;
				}
			};
		}
	}
	
	ChromosomePair<T> crossover(T parent1, T parent2);
}
