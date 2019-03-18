package taskbook.v1.business.task.entity;

import java.util.HashMap;
import java.util.Map;

public enum Difficulty {
	EASY("Easy", 1), INTERMEDIATE("Intermediate", 2), DIFFICULT("Difficult", 3);
	
	private static final Map<String, Difficulty> vals =
			new HashMap<>();
	
	static {
		for(Difficulty type : values()) {
			vals.put(type.toString, type);
		}
	}
	
	private final Integer rank;
	final String toString;
	Difficulty(String s, final Integer rank) {
		this.toString = s;
		this.rank = rank;
	}
	
	public String toString() {
		return this.toString;
	}
	
	public Integer getRank() {
		return this.rank;
	}
	
	public static Difficulty value(final String value) {
		return vals.get(value);
	}
}
