package usdl.servicemodel;

import java.util.ArrayList;

import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLCoreEnum;
import usdl.constants.enums.USDLPriceEnum;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Offering {
	private String name = null;
	private ArrayList<Service> includes = null;
	private PricePlan pricePlan = null;
	private String comment = null;
	
	public Offering() {
		super();
		this.includes = new ArrayList<>();
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
	
	/**
	 * Reads an Offering object from the Semantic Model. 
	 * @param   resource   The Resource object of the Linked USDL ServiceOffering.
	 * @param   model   Model where the resource is located.
	 * @return  An Offering object populated with its information extracted from the Semantic Model.
	 */
	public static Offering readFromModel(Resource resource, Model model){
		Offering offering = new Offering();
		ArrayList<Service> services = new ArrayList<Service>();
		
		//populate the Offering
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			offering.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		else
			offering.setName(resource.getLocalName());
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			offering.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		//get included services
		StmtIterator iter = resource.listProperties(USDLCoreEnum.INCLUDES.getProperty(model));
		while (iter.hasNext()) {
			Resource service = iter.next().getResource();			
			services.add(Service.readFromModel(service, model));
		}
		offering.setIncludes(services);

		if (resource.hasProperty(USDLPriceEnum.HAS_PRICE_PLAN.getProperty(model))) {
			Resource pp = resource.getProperty(USDLPriceEnum.HAS_PRICE_PLAN.getProperty(model)).getResource();
			offering.setPricePlan(PricePlan.readFromModel(pp, model));
		}
		return offering;
	}
	
	/**
	 * Creates a Resource representation of the Offering instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Model model)
	{
		Resource offering = null;
		if(this.name != null)
			offering = model.createResource(Prefixes.BASE.getPrefix() + this.name.replaceAll(" ", "_") + "_" + System.nanoTime());
		else
			offering = model.createResource(Prefixes.BASE.getPrefix() +"ServiceOffering" + "_" + System.nanoTime());
		
		offering.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.USDL_CORE.getPrefix() +"ServiceOffering" ));//rdf type
		
		if(this.name != null)
			offering.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name.replaceAll(" ", "_")));//label name
		
		if(this.comment != null)
			offering.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.comment)); // a comment
		
		if(!includes.isEmpty()){
			for(Service service : includes)
			{
				service.writeToModel(offering, model);
			}
		}
		
		if(this.pricePlan != null)
			pricePlan.writeToModel(offering, model);
	}
	
	
	/**
	 * Adds a Service to the list of included Services of the ServiceOffering.
	 * @param service service to be added.
	 */
	public void addService(Service service)
	{
		this.includes.add(service);
	}
	
	/**
	 * Removes a Service from the list of ServiceOffering included services.
	 * @param index Position of the Service that is to be eliminated.
	 */
	public void removeService(int index)
	{
		this.includes.remove(index);
	}
	
	@Override
	public String toString() {
		return "Offering [name=" + name + ", includes=" + includes
				+ ", pricePlan=" + pricePlan + ", comment=" + comment + "]";
	}

	
}