package ex2.gp;

import java.util.Random;

import ex2.gp.GPNode.GPNodeFunction;
import ex2.gp.GPNode.GPNodeType;

public class GPNodeCreators {

	public static GPNodeCreator makeConstant(double value) {
		GPNode node = new GPNode("const: " + value, GPNodeType.CONSTANT, 0, (vars, inputs) -> value);
		return new ConstantNodeCreator(node);
	}

	public static GPNodeCreator makeUniformConstant(double min, double max) {
		return new GPNodeCreator() {

			@Override
			public GPNode make() {
				Random prg = new Random();
				double val = prg.nextDouble() * (max - min) + min;

				return new GPNode("const: " + val, GPNodeType.CONSTANT, 0, (x, y) -> val);
			}

			@Override
			public int arity() {
				return 0;
			}
		};
	}

	public static GPNodeCreator makeGaussianConstant(double center, double stdDev) {
		return new GPNodeCreator() {

			@Override
			public GPNode make() {
				Random prg = new Random();
				double val = prg.nextGaussian() * stdDev + center;

				return new GPNode("const: " + val, GPNodeType.CONSTANT, 0,
						(x, y) -> val);
			}

			@Override
			public int arity() {
				return 0;
			}
		};
	}

	public static final GPNodeCreator ADD2 = makeOperatorCreator("add", 2, (vars, inputs) -> inputs[0] + inputs[1]);

	public static final GPNodeCreator MULT2 = makeOperatorCreator("mult", 2, (vars, inputs) -> inputs[0] * inputs[1]);

	public static GPNodeCreator makeOperatorCreator(String name, int arity, GPNodeFunction func) {
		final GPNode node = new GPNode(name, GPNodeType.OPERATOR, arity, func);
		return new ConstantNodeCreator(node);
	}
}
