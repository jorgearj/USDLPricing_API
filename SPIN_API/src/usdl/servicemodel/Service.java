package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import usdl.servicemodel.Offering;

public class Service {
	private String name;
	//a service can include several other services
	private ArrayList<Service> includes;
	private String comment;
	private ArrayList<QuantitativeFeature> quantfeatures;
	private ArrayList<QualitativeFeature> qualfeatures;
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

	public void setIncludes(ArrayList<Service> services) {
		this.includes = services;
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

	public ArrayList<QuantitativeFeature> getQuantfeatures() {
		return quantfeatures;
	}

	public void setQuantfeatures(ArrayList<QuantitativeFeature> quantfeatures) {
		this.quantfeatures = quantfeatures;
	}

	public ArrayList<QualitativeFeature> getQualfeatures() {
		return qualfeatures;
	}

	public void setQualfeatures(ArrayList<QualitativeFeature> qualfeatures) {
		this.qualfeatures = qualfeatures;
	}
	
}