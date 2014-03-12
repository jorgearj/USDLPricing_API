package usdl.servicemodel;


import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLPriceEnum;
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
		Resource pc = null;
		if(name != null)
		{
			pc = model.createResource(Prefixes.BASE.getName() + this.name + "_" + System.currentTimeMillis());
			pc.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.USDL_PRICE.getName() + "PriceComponent"));//rdf type
			pc.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name));//label name
		}
		
		if(pc != null)
		{
			if(comment != null)
				pc.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.comment)); // a comment
			
			if(this.componentCap != null)
				this.componentCap.writeToModel(pc,model);
			
			if(this.componentFloor != null)
				this.componentFloor.writeToModel(pc,model);
			
			if(this.price != null)
				this.price.writeToModel(pc,model);
			
			if(this.priceFunction != null)
				this.priceFunction.writeToModel(pc,model);
			
			for(QuantitativeValue metric : this.metrics)
				metric.writeToModel(pc,model,2);
			
			if(this.isDeduction)
				pc.addProperty(RDFSEnum.SUB_CLASS_OF.getProperty(model), model.createResource(Prefixes.USDL_PRICE.getName() + "Deduction"));
			
			owner.addProperty(USDLPriceEnum.HAS_PRICE_COMPONENT.getProperty(model), pc);//link the Price Component with the Price Plan
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
		//populate the PricePlan

		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			pc.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_CAP.getProperty(model)))//if the resource has a pricecap
		{
			Resource pricecap = resource.getProperty(USDLPriceEnum.HAS_PRICE_CAP.getProperty(model)).getResource();
			pc.setComponentCap(PriceSpec.readFromModel(pricecap,model));//read it and add it to the price comp
		}
		
		if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_FLOOR.getProperty(model)))//if the resource has a pricefloor
		{
			Resource pricefloor = resource.getProperty(USDLPriceEnum.HAS_PRICE_FLOOR.getProperty(model)).getResource();
			pc.setComponentFloor((PriceSpec.readFromModel(pricefloor,model)));//read it and add it to the price comp
		}
		
		if(resource.hasProperty(USDLPriceEnum.HAS_PRICE.getProperty(model)))//if the resource has a price
		{
			Resource price = resource.getProperty(USDLPriceEnum.HAS_PRICE.getProperty(model)).getResource();
			pc.setPrice(PriceSpec.readFromModel(price,model));//read it and add it to the price plan
		}
		
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			pc.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		
		if(resource.hasProperty(RDFEnum.RDF_TYPE.getProperty(model)) || resource.hasProperty(RDFSEnum.SUB_CLASS_OF.getProperty(model)))
		{
			if(resource.hasProperty(RDFEnum.RDF_TYPE.getProperty(model)))
			{
				if(resource.getProperty(RDFEnum.RDF_TYPE.getProperty(model)).getResource().getLocalName().equals("Deduction"))
					pc.setDeduction(true);
			}
			else if(resource.hasProperty(RDFSEnum.SUB_CLASS_OF.getProperty(model)))
			{
				if(resource.getProperty(RDFSEnum.SUB_CLASS_OF.getProperty(model)).getResource().getLocalName().equals("Deduction"))
					pc.setDeduction(true);
			}
		}
			
		if(resource.hasProperty(USDLPriceEnum.HAS_METRICS.getProperty(model)))
		{
			//get metrics
			StmtIterator iter = resource.listProperties(USDLPriceEnum.HAS_METRICS.getProperty(model));
			while (iter.hasNext()) {//while there's price metrics  left
				Resource metric = iter.next().getObject().asResource();
				pc.addMetric(QuantitativeValue.readFromModel(metric,model));
			}
		}
		
		if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_FUNCTION.getProperty(model)))//if it has a function
		{
			Resource function = resource.getProperty(USDLPriceEnum.HAS_PRICE_FUNCTION.getProperty(model)).getResource();//fetch the resource
			pc.setPriceFunction(PriceFunction.readFromModel(function,model));//and read it
		}
			
		
		return pc;
	}
}