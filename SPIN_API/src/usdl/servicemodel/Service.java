package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import usdl.servicemodel.Offering;

public class Service {
	private String name;
	private List<Service> includes;
	private String comment;
	
	public Service() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Service> getIncludes() {
		return includes;
	}

	public void setIncludes(List<Service> includes) {
		this.includes = includes;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Service [name=" + name + ", includes=" + includes
				+ ", comment=" + comment + "]";
	}
	
	
}