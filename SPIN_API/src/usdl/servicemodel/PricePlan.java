package usdl.servicemodel;

import java.util.List;

import Factories.RDFPropertiesFactory;
import Factories.RDFSPropertiesFactory;
import Factories.USDLPricePropertiesFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import usdl.constants.enums.Prefixes;
import usdl.servicemodel.PriceSpec;


/**
 * The PricePlan class represents an instance of an RDF PricePlan resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class PricePlan {
	private String name;
	private PriceSpec priceCap;
	private PriceSpec priceFloor;
	private List<PriceComponent> priceComponents;
	private String comment;
	//private List<Offering> offerings;//needed? NO
	
	
	
	public PricePlan() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PriceSpec getPriceCap() {
		return priceCap;
	}
	public void setPriceCap(PriceSpec priceCap) {
		this.priceCap = priceCap;
	}
	public PriceSpec getPriceFloor() {
		return priceFloor;
	}
	public void setPriceFloor(PriceSpec priceFloor) {
		this.priceFloor = priceFloor;
	}
	public List<PriceComponent> getPriceComponents() {
		return priceComponents;
	}
	public void setPriceComponents(List<PriceComponent> priceComponents) {
		this.priceComponents = priceComponents;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "PricePlan [name=" + name + ", priceCap=" + priceCap
				+ ", priceFloor=" + priceFloor + ", priceComponents="
				+ priceComponents + ", comment=" + comment + "]";
	}
	
	
	//New functions
	/**
	 * Adds a Price Component to the Price Plan.
	 * @param pc Price Component to be added to the Price Plan.
	 */
	public void addPriceComponent(PriceComponent pc)
	{
		this.priceComponents.add(pc);
	}
	
	/**
	 * Removes a Price Component from the Price Plan.
	 * @param index Position of the Price Component that is to be eliminated.
	 */
	public void removePriceComponentAt(int index)
	{
		this.priceComponents.remove(index);
	}
	
	/**
	 * Calculates the price value of the Price Plan.  The price value is the sum of each its Price Components.
	 * @return  A PriceSpec instance that contains the price value of the Price Plan.
	 */
	private PriceSpec calculatePrice()
	{
		PriceSpec ps = new PriceSpec();
		//sum each of the price components price value
		return ps;
	}
	
	/**
	 * Reads a PricePlan object from the Semantic Model. 
	 * @param   resource   The Resource object of the PricePlan.
	 * @param   model   Model where the resource is located.
	 * @return  A PricePlan object populated with its information extracted from the Semantic Model.
	 */
	public PricePlan readFromModel(Resource resource, Model model)
	{
		PricePlan pp = new PricePlan();
		
		RDFSPropertiesFactory rdfsprop= new RDFSPropertiesFactory(model);
		USDLPricePropertiesFactory priceprop = new USDLPricePropertiesFactory(model);

		//populate the PricePlan

		if(resource.hasProperty(rdfsprop.comment()))
			pp.setComment(resource.getProperty(rdfsprop.comment()).getString());
		
		if(resource.hasProperty(priceprop.hasPriceCap()))//if the resource has a pricecap
		{
			Resource pricecap = resource.getProperty(priceprop.hasPriceCap()).getResource();
			this.setPriceCap(PriceSpec.readFromModel(pricecap,model));//read it and add it to the price plan
		}
		
		if(resource.hasProperty(priceprop.hasPriceFloor()))//if the resource has a price floor
		{
			Resource pricefloor = resource.getProperty(priceprop.hasPriceFloor()).getResource();
			this.setPriceFloor(PriceSpec.readFromModel(pricefloor,model));//read it and add it to the price plan
		}
		 
		if(resource.hasProperty(priceprop.hasPriceComponent()))//if the price plan has components
		{
			//get PriceComponents
			StmtIterator iter = resource.listProperties(priceprop.hasPriceComponent());
			while (iter.hasNext()) {//while there's price components left
				Resource pricecomp = iter.next().getObject().asResource();
				pp.addPriceComponent(PriceComponent.readFromModel(pricecomp,model));//read it and add it to the price plan
			}
		}
		
		return pp;
	}
	
	/**
	 * Creates a Resource representation of the PricePlan instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
				USDLPricePropertiesFactory PriceProp = new USDLPricePropertiesFactory(model);
				RDFSPropertiesFactory RDFSProp = new RDFSPropertiesFactory(model);
				RDFPropertiesFactory RDFProp = new RDFPropertiesFactory(model);
				
				
				Resource pp = model.createResource(Prefixes.BASE.getName() + this.name);
				pp.addProperty(RDFProp.type(), model.createResource(Prefixes.USDL_PRICE.getName() + "PricePlan"));//rdf type
				pp.addProperty(RDFSProp.label(), model.createLiteral(this.name));//label name
				pp.addProperty(RDFSProp.comment(), model.createLiteral(this.comment)); // a comment
				
				if(priceCap != null)
				{
					priceCap.writeToModel(pp,model);//we need to pass pp in order to establish a connection between the resource PricePlan and the resource priceCap
				}
				
				if(priceFloor != null)
				{
					priceFloor.writeToModel(pp,model);//we need to pass pp in order to establish a connection between the resource PricePlan and the resource priceCap
				}
				
				if(!priceComponents.isEmpty())
				{
					for(PriceComponent pc : priceComponents)
					{
						pc.writeToModel(pp,model);//we need to pass pp in order to establish a connection between the resource PricePlan and the resource priceCap
					}
				}
				
				owner.addProperty(PriceProp.hasPricePlan(), pp);//establish a connection between the owner of the PricePlan and the created PricePlan, wich will probably be an instance of the ServiceOffering object
	}
}