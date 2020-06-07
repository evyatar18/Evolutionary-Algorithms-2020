package ex1.chess;

public class RandomQueenGuesser {

	public static ChessChromo guess(int numberOfQueens) {
		ChessChromoCreator creator = new ChessChromoCreator(numberOfQueens);
		
		ChessChromo chromo;
		
		int count = 0;
		do {
			chromo = creator.createChromo();
			count++;
		}
		while (chromo.numberOfCollisions() > 0);
		
		System.out.println(count);
		
		return chromo;
	}
	
}
