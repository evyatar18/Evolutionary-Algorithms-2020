package ex2;

import java.util.Random;

import genetic_base.experiment.FitnessMeter;

public class GuessFitness implements FitnessMeter<GuessChromo> {

	private final GuessRepository repo;
	private final Random rand = new Random();
	
	private final double beta = 0.25;
	private final double betaSquared = beta * beta;
	
	public GuessFitness(GuessRepository repo) {
		this.repo = repo;
	}

	@Override
	public double fitness(GuessChromo chromo) {
		int N = repo.items.length;
		
		int TP = 0;
		int FP = 0;
		int FN = 0;
		
		for (int i = 0; i < N; i++) {
			boolean guess = rand.nextDouble() < chromo.positiveChance;
			boolean value = repo.items[i];
			
			// we're only interested in the case where either the real value or the guessed value is true
			if (guess || value) {
				if (guess == value) {
					TP++;
				}
				else if (!guess) {
					// guess = false, value = true
					FN++;
				}
				else {
					// guess = true, value = false
					FP++;
				}
			}
		}
		
		double precision = 1.0 * (TP) / (TP + FP);
		double recall = 1.0 * (TP) / (TP + FN);
		
		if (precision == Double.NaN) {
			precision = 0.000001;
		}
		
		if (recall == Double.NaN) {
			recall = 0.000001;
		}
		
		double score = (1+betaSquared) * (precision * recall) / ((betaSquared * precision) + recall);
		
//		System.out.println(precision);
//		System.out.println(recall);
//		System.out.println(score);
		
		return score == Double.NaN ? 0.4 : score;
	}
}
