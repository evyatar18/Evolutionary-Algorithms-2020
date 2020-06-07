package ex1.textguesser;

import java.util.HashSet;
import java.util.Set;

/**
 * Notice that only this object and the object which created it know what text
 * it has inside.
 */
public class Text {

	private final Character[] chars;
	private final String text;
	
	public Text(String text) {
		this.chars = getCharacters(text);
		this.text = text;
	}
	
	private Character[] getCharacters(String text) {
		Set<Character> chars = new HashSet<>();
		
		for (char c : text.toCharArray()) {
			chars.add(c);
		}
		
		return chars.toArray(new Character[chars.size()]);
	}
	
	public Character[] characters() {
		return this.chars;
	}
	
	public char get(int index) {
		return this.chars[index];
	}
	
	public int length() {
		return text.length();
	}
	
	public int numberOfDifferentCharacters() {
		return chars.length;
	}
	
	public int numberOfEqualities(String otherText) {
		int len = Math.min(text.length(), otherText.length());
		int count = 0;
		
		for (int i = 0; i < len; ++i) {
			if (text.charAt(i) == otherText.charAt(i)) {
				count++;
			}
		}
		
		return count;
	}
}
