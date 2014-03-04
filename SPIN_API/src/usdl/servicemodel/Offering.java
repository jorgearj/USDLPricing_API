package usdl.servicemodel;

import java.util.ArrayList;

public class Offering {
	private String name;
	private ArrayList<Service> includes;
	private PricePlan pricePlan;
	private String comment;
	private ArrayList<QuantitativeFeature> quantfeatures;
	private ArrayList<QualitativeFeature> qualfeatures;
	
	public Offering() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Service> getIncludes() {
		return includes;
	}

	public void setIncludes(ArrayList<Service> includes) {
		this.includes = includes;
	}

	public PricePlan getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(PricePlan pricePlan) {
		this.pricePlan = pricePlan;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Offering [name=" + name + ", includes=" + includes
				+ ", pricePlan=" + pricePlan + ", comment=" + comment + "]";
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