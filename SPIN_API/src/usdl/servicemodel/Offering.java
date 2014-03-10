package usdl.servicemodel;

import java.util.ArrayList;

import Factories.RDFPropertiesFactory;
import Factories.RDFSPropertiesFactory;
import Factories.USDLPricePropertiesFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

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
	
	public static Offering readFromModel(Resource resource, Model model){
		USDLPricePropertiesFactory PriceProp = new USDLPricePropertiesFactory(model);
		RDFSPropertiesFactory RDFSProp = new RDFSPropertiesFactory(model);
		RDFPropertiesFactory RDFProp = new RDFPropertiesFactory(model);
		
		
		
		Offering offering = new Offering();
		
		offering.setName(resource.getLocalName());
		
		if(resource.hasProperty(PriceProp.hasPricePlan()))
		{
			Resource pp = resource.getProperty(PriceProp.hasPricePlan()).getResource();
			offering.setPricePlan(PricePlan.readFromModel(pp, model ));
		}
		return offering;
	}
	
	@Override
	public String toString() {
		return "Offering [name=" + name + ", includes=" + includes
				+ ", pricePlan=" + pricePlan + ", comment=" + comment + "]";
	}

	
}