package usdl.servicemodel;


public class PriceVariable {
	private String name = null;
	//Variable value, either Qualitative or Quantitative
	private Value value = null;
	private String comment = null;
	private String localName = null;
	private String namespace = null;
	
	protected PriceVariable() {
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
	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName.replaceAll(" ", "_");
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public String toString() {
		return "PriceVariable [name=" + name + ", value=" + value
				+ ", comment=" + comment + "]";
	}
	
}