package ex2.gp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class GPTreeNode {

	private static final List<GPTreeNode> EMPTY_LIST = new ArrayList<>();

	public final GPNode gpNode;
	public final List<GPTreeNode> children;

	public final int depth;
	public final int size;

	public GPTreeNode(GPNode gpNode, GPTreeNode... children) {
		this(gpNode, children == null ? null : Arrays.asList(children));
	}

	public GPTreeNode(GPNode gpNode, List<GPTreeNode> children) {
		this.gpNode = gpNode;
		this.children = children == null ? EMPTY_LIST : children;

		if (this.children.isEmpty()) {
			depth = 1;
			size = 1;
		} else {
			depth = children.stream().mapToInt((node) -> node.depth).max().getAsInt() + 1;
			size = children.stream().mapToInt((node) -> node.size).sum() + 1;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("<");
		sb.append(gpNode.name);
		sb.append(",");
		sb.append("depth: " + depth);
		sb.append(",");
		sb.append("size: " + size);
		sb.append(">");

		switch (gpNode.type) {
		case CONSTANT:
		case VARIABLE:
			break;
		case OPERATOR:
			sb.append("(");

			StringJoiner sj = new StringJoiner(", ");

			for (GPTreeNode child : children) {
				sj.add(child.toString());
			}

			sb.append(sj.toString());
			sb.append(")");
			break;
		default:
			break;
		}

		return sb.toString();
	}
}
