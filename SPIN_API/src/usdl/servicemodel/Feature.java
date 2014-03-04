package usdl.servicemodel;


import java.util.List;

public class Feature {
	private String name;
	private String comment;
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

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	@Override
	public String toString() {
		return "Feature [name=" + name + ", comment=" + comment + ", value="
				+ ", types=" + types ;
	}

}

