package usdl.servicemodel;

public class Value {
	private String name;
	private String comment;
	
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
	
	
}