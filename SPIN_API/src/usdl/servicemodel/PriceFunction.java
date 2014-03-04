package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import usdl.servicemodel.PriceComponent;

public class PriceFunction {
	private String name;
	private String stringFunction;
	private List<PriceVariable> variables;
	private List<Constraint> constraints;
	private String comment;
	
	public PriceFunction() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStringFunction() {
		return stringFunction;
	}
	public void setStringFunction(String stringFunction) {
		this.stringFunction = stringFunction;
	}
	public List<PriceVariable> getVariables() {
		return variables;
	}
	public void setVariables(List<PriceVariable> variables) {
		this.variables = variables;
	}
	public List<Constraint> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "PriceFunction [name=" + name + ", stringFunction="
				+ stringFunction + ", variables=" + variables
				+ ", constraints=" + constraints + ", comment=" + comment + "]";
	}
	
	
}