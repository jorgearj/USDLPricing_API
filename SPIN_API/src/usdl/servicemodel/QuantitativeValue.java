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


/**
 * The QuantitativeValue class represents an instance of a QuantitativeValue resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class QuantitativeValue extends Value {
	private double value = -1;
	private double minValue = -1;
	private double maxValue = -1;
	private String unitOfMeasurement = null;
	
	private final String resourceType = ResourceNameEnum.QUANTVALUE.getResourceType();
	
	public QuantitativeValue(){
		this(ResourceNameEnum.QUANTVALUE.getResourceName(), null);
	}
	
	public QuantitativeValue(String name){
		this(name, null);
	}
	
	public QuantitativeValue(String name, String nameSpace) {
		super();
		this.setName(name);
		this.setNamespace(nameSpace);
	}
	
	public QuantitativeValue(QuantitativeValue source)  {//copy constructor

		if(source.getName() != null)
			this.setName(source.getName());
		if(source.getComment() != null)
			this.setComment(source.getComment());

		if(source.getTypes().size() > 0)
		{
			for(String s : source.getTypes())
				this.addType(s);
		}

		if(source.getMaxValue() >= 0)
			this.setMaxValue(source.getMaxValue());

		if(source.getMinValue() >= 0)
			this.setMinValue(source.getMinValue());

		if(source.getUnitOfMeasurement() != null)
			this.setUnitOfMeasurement(source.getUnitOfMeasurement());

		if(source.getValue() >= -1)
			this.setValue(source.getValue());
    }

	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			super.setName(name);
		}else{
			super.setName(ResourceNameEnum.QUANTVALUE.getResourceName());
		}
		super.setLocalName(this.getName());
	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	@Override
	public String toString() {
		return "QuantitativeValue [value=" + value + ", minValue=" + minValue
				+ ", maxValue=" + maxValue + ", unitOfMeasurement="
				+ unitOfMeasurement + ",Name="+this.getName() +"  ,comment=  "+this.getComment() + ",types="+this.getTypes().toString() +"]";
	}
	
//	new functions
	
	/**
	 * Creates a Resource representation of the QuantitativeValue instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 * @throws InvalidLinkedUSDLModelException 
	 */
	protected void writeToModel(Resource owner,Model model,int mode,String baseURI) throws InvalidLinkedUSDLModelException
	{
		Resource qv = null;
		
		if(this.getNamespace() == null){ //no namespace defined for this resource, we need to define one
			if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
				this.setNamespace(baseURI);
			else //use the default baseURI
				this.setNamespace(PricingAPIProperties.defaultBaseURI);
		}
		if(this.getLocalName() != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.getNamespace() + this.getLocalName()));
			qv = model.createResource(this.getNamespace() + this.getLocalName());
			
			qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), GREnum.QUANT_VALUE.getResource(model));//rdf type
			for(String s : this.getTypes()){
				qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(s));//rdf type
			}
	
			if(this.getComment() != null)
				qv.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.getComment()));
			
			if(this.unitOfMeasurement != null)
				qv.addProperty(GREnum.HAS_UNIT_OF_MEASUREMENT.getProperty(model), model.createLiteral(this.unitOfMeasurement));
			
			if(this.maxValue >= 0)
				qv.addLiteral(GREnum.HAS_MAX_VALUE.getProperty(model), this.maxValue);
			
			if(this.minValue >= 0)
				qv.addLiteral(GREnum.HAS_MIN_VALUE.getProperty(model), this.minValue);
			
			if(this.value >= 0)
				qv.addLiteral(GREnum.HAS_VALUE.getProperty(model), this.value);
			
			if(mode == 0)
				owner.addProperty(GREnum.QUANT_PROD_OR_SERV.getProperty(model),qv);
			else if(mode == 1)
				owner.addProperty(USDLPriceEnum.HAS_VALUE.getProperty(model),qv);
			else
				owner.addProperty(USDLPriceEnum.HAS_METRICS.getProperty(model),qv);
		}
	}
	
	/**
	 * Reads a QuantitativeValue object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceComponent.
	 * @param   model   Model where the resource is located.
	 * @return  A QuantitativeValue object populated with its information extracted from the Semantic Model.
	 */
	protected static QuantitativeValue readFromModel(Resource resource,Model model)
	{
		QuantitativeValue val = null;
		
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			val = new QuantitativeValue(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());
			
			if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
				val.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
				
			
			if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
				val.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
			
			if(resource.hasProperty(RDFEnum.RDF_TYPE.getProperty(model)))
			{
				StmtIterator iter = resource.listProperties(RDFEnum.RDF_TYPE.getProperty(model));
				while (iter.hasNext()) {
					String uri = iter.next().getObject().asResource().getURI();
					/*if(uri.toLowerCase().contains("quantitativevalue"))
						continue;
					else*/
					val.addType(uri);
				}
			}
			
			if(resource.hasProperty(GREnum.HAS_UNIT_OF_MEASUREMENT.getProperty(model)))
				val.setUnitOfMeasurement(resource.getProperty(GREnum.HAS_UNIT_OF_MEASUREMENT.getProperty(model)).getString());
			
			if(resource.hasProperty(GREnum.HAS_MAX_VALUE.getProperty(model)))
				val.setMaxValue(resource.getProperty(GREnum.HAS_MAX_VALUE.getProperty(model)).getDouble());
			
			if(resource.hasProperty(GREnum.HAS_MIN_VALUE.getProperty(model)))
				val.setMinValue(resource.getProperty(GREnum.HAS_MIN_VALUE.getProperty(model)).getDouble());
			
			if(resource.hasProperty(GREnum.HAS_VALUE.getProperty(model)))
				val.setValue(resource.getProperty(GREnum.HAS_VALUE.getProperty(model)).getDouble());
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