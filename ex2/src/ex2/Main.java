package ex2;

import static io.jenetics.engine.EvolutionResult.toBestEvolutionResult;
import static io.jenetics.engine.Limits.bySteadyFitness;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.SinglePointCrossover;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStreamable;
import io.jenetics.engine.Limits;
import io.jenetics.util.Factory;


public class Main {

	// 2.) Definition of the fitness function.
	private static int eval(Genotype<BitGene> gt) {
		return gt.chromosome().as(BitChromosome.class).bitCount();
	}

	public static void main(String[] args) {
		// 1.) Define the genotype (factory) suitable
		// for the problem.
		Factory<Genotype<BitGene>> gtf = Genotype.of(BitChromosome.of(16, 0.5));

		// The base engine tries to approximate to good solution in current
		// environment.
		final Engine<BitGene, Integer> baseEngine = Engine.builder(Main::eval, gtf)
			.populationSize(500)
			.alterers(
				new Mutator<>(0.115),
				new SinglePointCrossover<>(0.16))
			.build();

		// The 'diversity' engine tries to broaden the search space again.
		final Engine<BitGene, Integer> diversityEngine = baseEngine.toBuilder()
			.alterers(new Mutator<>(0.5))
			.build();

		// Concatenates the two engines into one cyclic engine.
		final EvolutionStreamable<BitGene, Double> engine = CyclicEngine.of(
			// This engine stops the evolution after 10 non-improving
			// generations and hands over to the diversity engine.
			baseEngine.limit(() -> Limits.bySteadyFitness(10)),

			// The higher mutation rate of this engine broadens the search
			// space for 15 generations and hands over to the base engine.
			diversityEngine.limit(15)
		);

		final EvolutionResult<BitGene, Double> best = engine.stream()
			// The evolution is stopped after 50 non-improving generations.
			.limit(bySteadyFitness(50))
			.collect(toBestEvolutionResult());

		System.out.println(best.totalGenerations());
		System.out.println(best.bestPhenotype());
	}
}
