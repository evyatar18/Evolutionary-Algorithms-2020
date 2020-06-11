package ex2;

import java.io.IOException;

import ex2.gp.BasicGPRepository;
import ex2.gp.GPTree;
import ex2.gp.GPTreeCreator;

public class Main {

	public static void main(String[] args) throws IOException {
//		GuessRepository repo = new GuessRepository(10000, 0.5);
//		
//		GuessExperiment.run(repo);
		
		
		for (int i = 0; i < 10; i++) {
			GPTree tree = GPTreeCreator.create(50, 0.3, BasicGPRepository.REPO);
			System.out.println("created tree");
			System.out.println(tree.root.depth);
			System.out.println(tree.root.size);
//			System.out.println(tree.calculate());
			
			System.in.read();
		}
	}
}
