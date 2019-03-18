package taskbook.v1.business.task.entity;

import java.util.HashMap;
import java.util.Map;

public enum Priority {
	HIGH("High", 3),
	MEDIUM("Medium", 2),
	LOW("Low", 1);
	
	private static final Map<String, Priority>
		values = new HashMap<>();
	
	static {
		for(Priority type : values()) {
			values.put(type.toString, type);
		}
	}
	
	private final String toString;
	private final Integer rank;
	Priority(final String toString, Integer rank) {
		this.toString = toString;
		this.rank = rank;
	}
	
	public boolean equals(final String other) {
		return this.toString.equals(other);
	}
	
	public String toString() {
		return this.toString;
	}
	
	public Integer getRank() {
		return this.rank;
	}
	public static Priority value(final String name) {
		return values.get(name);
	}
}
