package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import usdl.servicemodel.Service;

public class Feature {
	private String name;
	private String comment;
	private Value value;
	private List<String> types;
	
	public Feature() {
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

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	@Override
	public String toString() {
		return "Feature [name=" + name + ", comment=" + comment + ", value="
				+ value + ", types=" + types + "]";
	}
	
	
}

