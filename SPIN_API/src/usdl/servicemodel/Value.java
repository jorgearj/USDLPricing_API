package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import exceptions.InvalidLinkedUSDLModelException;

public class Value {
	private String name=null;
	private String label = null;
	private String comment=null;
	private List<String> types=null;
	private String localName = null;
	private String namespace = null;
	
	public Value() {
		super();
		types = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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
	
	public void removeType(int index){
		this.types.remove(index);
	}
	
	protected void validate() throws InvalidLinkedUSDLModelException{
		
	}
	
}