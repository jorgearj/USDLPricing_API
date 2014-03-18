package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import org.topbraid.spin.arq.ARQFactory;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLPriceEnum;
import usdl.servicemodel.PriceSpec;


/**
 * The PricePlan class represents an instance of a PricePlan resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class PricePlan {
	private String name;
	private PriceSpec priceCap = null;
	private PriceSpec priceFloor = null;
	private List<PriceComponent> priceComponents = null;
	private String comment;
	//private List<Offering> offerings;//needed? NO
	
	
	
	public PricePlan() {
		super();
		priceComponents = new ArrayList<PriceComponent>();
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
	public String calculatePrice(Model model)
	{
		//sum each of the price components price value
		String finalprice = "";
		double pc_price = -1, lower_limit = -1, upper_limit = -1, function_price = -1,finalvalue=0;
		for(PriceComponent pc : this.priceComponents)
		{
			//get the variables and define their value
			if(pc.getPriceFunction() != null)
			{
				if (pc.getPriceFunction().getSPARQLFunction() != null) {
					com.hp.hpl.jena.query.Query q = ARQFactory.get().createQuery(pc.getPriceFunction().getSPARQLFunction());
					QueryExecution qexecc = ARQFactory.get().createQueryExecution(q, model);	
					ResultSet rsc = qexecc.execSelect();
					function_price = rsc.nextSolution().getLiteral("result").getDouble();// final result is store in the ?result variable of the query
				}
			}
			if (pc.getComponentCap() != null) {
				upper_limit = pc.getComponentCap().getValue();
			}

			if (pc.getComponentFloor() != null) {
				lower_limit =pc.getComponentFloor().getValue();
			}

			if (pc.getPrice() != null) {
				pc_price = pc.getPrice().getValue();
				
			}

			if(function_price >=0)
			{
				if(function_price > upper_limit && upper_limit >= 0)
					function_price = upper_limit;
				else if(function_price < lower_limit && lower_limit >=0)
					function_price = lower_limit;
			}
			
			if(pc_price >= 0)
			{
				if(pc_price > upper_limit && upper_limit >=0)
					pc_price = upper_limit;
				else if(pc_price < lower_limit && lower_limit >= 0)
					pc_price = lower_limit;
			}
			
			if(function_price >= 0 && pc_price >= 0)
				System.out.println("Dynamic and static price? offer->"+this.name+",pc->"+pc.getName() + "price ->"+pc_price);//throw expection?
			
			if(pc.isDeduction())
			{
				if(function_price >= 0)
				{
					finalprice = finalprice+" - " +function_price;
					finalvalue-=function_price;
				}
				else
				{
					finalprice = finalprice+" - " +pc_price;
					finalvalue-=pc_price;
				}
			}
			else
			{
				if(function_price >= 0)
				{
					finalprice = finalprice+" + " +function_price;
					finalvalue+=function_price;
				}
				else
				{
					finalprice = finalprice+" + " +pc_price;
					finalvalue+=pc_price;
				}
			}
		}
		//conditions to verify that the final price is inside the interval defined by the lower and upper limit, in case these exist
		if(this.getPriceCap() != null)
		{
			if(this.getPriceCap().getValue() >= 0 && finalvalue > this.getPriceCap().getValue())
				finalvalue = this.getPriceCap().getValue();
		}
		if(this.getPriceFloor() != null)
		{
			if(this.getPriceFloor().getValue() >= 0 && finalvalue <this.getPriceFloor().getValue())
				finalvalue = this.getPriceFloor().getValue();
		}
				
		return finalprice;
	}
	
	/**
	 * Reads a PricePlan object from the Semantic Model. 
	 * @param   resource   The Resource object of the PricePlan.
	 * @param   model   Model where the resource is located.
	 * @return  A PricePlan object populated with its information extracted from the Semantic Model.
	 */
	public static PricePlan readFromModel(Resource resource, Model model)
	{
		PricePlan pp = new PricePlan();
		
		//populate the PricePlan
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			pp.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		else
		{
			if(resource.getLocalName() != null)
				pp.setName(resource.getLocalName().replaceAll("_TIME\\d+",""));
		}
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			pp.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_CAP.getProperty(model)))//if the resource has a pricecap
		{
			Resource pricecap = resource.getProperty(USDLPriceEnum.HAS_PRICE_CAP.getProperty(model)).getResource();
			pp.setPriceCap(PriceSpec.readFromModel(pricecap,model));//read it and add it to the price plan
		}
		
		if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_FLOOR.getProperty(model)))//if the resource has a price floor
		{
			Resource pricefloor = resource.getProperty(USDLPriceEnum.HAS_PRICE_FLOOR.getProperty(model)).getResource();
			pp.setPriceFloor(PriceSpec.readFromModel(pricefloor,model));//read it and add it to the price plan
		}
		 
		if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_COMPONENT.getProperty(model)))//if the price plan has components
		{
			//get PriceComponents
			StmtIterator iter = resource.listProperties(USDLPriceEnum.HAS_PRICE_COMPONENT.getProperty(model));
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
	public void writeToModel(Resource owner,Model model,String baseURI)
	{
				Resource pp = null;
				if(this.name != null)
				{
					pp = model.createResource(baseURI +"#"  + this.name.replaceAll(" ", "_") + "_TIME" +System.nanoTime());
					pp.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name.replaceAll(" ", "_")));//label name
				}
				else 
					pp = model.createResource(baseURI +"#"  + "PricePlan" + "_TIME" +System.nanoTime());
				
				pp.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.USDL_PRICE.getPrefix() + "PricePlan"));//rdf type
				
				if(this.comment != null)
					pp.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.comment)); // a comment
				
				if(priceCap != null)
				{
					priceCap.writeToModel(pp,model,baseURI);//we need to pass pp in order to establish a connection between the resource PricePlan and the resource priceCap
				}
				
				if(priceFloor != null)
				{
					priceFloor.writeToModel(pp,model,baseURI);//we need to pass pp in order to establish a connection between the resource PricePlan and the resource priceCap
				}
				
				if(!priceComponents.isEmpty())
				{
					for(PriceComponent pc : priceComponents)
					{
						pc.writeToModel(pp,model,baseURI);//we need to pass pp in order to establish a connection between the resource PricePlan and the resource priceCap
					}
				}
				
				owner.addProperty(USDLPriceEnum.HAS_PRICE_PLAN.getProperty(model), pp);//establish a connection between the owner of the PricePlan and the created PricePlan, wich will probably be an instance of the ServiceOffering object
	}
}