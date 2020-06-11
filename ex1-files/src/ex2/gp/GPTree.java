package ex2.gp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import genetic_base.experiment.Chromosome;

public class GPTree implements Chromosome {

	public final GPTreeNode root;
	
	private final List<GPNode> calculationList;
	
	public GPTree(GPTreeNode root) {
		this.root = root;
		this.calculationList = createCalculationList(root);
	}
	
	private List<GPNode> createCalculationList(GPTreeNode root) {
		Stack<GPNode> ordered = new Stack<>();
		Stack<GPTreeNode> iterating = new Stack<>();
		iterating.add(root);
		
		while (!iterating.isEmpty()) {
			GPTreeNode node = iterating.pop();
			
			ordered.push(node.gpNode);
			
			if (node.children != null) {
				iterating.addAll(node.children);
			}
		}
		
		List<GPNode> calculationList = new ArrayList<>(ordered.size());
		
		while (!ordered.isEmpty()) {
			calculationList.add(ordered.pop());
		}
		
		return calculationList;
	}
	
	private static final Map<String, Double> EMPTY_MAP = new TreeMap<>();
	
	public double calculate() {
		return calculate(EMPTY_MAP);
	}
	
	public double calculate(Map<String, Double> vars) {
		Stack<Double> values = new Stack<>();
		
		for (int i = 0; i < calculationList.size(); i++) {
			GPNode elem = calculationList.get(i);
			
			double[] inputs = new double[elem.arity];
			
			for (int j = 0; j < elem.arity; j++) {
				inputs[j] = values.pop();
			}
			
			double out = elem.applyFunction(vars, inputs);
			
			values.push(out);
		}
		
		return values.pop();
	}
}
