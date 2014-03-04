package usdl.servicemodel;

import java.util.ArrayList;

import usdl.servicemodel.PriceFunction;

public class PriceVariable {
	private String name;
	private Value value;
	private String comment;
	
	public PriceVariable() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "PriceVariable [name=" + name + ", value=" + value
				+ ", comment=" + comment + "]";
	}

	
}