package ex2.gp;

public class BasicGPRepository { 
	
	public static final GPNodeRepository REPO = new GPNodeRepository(
			GPNodeCreators.ADD2,
			GPNodeCreators.MULT2,
			GPNodeCreators.makeConstant(5.5),
			GPNodeCreators.makeUniformConstant(10d, 20d));
}
