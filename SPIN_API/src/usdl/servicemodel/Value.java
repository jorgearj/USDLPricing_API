package usdl.servicemodel;

import java.util.List;

public class Value {
	private String name;
	private String comment;
	private List<String> types;
	public Value() {
		super();
		// TODO Auto-generated constructor stub
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
		return "Value [name=" + name + ", comment=" + comment + "]";
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
	
	public void addType(String type)
	{
		this.types.add(type);
	}
	
}