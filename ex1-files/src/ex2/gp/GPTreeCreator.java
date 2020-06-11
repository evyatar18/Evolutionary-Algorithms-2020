package ex2.gp;

import java.util.Random;
import java.util.Stack;

import genetic_base.experiment.ChromoCreator;

public class GPTreeCreator implements ChromoCreator<GPTree> {

	@Override
	public GPTree createChromo() {
		// TODO Auto-generated method stub
		return null;
	}

	public static GPTree create(int maxDepth, double leafChance, GPNodeRepository repo) {
		Random r = new Random();
		Stack<GPNode> awaitingNodes = new Stack<>();
		Stack<Integer> arityRequirements = new Stack<>();
		Stack<GPTreeNode> completeNodes = new Stack<>();

		GPNode node = createNode(r, leafChance, repo);
		awaitingNodes.add(node);
		arityRequirements.add(node.arity);

		while (!awaitingNodes.isEmpty()) {
			while (!arityRequirements.isEmpty() && arityRequirements.peek() == 0) {
				// remove requirement and matching node as they were satisfied
				arityRequirements.pop();
				GPNode completeNodeDescriptor = awaitingNodes.pop();

				GPTreeNode[] children = new GPTreeNode[completeNodeDescriptor.arity];

				for (int i = 0; i < children.length; i++) {
					children[i] = completeNodes.pop();
				}

				completeNodes.add(new GPTreeNode(completeNodeDescriptor, children));
			}

			if (arityRequirements.isEmpty()) {
				break;
			}

			GPNode addedNode;

			// we must add a leaf
			if (awaitingNodes.size() >= maxDepth - 1) {
				addedNode = repo.getLeaf();
			} else {
				// we'll generate a random node
				addedNode = createNode(r, leafChance, repo);
			}

			arityRequirements.add(arityRequirements.pop() - 1);
			awaitingNodes.add(addedNode);
			arityRequirements.add(addedNode.arity);
		}

		System.out.println("Created tree!");
		return new GPTree(completeNodes.pop());
	}

	private static GPNode createNode(Random rand, double leafChance, GPNodeRepository repo) {
		if (rand.nextDouble() < leafChance) {
			return repo.getLeaf();
		} else {
			return repo.getOperation();
		}
	}

}
