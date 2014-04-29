package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import usdl.constants.enums.FOAFEnum;
import usdl.constants.enums.GREnum;
import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.ResourceNameEnum;
import usdl.constants.enums.USDLCoreEnum;
import usdl.constants.properties.PricingAPIProperties;
import usdl.servicemodel.validations.LinkedUSDLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;

public class CloudProvider {
	private String name;
	private String comment;
	private String homepage;
	private List<Service> providedServices;
	private String localName = null;
	private String namespace = null;
	private final String resourceType = ResourceNameEnum.OFFERING.getResourceType();

	
	public CloudProvider(){
		this(ResourceNameEnum.CLOUDPROVIDER.getResourceName(), null);
	}
	
	public CloudProvider(String name){
		this(name, null);
	}
	
	public CloudProvider(String name, String nameSpace) {
		providedServices = new ArrayList<Service>();
		this.setName(name);
		this.namespace = nameSpace;
	}
	
	public CloudProvider(CloudProvider source) {//copy constructor
		super();

		if(source.getName() != null)
			this.setName(source.getName() + PricingAPIProperties.resourceCounter++);

		if(source.getComment() != null)
			this.setComment(source.getComment());

		if(source.getHomepage() != null)
			this.setHomepage(source.getHomepage());

		ArrayList<Service> myCopy = new ArrayList<Service>();
		for(Service s : source.getProvidedServices())
			myCopy.add(new Service(s));

		this.setProvidedServices(myCopy);
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			this.name = name;
		}else{
			this.name = ResourceNameEnum.CLOUDPROVIDER.getResourceName();
		}
		this.setLocalName(this.name);
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}

	

	public String getHomepage() {
		return homepage;
	}


	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}


	public List<Service> getProvidedServices() {
		return providedServices;
	}


	public void setProvidedServices(List<Service> providedServices) {
		this.providedServices = providedServices;
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


	@Override
	public String toString() {
		return "CloudProvider [name=" + name + ", comment=" + comment
				+ ", homepage=" + homepage + ", providedServices="
				+ providedServices + "]";
	}
	
	/**
	 * Reads a Cloudprovider object from the Semantic Model. 
	 * @param   resource   The Resource object of the provider entity semantic representation.
	 * @param   model   Model where the resource is located.
	 * @return  A CloudProvider object populated with its information extracted from the Semantic Model.
	 */
	protected static CloudProvider readFromModel(Resource resource, Model model){
		
		CloudProvider provider = null;
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			
			provider = new CloudProvider(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());
		
		
			if(resource.hasProperty(FOAFEnum.NAME.getProperty(model)))
				provider.setName(resource.getProperty(FOAFEnum.NAME.getProperty(model)).getString());
			else{
				if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
					provider.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
			}
			
			if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
				provider.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
			
			//TODO: Problema a ler os links das homepages: com.hp.hpl.jena.rdf.model.LiteralRequiredException:
			/*if(resource.hasProperty(FOAFEnum.HOMEPAGE.getProperty(model)))
				provider.setHomepage(resource.getProperty(FOAFEnum.HOMEPAGE.getProperty(model)).getLiteral().toString());
			*/
		}
		return provider;
	}
	
	/**
	 * Creates a Resource representation of the CloudProvider instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 * @throws InvalidLinkedUSDLModelException 
	 */
	@SuppressWarnings("null")
	protected void writeToModel(Resource owner,Model model,String baseURI) throws InvalidLinkedUSDLModelException{
		
		Resource provider = null;
		

		if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
			this.namespace = baseURI;
		else if(this.getNamespace() == null)  //use the default baseURI
			this.namespace = PricingAPIProperties.defaultBaseURI;

		
		if(this.localName != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.namespace + this.localName),Prefixes.GR.getName()+":"+this.resourceType);
			provider = model.createResource(this.namespace + this.localName);
			if(this.name != null){
				provider.addProperty(FOAFEnum.NAME.getProperty(model),model.createLiteral(this.name));
			}
			
			provider.addProperty(RDFEnum.RDF_TYPE.getProperty(model), GREnum.BUSINESS_ENTITY.getResource(model));//rdf type
			
			
			owner.addProperty(USDLCoreEnum.HAS_PROVIDER.getProperty(model), provider);
		}
	}
		
		protected void validate() throws InvalidLinkedUSDLModelException{
			
			this.validateSelfData();

		}
		
		private void validateSelfData() throws InvalidLinkedUSDLModelException{
			if(this.getName() == null || this.getName().equalsIgnoreCase("")){
				throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_RESOURCE_DATA, new String[]{this.name, "name"});
			}
		}

	
}