package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import usdl.constants.enums.GREnum;
import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLCoreEnum;


public class Service {
	private String name=null;
	//a service can include several other services - current approach may lead to an infinite loop. 
	private ArrayList<Service> includes=null;
	private String comment=null;
	private ArrayList<QuantitativeValue> quantfeatures=null;
	private ArrayList<QualitativeValue> qualfeatures=null;
	private CloudProvider provider=null;
	
	public Service() {
		super();
		 quantfeatures = new ArrayList<QuantitativeValue>();
		 qualfeatures = new ArrayList<QualitativeValue>();
		 includes = new ArrayList<Service>();
	}
	
	public String getName() {
		return name;
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Service> getIncludes() {
		return includes;
	}

	public void setIncludes(ArrayList<Service> services) {
		this.includes = services;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
	public CloudProvider getProvider() {
		return provider;
	}

	public void setProvider(CloudProvider provider) {
		this.provider = provider;
	}

	@Override
	public String toString() {
		return "Service [name=" + name + ", includes=" + includes
				+ ", comment=" + comment + ", quantfeatures=" + quantfeatures
				+ ", qualfeatures=" + qualfeatures + ", provider=" + provider
				+ "]";
	}

	public ArrayList<QuantitativeValue> getQuantfeatures() {
		return quantfeatures;
	}

	public void setQuantfeatures(ArrayList<QuantitativeValue> quantfeatures) {
		this.quantfeatures = quantfeatures;
	}

	public ArrayList<QualitativeValue> getQualfeatures() {
		return qualfeatures;
	}

	public void setQualfeatures(ArrayList<QualitativeValue> qualfeatures) {
		this.qualfeatures = qualfeatures;
	}
	
	/**
	 * Reads a Service object from the Semantic Model. 
	 * @param   resource   The Resource object of the Linked USDL ServiceOffering.
	 * @param   model   Model where the resource is located.
	 * @return  A Service object populated with its information extracted from the Semantic Model.
	 */
	public static Service readFromModel(Resource resource, Model model){
		Service service = new Service();
		ArrayList<Service> services = new ArrayList<Service>();
		ArrayList<QuantitativeValue> quantitativeFeatures = new ArrayList<QuantitativeValue>();
		ArrayList<QualitativeValue> qualitativeFeatures = new ArrayList<QualitativeValue>();
		
		
		//populate the Offering
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			service.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		else
			service.setName(resource.getLocalName());
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			service.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		if(resource.hasProperty(USDLCoreEnum.HAS_PROVIDER.getProperty(model))){
			Resource provider = resource.getProperty(USDLCoreEnum.HAS_PROVIDER.getProperty(model)).getResource();
			service.setProvider(CloudProvider.readFromModel(provider, model));
		}
		
		//get the included services/ServiceModels
		StmtIterator iterServ = resource.listProperties(USDLCoreEnum.HAS_SERVICE_MODEL.getProperty(model));
		while (iterServ.hasNext()) {
			Resource serv = iterServ.next().getResource(); 
			services.add(Service.readFromModel(serv, model)); 
		}
		service.setIncludes(services);
		
		//get Qualitative Features
		StmtIterator iterQual = resource.listProperties(GREnum.QUAL_PROD_OR_SERV.getProperty(model));
		while (iterQual.hasNext()) {
			Resource qual = iterQual.next().getResource(); 
			qualitativeFeatures.add(QualitativeValue.readFromModel(qual, model)); 
		}
		service.setQualfeatures(qualitativeFeatures);
		
		//get Quantitative Features
		StmtIterator iterQuant = resource.listProperties(GREnum.QUANT_PROD_OR_SERV.getProperty(model));
		while (iterQuant.hasNext()) {
			Resource quant = iterQuant.next().getResource(); 
			quantitativeFeatures.add(QuantitativeValue.readFromModel(quant, model)); 
		}
		service.setQuantfeatures(quantitativeFeatures);
		
		return service;
	}
	
	/**
	 * Creates a Resource representation of the Service instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
		Resource service = null;
		if(this.name != null)
			service = model.createResource(Prefixes.BASE.getName() + this.name + "_" + System.currentTimeMillis());
		else
			service = model.createResource(Prefixes.BASE.getName() +"Service" + "_" + System.currentTimeMillis());
		
		service.addProperty(RDFEnum.RDF_TYPE.getProperty(model), USDLCoreEnum.SERVICE.getResource(model));//rdf type
		
		if(this.name != null)
			service.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name));//label name
		if(this.comment != null)
			service.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.comment)); // a comment
		
		if(!this.includes.isEmpty())
		{
			for(Service sv : includes)
			{
				sv.writeToModel(service,model);
			}
		}
		
		if(!this.quantfeatures.isEmpty())
		{
			for(QuantitativeValue qv : this.quantfeatures)
			{
				qv.writeToModel(service,model,0);
			}
		}
		
		if(!this.qualfeatures.isEmpty())
		{
			for(QualitativeValue qv : this.qualfeatures)
			{
				qv.writeToModel(service,model,0);
			}
		}
		
		owner.addProperty(USDLCoreEnum.INCLUDES.getProperty(model),service);
	}
	
}