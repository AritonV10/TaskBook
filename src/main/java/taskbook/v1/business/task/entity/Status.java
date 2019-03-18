package taskbook.v1.business.task.entity;

import java.util.HashMap;
import java.util.Map;

public enum Status {
	
	TAKEN("Taken", 2),
	NOT_TAKEN("Not Taken", 1);
	
	private static Map<String, Status> vals
		= new HashMap<>();
	
	static {
		for(Status type : values()) {
			vals.put(type.toString, type);
		}
	}
	
	private final String toString;
	private final Integer rank;
	Status(final String toString, final Integer rank) {
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
	
	public static Status value(final String value) {
		return vals.get(value);
	}
}
