package ex2;

import java.util.Random;

public class GuessRepository {

	public final boolean[] items;
	
	public GuessRepository(int N, double positivePortion) {
		items = new boolean[N];
		
		Random rand = new Random();
		for (int i = 0; i < items.length; i++) {
			items[i] = rand.nextDouble() < positivePortion;
		}
	}
	
	
}
