package usdl.servicemodel;

public class Constraint {
	private String name;
	private PriceFunction ifFunction;
	private PriceFunction thenFunction;
	private PriceFunction elseFunction;
	private String comment;
	
	
	public Constraint() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PriceFunction getIfFunction() {
		return ifFunction;
	}
	public void setIfFunction(PriceFunction ifFunction) {
		this.ifFunction = ifFunction;
	}
	public PriceFunction getThenFunction() {
		return thenFunction;
	}
	public void setThenFunction(PriceFunction thenFunction) {
		this.thenFunction = thenFunction;
	}
	public PriceFunction getElseFunction() {
		return elseFunction;
	}
	public void setElseFunction(PriceFunction elseFunction) {
		this.elseFunction = elseFunction;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "Constraint [name=" + name + ", ifFunction=" + ifFunction
				+ ", thenFunction=" + thenFunction + ", elseFunction="
				+ elseFunction + ", comment=" + comment + "]";
	}
	
	
	
}