package usdl.servicemodel;

import java.util.ArrayList;

import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.ResourceNameEnum;
import usdl.constants.enums.USDLCoreEnum;
import usdl.constants.enums.USDLPriceEnum;
import usdl.constants.properties.PricingAPIProperties;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Offering {
	private String name = null;
	private ArrayList<Service> includes = null;
	private PricePlan pricePlan = null;
	private String comment = null;
	private String localName = null;
	private String namespace = null;
	
	public Offering(){
		this(ResourceNameEnum.OFFERING.getResourceName());
	}
	
	public Offering(String name){
		super();
		this.includes = new ArrayList<>();
		this.name = name;
		this.localName = name.replaceAll(" ", "_");
	}
	
	public Offering(String name, String nameSpace) {
		this(name);
		this.namespace = nameSpace;
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
	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Reads an Offering object from the Semantic Model. 
	 * @param   resource   The Resource object of the Linked USDL ServiceOffering.
	 * @param   model   Model where the resource is located.
	 * @return  An Offering object populated with its information extracted from the Semantic Model.
	 */
	public static Offering readFromModel(Resource resource, Model model){
		Offering offering = new Offering(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());
		ArrayList<Service> services = new ArrayList<Service>();
		//populate the Offering
		
		//if this condition is not verified the name is already defined to be the resource localname
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			offering.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			offering.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		//get included services
		StmtIterator iter = resource.listProperties(USDLCoreEnum.INCLUDES.getProperty(model));
		while (iter.hasNext()) {
			Resource service = iter.next().getResource();			
			services.add(Service.readFromModel(service, model));
		}
		offering.setIncludes(services);

//		if (resource.hasProperty(USDLPriceEnum.HAS_PRICE_PLAN.getProperty(model))) {
//			Resource pp = resource.getProperty(USDLPriceEnum.HAS_PRICE_PLAN.getProperty(model)).getResource();
//			offering.setPricePlan(PricePlan.readFromModel(pp, model));
//		}
		return offering;
	}
	
	/**
	 * Creates a Resource representation of the Offering instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Model model,String baseURI)
	{
		Resource offering = null;
		
		if(this.namespace == null){ //no namespace defined for this resource, we need to define one
			if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
				this.namespace = baseURI;
			else //use the default baseURI
				this.namespace = PricingAPIProperties.defaultBaseURI;
		}
		
		if(this.name != null)
			offering = model.createResource(this.namespace + this.localName);
		
		offering.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.USDL_CORE.getPrefix() +"ServiceOffering" ));//rdf type
		
		if(this.name != null)
			offering.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name));//label name
		
		if(this.comment != null)
			offering.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.comment)); // a comment
		
		if(!includes.isEmpty()){
			for(Service service : includes)
			{
				service.writeToModel(offering, model,baseURI);
			}
		}
		
		if(this.pricePlan != null)
			pricePlan.writeToModel(offering, model,baseURI);
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
		String result = "	- " + this.name + "\n"+
						"		- " + this.localName +"\n"+
						"		- " + this.namespace +"\n"+
						"		- " + this.comment +"\n"+
						"		- SERVICES:\n";
		for(Service service : this.includes){
			result = result + 
						"			- " + service.toString() + "\n"; 
		}
		if(this.pricePlan != null){
			result = result +
						"		- PRICE PLAN: \n" + this.pricePlan.toString();
		}
		return result;
	}

	
}