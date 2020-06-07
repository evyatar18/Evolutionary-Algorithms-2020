package global;

public class Variable<T> {
	
	private T val;
	
	public Variable(T initialValue) {
		val = initialValue;
	}
	
	public Variable() {
		this(null);
	}
	
	public T get() {
		return val;
	}
	
	public void set(T val) {
		this.val = val;
	}
}
