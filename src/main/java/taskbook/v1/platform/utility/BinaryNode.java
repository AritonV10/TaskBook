package taskbook.v1.platform.utility;

public class BinaryNode<T extends Comparable<T>> {
	
	private T value;
    private BinaryNode<T> right;
    private BinaryNode<T> left;
    
    public BinaryNode(final T value, BinaryNode<T> left, BinaryNode<T> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }
    
    public BinaryNode(final T value) {
        this(value, null, null);
    }
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public void setRightNode(final BinaryNode<T> node) {
        this.right = node;
    }
    
    public void setLeftNode(final BinaryNode<T> node) {
        this.left = node;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public BinaryNode<T> getRight() {
        return this.right;
    }
    
    public BinaryNode<T> getLeft() {
        return this.left;
    }
    
    public boolean compare(T other) {
        return this.value.compareTo(other) >= 0 ? true : false;
    }
    
    public int compareTo(T other) {
        return this.value.compareTo(other);
    }
}
