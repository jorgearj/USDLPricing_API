package usdl.servicemodel;

import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;
import usdl.constants.enums.GREnum;
import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.ResourceNameEnum;
import usdl.constants.enums.USDLCoreEnum;
import usdl.constants.properties.PricingAPIProperties;
import usdl.servicemodel.validations.LinkedUSDLValidator;


public class Service{
	private String name=null;
	//a service can include several other services - current approach may lead to an infinite loop. 
	private ArrayList<Service> includes=null;
	private String comment=null;
	private ArrayList<QuantitativeValue> quantfeatures=null;
	private ArrayList<QualitativeValue> qualfeatures=null;
	private CloudProvider provider=null;
	private String localName = null;
	private String namespace = null;
	private final String resourceType = ResourceNameEnum.SERVICE.getResourceType();
	
	public Service(){
		this(ResourceNameEnum.SERVICE.getResourceName(), null);
	}
	
	public Service(String name){
		this(name, null);
	}
	
	public Service(String name, String nameSpace) {
		quantfeatures = new ArrayList<QuantitativeValue>();
		qualfeatures = new ArrayList<QualitativeValue>();
		includes = new ArrayList<Service>();
		this.setName(name);
		this.namespace = nameSpace;
	}
	
	public Service(Service source)  {//copy constructor

		if(source.getName() != null)
			this.setName(source.getName() +"_" + PricingAPIProperties.resourceCounter++);

		if(source.getComment() != null)
			this.setComment(source.getComment());


		for(Service s : source.getIncludes())
			this.includes.add(new Service(s));

		ArrayList<QuantitativeValue> QuantCopy = new ArrayList<QuantitativeValue>();
		for(QuantitativeValue  qv: source.getQuantfeatures())
			QuantCopy.add(new QuantitativeValue(qv));

		this.setQuantfeatures(QuantCopy);

		ArrayList<QualitativeValue> QualCopy = new ArrayList<QualitativeValue>();
		for(QualitativeValue qval : source.getQualfeatures())
			QualCopy.add(new QualitativeValue(qval));

		this.setQualfeatures(QualCopy);

		if(source.getProvider() != null)
			this.setProvider(new CloudProvider(source.getProvider()));

    }
	
	public String getName() {
		return name;
	}

	public void addQuantitativeFeature(QuantitativeValue qv)
	{
		this.getQuantfeatures().add(qv);
	}
	
	public void addQualitativeFeature(QualitativeValue qv)
	{
		this.getQualfeatures().add(qv);
	}
	
	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			this.name = name;
		}else{
			this.name = ResourceNameEnum.SERVICE.getResourceName();
		}
		this.setLocalName(this.name);
	}

	public ArrayList<Service> getIncludes() {
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
	 * Adds a Service to the list of included Services of the Service.
	 * @param service service to be added.
	 */
	public void addService(Service service)
	{
		this.includes.add(service);
	}
	
	/**
	 * Removes a Service from the list of the Service included services.
	 * @param index Position of the Service that is to be eliminated.
	 */
	public void removeService(int index)
	{
		this.includes.remove(index);
	}
	
	/**
	 * Adds a {@link QualitativeValue} to the list of qualitative features of the {@link Service}.
	 * @param qual QualitativeValue to be added.
	 */
	public void addQualFeature(QualitativeValue qual)
	{
		this.qualfeatures.add(qual);
	}
	
	/**
	 * Removes a {@link QualitativeValue} from the list of the qualitative features of the {@link Service}.
	 * @param index Position of the Service that is to be eliminated.
	 */
	public void removeQualFeature(int index)
	{
		this.qualfeatures.remove(index);
	}
	
	/**
	 * Adds a {@link QuantitativeValue} to the list of qualitative features of the {@link Service}.
	 * @param qual QualitativeValue to be added.
	 */
	public void addQuantFeature(QuantitativeValue quant)
	{
		this.quantfeatures.add(quant);
	}
	
	/**
	 * Removes a {@link QuantitativeValue} from the list of the qualitative features of the {@link Service}.
	 * @param index Position of the Service that is to be eliminated.
	 */
	public void removeQuantFeature(int index)
	{
		this.quantfeatures.remove(index);
	}
	
	/**
	 * Reads a Service object from the Semantic Model. 
	 * @param   resource   The Resource object of the Linked USDL ServiceOffering.
	 * @param   model   Model where the resource is located.
	 * @return  A Service object populated with its information extracted from the Semantic Model.
	 */
	protected static Service readFromModel(Resource resource, Model model){
		Service service = null;
		
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			service = new Service(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());

			//populate the Service
			if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
				service.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
				
			
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
				Service includedService = Service.readFromModel(serv, model);
				if(includedService != null){
					service.addService(includedService); 
				}
			}
			
			//get Qualitative Features
			StmtIterator iterQual = resource.listProperties(GREnum.QUAL_PROD_OR_SERV.getProperty(model));
			while (iterQual.hasNext()) {
				Resource qual = iterQual.next().getResource(); 
				QualitativeValue qualFeat = QualitativeValue.readFromModel(qual, model);
				if(qualFeat != null){
					service.addQualFeature(qualFeat);
				}
			}
			
			//get Quantitative Features
			StmtIterator iterQuant = resource.listProperties(GREnum.QUANT_PROD_OR_SERV.getProperty(model));
			while (iterQuant.hasNext()) {
				Resource quant = iterQuant.next().getResource(); 
				QuantitativeValue quantFeat = QuantitativeValue.readFromModel(quant, model);
				if(quantFeat != null){
					service.addQuantFeature(quantFeat);
				}
			}
		}
		return service;
	}
	
	/**
	 * Creates a Resource representation of the Service instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 * @throws InvalidLinkedUSDLModelException 
	 */
	@SuppressWarnings("null")
	protected void writeToModel(Resource owner, Model model, String baseURI) throws InvalidLinkedUSDLModelException
	{
		Resource service = null;
		

		if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
			this.namespace = baseURI;
		else //use the default baseURI
			this.namespace = PricingAPIProperties.defaultBaseURI;

		
		if(this.localName != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.namespace + this.localName),Prefixes.USDL_CORE.getName()+":"+this.resourceType);
			service = model.createResource(this.namespace + this.localName);
			
			service.addProperty(RDFEnum.RDF_TYPE.getProperty(model), USDLCoreEnum.SERVICE.getResource(model));//rdf type
			
			if(this.name != null)
				service.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name));//label name
			if(this.comment != null)
				service.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.comment)); // a comment
			
			if(this.includes != null)
			{
				if(!this.includes.isEmpty())
				{
					for(Service sv : includes)
					{
						sv.writeToModel(service,model,baseURI);
					}
				}
			}
			
			
			if(!this.quantfeatures.isEmpty())
			{
				for(QuantitativeValue qv : this.quantfeatures)
				{
					qv.writeToModel(service,model,0,baseURI);
				}
			}
			
			if(!this.qualfeatures.isEmpty())
			{
				for(QualitativeValue qv : this.qualfeatures)
				{
					qv.writeToModel(service,model,0,baseURI);
				}
			}
			
			owner.addProperty(USDLCoreEnum.INCLUDES.getProperty(model),service);
		}
		
	}
	
	//TODO: finish validations
	protected void validate() throws InvalidLinkedUSDLModelException{
		this.validateSelfData();
		
		for(Service serv : this.getIncludes()){
			serv.validate();
		}
		
		for(QuantitativeValue quant : this.getQuantfeatures()){
//			quant.validate();
		}
		for(QualitativeValue qual : this.getQualfeatures()){
//			qual.validate();
		}
	}
	
	private void validateSelfData() throws InvalidLinkedUSDLModelException{
		if(this.getName() == null || this.getName().equalsIgnoreCase("")){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_RESOURCE_DATA, new String[]{this.name, "name"});
		}
	}
	
}