package taskbook.v1.business.algorithm.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import taskbook.v1.business.task.entity.FinishedTasks;

public class Algorithm {
	
	/**
	 * 
	 * @param entities 	the list to find the element
	 * @param target	the element it's looking for
	 * @param pred		predicate for if equal
	 * @param bigger	predicate if bigger
	 * @return			the element if found else null
	 */
	public <T, D> T binarySearch(
			final List<T> entities, 
			D target,
			BiPred<T, D> pred,
			BiPred<T, D> bigger) {
		int low = 0;
		int high = entities.size() - 1;
		while(low <= high) {
			int mid = (low + high)/2;
			if(pred.apply(entities.get(mid), target)) {
				return entities.get(mid);
			} else if(bigger.apply(entities.get(mid), target)) {
				low = mid +1;
			} else if(!bigger.apply(entities.get(mid), target)) {
				high = mid - 1;
			}
		}
		return null;
	}
	
	/**
	 * Searching for an element in a list to see if exist
	 * The conditions are checked using {@value compareTo} method, thus the entity has to
	 * implement the {@link Comparable} interface
	 * @param entities	the list to find the element
	 * @param target	the element It's looking for
	 * @param predicate	the predicate condition
	 * @return	T		the element if exists else null
	 */
	public <T, D> int binarySearch(
			final List<T> entities, 
			D target,
			BiFunction<D, T, Integer> predicate) {
		int low = 0;
		int high = entities.size() - 1;
		while(low <= high) {
			int mid = (low + high)/2;
			if(predicate.apply(target, entities.get(mid)) == 0) {
				return mid;
			} else if(predicate.apply(target, entities.get(mid)) >= 1) {
				low = mid +1;
			} else if(predicate.apply(target, entities.get(mid)) <= 1) {
				high = mid - 1;
			}
		}
		return -1;
	}
	
	/**
	 * Sorting a list of elements based on a predicate
	 * @param entities	the list of entities to be sorted
	 * @param predicate	the condition
	 */
	public <T> void insertionSort(List<T> entities, BiPred<T, T> predicate) {
		for(int i = 1; i < entities.size(); ++i) {
			T entity = entities.get(i);
			int index = i - 1;
			while(index > -1 && predicate.apply(entities.get(index), entity)) {
				entities.set(index + 1, entities.get(index));
				--index;
			}
			entities.set(index + 1, entity);
		}
	}
	
	public <T> void insertionSort(List<T> entities, BiFunction<T, T, Integer> pred, String order) {
        BiFunction<T, T, Boolean> func = null;
       switch(order) {
           case "asc":
               func = (a, b) -> pred.apply(a, b) >= 1;
               break;
           case "desc":
               func = (a, b) -> pred.apply(a, b) < 0;
               break;
       }
       for(int i = 1; i < entities.size(); ++i) {
		    T entity = entities.get(i);
		    int index = i - 1;
		    while(index > -1 && func.apply(entities.get(index), entity)) {
			    entities.set(index + 1, entities.get(index));
			    --index;
		    }
		    entities.set(index + 1, entity);
	    }
   }
	
	private void setup(List<FinishedTasks> tasks) {
		int y = -1, x = -1, smallest = 100;
		Map<Integer, Integer> m = new HashMap<>();
		for(FinishedTasks t : tasks) {
			Integer month = t.getFinishedDate().getMonthValue();
			if(month < smallest) {
				smallest = month;
			}
			if(month > x) {
				x = month;
			}
			m.merge(month, 1, Integer::sum);
		}
		int copy_x = x, copy_smallest = smallest;
		int xMean = 
				(copy_x*(copy_x+1)/2 - ((copy_smallest - 1)*copy_smallest)/2)
				/
				(copy_x - copy_smallest)+1;
		
	}
	
	public static interface BiPred<T, D> {
		boolean apply(T arg1, D arg2);
	}
}
