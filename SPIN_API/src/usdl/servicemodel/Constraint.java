package usdl.servicemodel;

import java.util.ArrayList;

public class Constraint {
	private String name;
	private String comment;
	private ArrayList<PriceFunction> constraints;
	
	public Constraint() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "Constraint [name=" + name + ", Constraints=" + constraints
				+ ", thenFunction=" + ", comment=" + comment + "]";
	}

	public ArrayList<PriceFunction> getConstraints() {
		return constraints;
	}

	public void setConstraints(ArrayList<PriceFunction> constraints) {
		this.constraints = constraints;
	}
	
	
	
}