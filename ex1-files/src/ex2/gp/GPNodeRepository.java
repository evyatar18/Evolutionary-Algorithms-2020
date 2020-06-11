package ex2.gp;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GPNodeRepository {
	
	public final List<GPNodeCreator> nodes;
	
	public final List<GPNodeCreator> leaves;
	public final List<GPNodeCreator> operations;
	
	private Random rand = new Random();
	
	public GPNodeRepository(GPNodeCreator... nodes) {
		this(Arrays.asList(nodes));
	}
	
	public GPNodeRepository(List<GPNodeCreator> nodes) {
		this.nodes = nodes;
		
		this.leaves = nodes.stream()
				.filter(node -> node.arity() == 0)
				.collect(Collectors.toUnmodifiableList());
		
		this.operations = nodes.stream()
				.filter(node -> node.arity() > 0)
				.collect(Collectors.toUnmodifiableList());
	}

	private GPNode choose(List<GPNodeCreator> nodeList) {
		return nodeList.get(rand.nextInt(nodeList.size())).make();
	}
	
	public GPNode getLeaf() {
		return choose(leaves);
	}
	
	public GPNode getOperation() {
		return choose(operations);
	}
	
	public GPNode getNode() {
		return choose(nodes);
	}
	
	public GPNode getNodeOfArity(int arity) {
		List<GPNodeCreator> nodeList = nodes.stream()
				.filter(node -> node.arity() == arity)
				.collect(Collectors.toList());
		return choose(nodeList);
	}
}
