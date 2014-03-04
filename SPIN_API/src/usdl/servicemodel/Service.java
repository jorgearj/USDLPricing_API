package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import usdl.servicemodel.Offering;

public class Service {
	private String name;
	private ArrayList<Offering> offerings;
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

	public List<Offering> getIncludes() {
		return offerings;
	}

	public void setIncludes(ArrayList<Offering> offs) {
		this.offerings = offs;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Service [name=" + name + ", includes=" + offerings
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