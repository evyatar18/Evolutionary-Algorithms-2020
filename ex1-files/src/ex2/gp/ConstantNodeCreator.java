package ex2.gp;

public class ConstantNodeCreator implements GPNodeCreator {

	private final GPNode node;
	
	public ConstantNodeCreator(GPNode node) {
		this.node = node;
	}
	
	@Override
	public int arity() {
		return node.arity;
	}

	@Override
	public GPNode make() {
		return node;
	}

}
