package ex2.gp;

import java.util.Map;

public class GPNode {

	public static interface GPNodeFunction {
		double calculate(Map<String, Double> vars, double... inputs);
	}
	
	public static enum GPNodeType {
		OPERATOR,
		CONSTANT,
		VARIABLE
	}
	
	public final String name;
	public final GPNodeType type;
	
	public final int arity;
	public final GPNodeFunction func;
	
	
	public GPNode(String name, GPNodeType type, int arity, GPNodeFunction func) {
		this.name = name;
		this.type = type;
		
		this.arity = arity;
		this.func = func;		
	}
	
	public double applyFunction(Map<String, Double> vars, double... inputs) {
		return func.calculate(vars, inputs);
	}
}
