package taskbook.v1.platform.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 
 * @author vio
 * A binary tree without duplicated elements
 * @param <T>
 */
public class UniqueBinaryTree<T extends Comparable<T>> implements BinaryTree<T>{
	
	private BinaryNode<T> root;
    private BiFunction<T, T, Integer> compareTo;
    private Integer size = 0;
	
	public UniqueBinaryTree(final T value, BiFunction<T, T, Integer> compareTo) {
        if(value != null) {
        	this.root = new BinaryNode<T>(value);
        }
        this.compareTo = compareTo;
    }
	
	public UniqueBinaryTree(final T value) {
		this(value, null);
	}
	
	public UniqueBinaryTree(BiFunction<T, T, Integer> compareTo) {
		this(null, compareTo);
	}
    
    public boolean add(T value) {
        if(this.get(value) != null) {
            return false;
        } else if(this.root == null) {
        	this.root = new BinaryNode<T>(value);
        	++size;
        	return true;
        }
        return add_(this.root, value, this.compareTo != null ? compareTo : (a, b) -> a.compareTo(b));
    }
    
    public T get(T value) {
    	if(this.root != null && this.size != 0) {
    		if(this.compareTo != null) {
            	return get(this.root, value, this.compareTo);
            }
        	return get(this.root, value, (a, b) -> a.compareTo(b));
    	}
    	return null;
    }
    
    private T get(BinaryNode<T> node, T value, BiFunction<T, T, Integer> compareTo) {
        if(node != null) {
            if(compareTo.apply(node.getValue(), value) == 0) {
                return node.getValue();
            } else if (compareTo.apply(value, node.getValue()) >= 1) {
                return get(node.getRight(), value, compareTo);
            } else {
                return get(node.getLeft(), value, compareTo);
            }
        }
        return null;
    }
    
    private boolean add_(BinaryNode<T> node, T value, BiFunction<T, T, Integer> compareTo) {
        BinaryNode<T> child;
        if(compareTo.apply(value, node.getValue()) < 0) {
            if(node.getLeft() == null) {
                node.setLeftNode(new BinaryNode<T>(value));
                ++size;
                return true;
            } else {
                child = node.getLeft();
            }
        } else  {
            if(node.getRight() == null) {
                node.setRightNode(new BinaryNode<T>(value));
                ++size;
                return true;
            } else {
                child = node.getRight();
            }
        }
        return add_(child, value, compareTo);
    }

    @Override
    public List<T> toList() {
    	List<T> list = new ArrayList<>();
    	if(this.size >= 1) {
    		traverseAsc(this.root,list, (dest, entity) -> dest.add(entity));
    	}
    	return list;
    }
    
    @Override
    public List<T> toList(Supplier<List<T>> supplier) {
    	List<T> list = supplier.get();
    	if(this.size >= 1) {
    		traverseAsc(this.root,list, (dest, entity) -> dest.add(entity));
    	}
    		
    	return list;
    }
    
    private void traverseAsc(BinaryNode<T> node, List<T> dest, BiConsumer<List<T>, T> consumer) {
    	if(node.getLeft() != null) {
			traverseAsc(node.getLeft(), dest, consumer);
		}
		consumer.accept(dest, node.getValue());
		if(node.getRight() != null) {
			traverseAsc(node.getRight(), dest, consumer);
		}
    }
    
	@Override
	public void display(Consumer<T> consumer) {
		display(this.root, consumer);
	}   
	
	@Override
	public Integer size() {
		return this.size;
	}
	
	public static <K extends Comparable<K>> BinaryTree<K> asTree(K ... entities) {
		BinaryTree<K> tree = new UniqueBinaryTree<K>(entities[0]);
		for(K entity : entities) {
			tree.add(entity);
		}
		return tree;
	}
	
	private void display(BinaryNode<T> node, Consumer<T> consumer) {
		if(node.getLeft() != null) {
			display(node.getLeft(), consumer);
		}
		consumer.accept(node.getValue());
		if(node.getRight() != null) {
			display(node.getRight(), consumer);
		}
	}

	@Override
	public BiConsumer<List<T>, T> accumulator() {
		return List::add;
	}

	@Override
	public Set<Characteristics> characteristics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BinaryOperator<List<T>> combiner() {
		return null;
	}

	@Override
	public Function<List<T>, BinaryTree<T>> finisher() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Supplier<List<T>> supplier() {
		return () -> Collections.emptyList();
	}
}
