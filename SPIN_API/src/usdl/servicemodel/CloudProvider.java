package usdl.servicemodel;

import java.util.List;

import usdl.constants.enums.FOAFEnum;
import usdl.constants.enums.GREnum;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLCoreEnum;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class CloudProvider {
	private String name;
	private String comment;
	private String homepage;
	private List<Service> providedServices;

	
	public CloudProvider() {
		super();
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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
	public static CloudProvider readFromModel(Resource resource, Model model){
		
		CloudProvider provider = new CloudProvider();
		
		
		//populate the Offering
		if(resource.hasProperty(FOAFEnum.NAME.getProperty(model)))
			provider.setName(resource.getProperty(FOAFEnum.NAME.getProperty(model)).getString());
		else{
			if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
				provider.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
			else
				provider.setName(resource.getLocalName());
		}
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			provider.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		//TODO: Problema a ler os links das homepages: com.hp.hpl.jena.rdf.model.LiteralRequiredException:
		/*if(resource.hasProperty(FOAFEnum.HOMEPAGE.getProperty(model)))
			provider.setHomepage(resource.getProperty(FOAFEnum.HOMEPAGE.getProperty(model)).getLiteral().toString());
		*/
		return provider;
	}
	
	/**
	 * Creates a Resource representation of the CloudProvider instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
		Resource provider = GREnum.BUSINESS_ENTITY.getResource(model);
		provider.addProperty(RDFEnum.RDF_TYPE.getProperty(model), GREnum.BUSINESS_ENTITY.getResource(model));//rdf type
		//TODO: terminar a escrita do provider
	}

	
}