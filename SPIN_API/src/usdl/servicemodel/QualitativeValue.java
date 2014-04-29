package usdl.servicemodel;

import usdl.constants.enums.GREnum;
import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.ResourceNameEnum;
import usdl.constants.enums.USDLPriceEnum;
import usdl.constants.properties.PricingAPIProperties;
import usdl.servicemodel.validations.LinkedUSDLValidator;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;
//http://www.heppnetz.de/ontologies/goodrelations/v1.html#QualitativeValue

/**
 * The QualitativeValue class represents an instance of a QualitativeValue resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class QualitativeValue extends Value {
	private String hasLabel = null;
	
	private final String resourceType = ResourceNameEnum.QUALVALUE.getResourceType();
	
	public QualitativeValue(){
		this(ResourceNameEnum.QUALVALUE.getResourceName(), null);
	}
	
	public QualitativeValue(String name){
		this(name, null);
	}
	
	public QualitativeValue(String name, String nameSpace) {
		super();
		this.setName(name);
		this.setNamespace(nameSpace);
	}

	public QualitativeValue(QualitativeValue source)  {//copy constructor

		if(source.getName() != null)
			this.setName(source.getName() + PricingAPIProperties.resourceCounter++);

		if(source.getComment() != null)
			this.setComment(source.getComment());

		if(source.getTypes().size() > 0)
		{
			for(String s : source.getTypes())
				this.addType(s);
		}
		if(source.getHasValue() != null)
			this.setHasLabel(source.getHasValue());
    }
	
	public String getHasValue() {
		return hasLabel;
	}

	public void setHasLabel(String label) {
		this.hasLabel = label;
	}

	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			super.setName(name);
		}else{
			super.setName(ResourceNameEnum.QUALVALUE.getResourceName());
		}
		super.setLocalName(this.getName());
	}
	
	@Override
	public String toString() {
		return "QualitativeValue [hasLabel=" + hasLabel + ",types ="+this.getTypes().toString()+ ",comment ="+this.getComment()+"]" ;
	}
	
	//New functions
	
	/**
	 * Creates a Resource representation of the QualitativeValue instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 * @throws InvalidLinkedUSDLModelException 
	 */
	@SuppressWarnings("null")
	protected void writeToModel(Resource owner,Model model,int mode,String baseURI) throws InvalidLinkedUSDLModelException
	{
		Resource qv = null;
		

		if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
			this.setNamespace(baseURI);
		else if(this.getNamespace() == null)  //use the default baseURI
			this.setNamespace(PricingAPIProperties.defaultBaseURI);

		if(this.getLocalName() != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.getNamespace() + this.getLocalName()),Prefixes.GR.getName()+":"+this.resourceType);
			qv = model.createResource(this.getNamespace() + this.getLocalName());
			
			qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), GREnum.QUAL_VALUE.getResource(model));//rdf type
			for(String s : this.getTypes())
			{
				qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(s));//rdf type
			}
			
			if(this.getComment() != null)
				qv.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.getComment()));
			
			if(this.getHasValue() != null)
				qv.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.getHasValue()));
			
			if(mode == 0)
				owner.addProperty(GREnum.QUAL_PROD_OR_SERV.getProperty(model),qv);
			else
				owner.addProperty(USDLPriceEnum.HAS_VALUE.getProperty(model),qv);
		}
	}
	
	/**
	 * Reads a QualitativeValue object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceComponent.
	 * @param   model   Model where the resource is located.
	 * @return  A QualitativeValue object populated with its information extracted from the Semantic Model.
	 */
	protected static QualitativeValue readFromModel(Resource resource,Model model)
	{
		QualitativeValue val = null;
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			val = new QualitativeValue(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());
			
			if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
				val.setHasLabel((resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString()));
			
			if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
				val.setComment((resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString()));
			
			if(resource.hasProperty(RDFEnum.RDF_TYPE.getProperty(model)))
			{
				StmtIterator iter = resource.listProperties(RDFEnum.RDF_TYPE.getProperty(model));
				while (iter.hasNext()) {
					String uri = iter.next().getObject().asResource().getURI();
					if(uri.toLowerCase().contains("qualitativevalue"))
						continue;
					else
						val.addType(uri );
				}
			}
		}
		
		return val;
	}
	
	protected void validate() throws InvalidLinkedUSDLModelException{
		super.validate();
		this.validateSelfData();

	}
	
	private void validateSelfData() throws InvalidLinkedUSDLModelException{
		if(this.getName() == null || this.getName().equalsIgnoreCase("")){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_RESOURCE_DATA, new String[]{this.getName(), "name"});
		}
	}
}