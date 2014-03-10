package usdl.servicemodel;


import java.util.ArrayList;
import java.util.List;

import Factories.RDFPropertiesFactory;
import Factories.RDFSPropertiesFactory;
import Factories.USDLPricePropertiesFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import usdl.constants.enums.Prefixes;
import usdl.servicemodel.PriceSpec;

/**
 * The PriceComponent class represents an instance of a PriceComponent resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class PriceComponent {
	private String name;
	private boolean isDeduction = false;
	private PriceSpec componentCap = null;
	private PriceSpec componentFloor = null;
	private PriceSpec price = null;
	private PriceFunction priceFunction = null;
	private List<QuantitativeValue> metrics = null;
	private String comment = null;
	
	public PriceComponent() {
		super();
		metrics = new ArrayList<QuantitativeValue>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeduction() {
		return isDeduction;
	}

	public void setDeduction(boolean isDeduction) {
		this.isDeduction = isDeduction;
	}

	public PriceSpec getComponentCap() {
		return componentCap;
	}

	public void setComponentCap(PriceSpec componentCap) {
		this.componentCap = componentCap;
	}

	public PriceSpec getComponentFloor() {
		return componentFloor;
	}

	public void setComponentFloor(PriceSpec componentFloor) {
		this.componentFloor = componentFloor;
	}

	public PriceSpec getPrice() {
		return price;
	}

	public void setPrice(PriceSpec price) {
		this.price = price;
	}

	public PriceFunction getPriceFunction() {
		return priceFunction;
	}

	public void setPriceFunction(PriceFunction priceFunction) {
		this.priceFunction = priceFunction;
	}

	public List<QuantitativeValue> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<QuantitativeValue> metrics) {
		this.metrics = metrics;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "PriceComponent [name=" + name + ", isDeduction=" + isDeduction
				+ ", componentCap=" + componentCap + ", componentFloor="
				+ componentFloor + ", price=" + price + ", priceFunction="
				+ priceFunction + ", metrics=" + metrics + ", comment="
				+ comment + "]";
	}

	// New Functions
	/**
	 * Adds a Metric to the PriceComponent
	 * @param   val   Metric related to the PriceComponent
	 */
	public void addMetric(QuantitativeValue val)
	{
		this.metrics.add(val);
	}
	/**
	 * Removes a Metric from the PriceComponent
	 * @param   index   Metric's position
	 */
	public void removeMetricAtIndex(int index)
	{
		this.metrics.remove(index);
	}
	
	/**
	 * Calculates the price value of the PriceComponent
	 * @return   price   PriceSpec instance that contains the value of the price component
	 */
	public PriceSpec calculatePrice()
	{
		PriceSpec price = new PriceSpec();
		//call the function of the component from the model
		return price;
	}
	
	/**
	 * Creates a Resource representation of the PriceComponent instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner, Model model)
	{
		USDLPricePropertiesFactory PriceProp = new USDLPricePropertiesFactory(model);
		RDFSPropertiesFactory RDFSProp = new RDFSPropertiesFactory(model);
		RDFPropertiesFactory RDFProp = new RDFPropertiesFactory(model);
		
		
		Resource pc = null;
		if(name != null)
		{
			pc = model.createResource(Prefixes.BASE.getName() + this.name);
			pc.addProperty(RDFProp.type(), model.createResource(Prefixes.USDL_PRICE.getName() + "PriceComponent"));//rdf type
			pc.addProperty(RDFSProp.label(), model.createLiteral(this.name));//label name
		}
		
		if(pc != null)
		{
			if(comment != null)
				pc.addProperty(RDFSProp.comment(), model.createLiteral(this.comment)); // a comment
			
			if(this.componentCap != null)
				//this.componentCap.writeToModel(pc,model);
			
			if(this.componentFloor != null)
				//this.componentFloor.writeToModel(pc,model);
			
			if(this.price != null)
				//this.price.writeToModel(pc,model);
			
			if(this.priceFunction != null)
				this.priceFunction.writeToModel(pc,model);
			
			for(QuantitativeValue metric : this.metrics)
				//metric.writeToModel(pc,model);
			
			if(this.isDeduction)
				pc.addProperty(RDFSProp.subClassOf(), model.createResource(Prefixes.USDL_PRICE.getName() + "Deduction"));
			
			owner.addProperty(PriceProp.hasPriceComponent(), pc);//link the Price Component with the Price Plan
		}
	}
	
	
	/**
	 * Reads a PriceComponent object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceComponent.
	 * @param   model   Model where the resource is located.
	 * @return  A PriceComponent object populated with its information extracted from the Semantic Model.
	 */
	public static PriceComponent readFromModel(Resource resource,Model model)
	{
		PriceComponent pc = new PriceComponent();
		
		RDFSPropertiesFactory rdfsprop= new RDFSPropertiesFactory(model);
		RDFPropertiesFactory rdfprop= new RDFPropertiesFactory(model);
		USDLPricePropertiesFactory priceprop = new USDLPricePropertiesFactory(model);

		//populate the PricePlan

		if(resource.hasProperty(rdfsprop.comment()))
			pc.setComment(resource.getProperty(rdfsprop.comment()).getString());
		
		if(resource.hasProperty(priceprop.hasPriceCap()))//if the resource has a pricecap
		{
			Resource pricecap = resource.getProperty(priceprop.hasPriceCap()).getResource();
			//pc.setPriceCap(PriceSpec.readFromModel(pricecap,model));//read it and add it to the price comp
		}
		
		if(resource.hasProperty(priceprop.hasPriceFloor()))//if the resource has a pricefloor
		{
			Resource pricefloor = resource.getProperty(priceprop.hasPriceFloor()).getResource();
			//pc.setPriceFloor(PriceSpec.readFromModel(pricefloor,model));//read it and add it to the price comp
		}
		
		if(resource.hasProperty(priceprop.hasPrice()))//if the resource has a price
		{
			Resource price = resource.getProperty(priceprop.hasPrice()).getResource();
			//pc.setPrice(PriceSpec.readFromModel(price,model));//read it and add it to the price plan
		}
		
		if(resource.hasProperty(rdfsprop.label()))
			pc.setName(resource.getProperty(rdfsprop.label()).getString());
		
		if(resource.hasProperty(rdfprop.type()) || resource.hasProperty(rdfsprop.subClassOf()))
			if(resource.hasProperty(rdfprop.type()))
			{
				if(resource.getProperty(rdfprop.type()).getResource().getLocalName().equals("Deduction"))
					pc.setDeduction(true);
			}
			else if(resource.hasProperty(rdfsprop.subClassOf()))
			{
				if(resource.getProperty(rdfsprop.subClassOf()).getResource().getLocalName().equals("Deduction"))
					pc.setDeduction(true);
			}
			
		if(resource.hasProperty(priceprop.hasMetrics()))
		{
			//get metrics
			StmtIterator iter = resource.listProperties(priceprop.hasMetrics());
			while (iter.hasNext()) {//while there's price metrics  left
				Resource metric = iter.next().getObject().asResource();
				//pc.addMetric(QuantitativeValue.readFromModel(metric,model));
			}
		}
		
		if(resource.hasProperty(priceprop.hasPriceFunction()))//if it has a function
		{
			Resource function = resource.getProperty(priceprop.hasPriceFunction()).getResource();//fetch the resource
			pc.setPriceFunction(PriceFunction.readFromModel(function,model));//and read it
		}
			
		
		return pc;
	}
}