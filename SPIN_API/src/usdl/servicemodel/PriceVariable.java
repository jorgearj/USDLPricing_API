package usdl.servicemodel;


public class PriceVariable {
	private String name = null;
	//Variable value, either Qualitative or Quantitative
	private Value value = null;
	
	private String comment = null;
	
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