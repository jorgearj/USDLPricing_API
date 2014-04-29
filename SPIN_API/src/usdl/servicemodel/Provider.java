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

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;

/**
 * The Provider class represents an instance of a Constant resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class Provider extends PriceVariable {
	
	
	private final String resourceType = ResourceNameEnum.PROVIDER.getResourceType();
	
	public Provider(){
		this(ResourceNameEnum.PROVIDER.getResourceName(), null);
	}
	
	public Provider(String name){
		this(name, null);
	}
	
	public Provider(String name, String nameSpace) {
		super();
		this.setName(name);
		this.setNamespace(nameSpace);
	}
	
	public Provider(Provider source) {//copy constructor
		super();
		// TODO Auto-generated constructor stub
		if(source.getName() != null)
			this.setName(source.getName() + PricingAPIProperties.resourceCounter++);

		if(source.getComment() != null)
			this.setComment(source.getComment());

		if(source.getValue() != null)
		{
			if(source.getValue() instanceof QuantitativeValue)
				this.setValue(new QuantitativeValue((QuantitativeValue)source.getValue()));
			else
				this.setValue(new QuantitativeValue((QuantitativeValue)source.getValue()));
		}

	}
	
	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			super.setName(name);
		}else{
			super.setName(ResourceNameEnum.PROVIDER.getResourceName());
		}
		super.setLocalName(this.getName());
	}
	
	/**
	 * Creates a Resource representation of the Provider instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 * @throws InvalidLinkedUSDLModelException 
	 */
	@SuppressWarnings("null")
	public void writeToModel(Resource owner,Model model,String baseURI) throws InvalidLinkedUSDLModelException
	{
		
		// Initialize system functions and templates
		Resource var = null;
		
		if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
			this.setNamespace(baseURI);
		else if(this.getNamespace() == null) //use the default baseURI
			this.setNamespace(PricingAPIProperties.defaultBaseURI);

		if(this.getLocalName() != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.getNamespace() + this.getLocalName()),Prefixes.USDL_PRICE.getName()+":"+this.resourceType);
			var = model.createResource(this.getNamespace() + this.getLocalName());
			
			var.addProperty(RDFEnum.RDF_TYPE.getProperty(model), USDLPriceEnum.PROVIDER.getResource(model));
			
			if(this.getName() != null)
				var.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.getName()));
			
			if(this.getComment() != null){
				var.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.getComment()));
			}
			
			if(this.getValue()!= null){
				if(this.getValue() instanceof QualitativeValue){
					QualitativeValue val = (QualitativeValue) this.getValue();
					val.writeToModel(var,model,1,baseURI);
				}
				else if(this.getValue() instanceof QuantitativeValue){
					QuantitativeValue val = (QuantitativeValue) this.getValue();
					val.writeToModel(var,model,1,baseURI);
				}
			}
			
			owner.addProperty(USDLPriceEnum.HAS_VARIABLE.getProperty(model), var);
		}
	}
	
	
	/**
	 * Reads a Provider object from the Semantic Model. 
	 * @param   resource   The Resource object of the Usage variable.
	 * @param   model   Model where the resource is located.
	 * @return  A Provider object populated with its information extracted from the Semantic Model.
	 */
	protected static Provider readFromModel(Resource resource,Model model)
	{
		Provider var = null;
		
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			
			var = new Provider(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());

			if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
				var.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
			
			if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
				var.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
			
			if(resource.hasProperty(USDLPriceEnum.HAS_VALUE.getProperty(model)))//if the resource has a value
			{
				Resource val = resource.getProperty(USDLPriceEnum.HAS_VALUE.getProperty(model)).getResource();//fetch the resource
				if(val.hasProperty(RDFEnum.RDF_TYPE.getProperty(model)))//if the value has a type
				{
					if(val.getProperty(RDFEnum.RDF_TYPE.getProperty(model)).getResource().getURI().equals(GREnum.QUANT_VALUE.getResource(model).getURI()))//check if the attribute is of the qualitative type
					{
						QuantitativeValue quant = QuantitativeValue.readFromModel(val,model);
						if(quant != null){
							var.setValue(quant);
						}
					}
					else if(val.getProperty(RDFEnum.RDF_TYPE.getProperty(model)).getResource().getURI().equals(GREnum.QUAL_VALUE.getResource(model).getURI()))//check if the attribute is of the qualitative type
					{
						QualitativeValue qual = QualitativeValue.readFromModel(val,model);
						if(qual != null){
							var.setValue(qual);
						}
					}
				}
			}
		}
			
		return var;
	}
	
	@SuppressWarnings("unused")
	protected void validate() throws InvalidLinkedUSDLModelException{
		boolean hasOneService = false;
		
		this.validateSelfData();
		
		if(this.getValue() != null){
			this.getValue().validate();
		}else{
			throw new InvalidLinkedUSDLModelException(ErrorEnum.VARIABLE_WITHOUT_VALUE, new String[]{this.getName(), this.resourceType});
		}
	}
	
	private void validateSelfData() throws InvalidLinkedUSDLModelException{
		if(this.getName() == null || this.getName().equalsIgnoreCase("")){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_RESOURCE_DATA, new String[]{this.getName(), "name"});
		}
	}
}
