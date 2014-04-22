package usdl.servicemodel;

import java.util.ArrayList;

import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.ResourceNameEnum;
import usdl.constants.enums.USDLCoreEnum;
import usdl.constants.enums.USDLPriceEnum;
import usdl.constants.properties.PricingAPIProperties;
import usdl.servicemodel.validations.LinkedUSDLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;

public class Offering {
	private String name = null;
	private ArrayList<Service> includes = null;
	private PricePlan pricePlan = null;
	private String comment = null;
	private String localName = null;
	private String namespace = null;
	private final String resourceType = ResourceNameEnum.OFFERING.getResourceType();
	
	public Offering(){
		this(ResourceNameEnum.OFFERING.getResourceName(), null);
	}
	
	public Offering(String name){
		this(name, null);
	}
	
	public Offering(String name, String nameSpace) {
		this.includes = new ArrayList<>();
		this.setName(name);
		this.namespace = nameSpace;
	}
	
	public Offering(Offering source) {//copy constructor
		super();

		if(source.getName() != null)
			this.setName(source.getName());

		if(source.getComment() != null)
			this.setComment(source.getComment());

		if(source.getIncludes().size() > 0)
		{
			ArrayList<Service> myIncludeCopy = new ArrayList<Service>();
			for(Service s : source.getIncludes())
				myIncludeCopy.add(new Service(s));

			this.setIncludes(myIncludeCopy);
		}

		if(source.getPricePlan() != null)
			this.setPricePlan(new PricePlan(source.getPricePlan()));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			this.name = name;
		}else{
			this.name = ResourceNameEnum.OFFERING.getResourceName();
		}
		this.setLocalName(this.name);
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
		this.localName = localName.replaceAll(" ", "_");
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
	 * @throws InvalidLinkedUSDLModelException 
	 */
	protected static Offering readFromModel(Resource resource, Model model){
		Offering offering = null;
		
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			
			offering = new Offering(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());
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
				Service serv = Service.readFromModel(service, model);
				if(serv != null){
					offering.addService(serv);
				}
			}

			if (resource.hasProperty(USDLPriceEnum.HAS_PRICE_PLAN.getProperty(model))) {
				Resource pp = resource.getProperty(USDLPriceEnum.HAS_PRICE_PLAN.getProperty(model)).getResource();
				PricePlan pricePlan = PricePlan.readFromModel(pp, model);
				if(pricePlan != null){
					offering.setPricePlan(pricePlan);
				}
			}
		}
		
		return offering;
	}
	
	/**
	 * Creates a Resource representation of the Offering instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 * @throws InvalidLinkedUSDLModelException 
	 */
	protected void writeToModel(Model model, String baseURI) throws InvalidLinkedUSDLModelException
	{
		Resource offering = null;
		
		if(this.namespace == null){ //no namespace defined for this resource, we need to define one
			if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
				this.namespace = baseURI;
			else //use the default baseURI
				this.namespace = PricingAPIProperties.defaultBaseURI;
		}
		
		if(this.localName != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.namespace + this.localName));
			offering = model.createResource(this.namespace + this.localName);
			
			offering.addProperty(RDFEnum.RDF_TYPE.getProperty(model), USDLCoreEnum.OFFERING.getResource(model));//rdf type
			
			if(this.name != null)
				offering.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name));//label name
			
			if(this.comment != null)
				offering.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.comment)); // a comment
			
			for(Service service : includes)
			{
				if(service != null){
					service.writeToModel(offering, model, baseURI);
				}
			}
			
			if(this.pricePlan != null)
				pricePlan.writeToModel(offering, model,baseURI);
		}
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
	
	
	protected void validate() throws InvalidLinkedUSDLModelException{
		boolean hasOneService = false;
		
		this.validateSelfData();
		
		if(this.getIncludes().size() > 0){
			for(Service serv : this.getIncludes()){
				if(serv != null){
					serv.validate();
					hasOneService = true;
				}else{
					throw new InvalidLinkedUSDLModelException(ErrorEnum.NULL_RESOURCE, new String[]{this.getName(), ResourceNameEnum.SERVICE.getResourceType()});
				}
			}
		}else{
			throw new InvalidLinkedUSDLModelException(ErrorEnum.OFFERING_WITHOUT_SERVICE, this.getName());
		}
		
		if(!hasOneService){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.OFFERING_WITHOUT_SERVICE, this.getName());
		}
		
		if(this.getPricePlan() != null){
			this.getPricePlan().validate();
		}else{
			throw new InvalidLinkedUSDLModelException(ErrorEnum.OFFERING_WITHOUT_PRICEPLAN, this.getName());
		}
	}
	
	private void validateSelfData() throws InvalidLinkedUSDLModelException{
		if(this.getName() == null || this.getName().equalsIgnoreCase("")){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_RESOURCE_DATA, new String[]{this.name, "name"});
		}
	}
	
	@Override
	public String toString() {
		String result = "- " + this.name + "\n"+
						"	- " + this.localName +"\n"+
						"	- " + this.namespace +"\n"+
						"	- " + this.comment +"\n"+
						"	- SERVICES:\n";
		for(Service service : this.includes){
			result = result + 
						"		- " + service.toString() + "\n"; 
		}
		if(this.pricePlan != null){
			result = result +
						"	- PRICE PLAN: \n" + this.pricePlan.toString();
		}
		return result;
	}

	
}