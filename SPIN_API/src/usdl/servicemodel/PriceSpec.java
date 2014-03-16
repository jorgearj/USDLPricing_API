package usdl.servicemodel;

import java.util.Date;

import usdl.constants.enums.GREnum;
import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The Usage class represents an instance of a PriceSpec resource of the GR model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class PriceSpec {
	private String name = null; //
	private String currency = null;//
	private double value = -1;//
	private double maxValue = -1;
	private double minValue = -1;
	private boolean addedTaxIncluded = false; //
	private Date validFrom = null;
	private Date validThrough = null;
	private String comment = null;//
	

	public PriceSpec() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public boolean isAddedTaxIncluded() {
		return addedTaxIncluded;
	}

	public void setAddedTaxIncluded(boolean addedTaxIncluded) {
		this.addedTaxIncluded = addedTaxIncluded;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidThrough() {
		return validThrough;
	}

	public void setValidThrough(Date validThrough) {
		this.validThrough = validThrough;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "PriceSpec [name=" + name + ", currency=" + currency
				+ ", value=" + value + ", maxValue=" + maxValue + ", minValue="
				+ minValue + ", addedTaxIncluded=" + addedTaxIncluded
				+ ", validFrom=" + validFrom + ", validThrough=" + validThrough
				+ ", comment=" + comment + "]";
	}
	
//	new functions
	
	/**
	 * Creates a Resource representation of the PriceSpec instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
		Resource ps = null;
		if(this.name != null)
		{
			ps = model.createResource(Prefixes.BASE.getPrefix() + this.name.replaceAll(" ", "_") +"_"+ System.nanoTime());
			ps.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.GR.getPrefix() + "PriceSpecification"));//rdf type
			ps.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name.replaceAll(" ", "_")));//label name
		}
		else
		{
			ps = model.createResource(Prefixes.BASE.getPrefix() + "PriceSpecification"+"_"+ System.nanoTime());
			ps.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.GR.getPrefix() + "PriceSpecification"));//rdf type
		}
		
		if(this.comment != null)
			ps.addProperty(RDFSEnum.COMMENT.getProperty(model), this.comment);
		
		ps.addLiteral(GREnum.VALUE_ADDED_TAX_INCLUDED.getProperty(model), this.addedTaxIncluded);
		
		if(this.currency != null)
			 ps.addProperty(GREnum.HAS_CURRENCY.getProperty(model), this.currency);
		
		if(this.value >=0)
			ps.addLiteral(GREnum.HAS_CURRENCY_VALUE.getProperty(model), this.value);
		
		if(this.maxValue >=0)
			ps.addLiteral(GREnum.HAS_MAX_CURRENCY_VALUE.getProperty(model), this.maxValue);
		
		if(this.minValue >=0)
			ps.addLiteral(GREnum.HAS_MIN_CURRENCY_VALUE.getProperty(model), this.minValue);
		
		if(this.validFrom != null)
			ps.addProperty(GREnum.VALID_FROM.getProperty(model), model.createTypedLiteral(this.validFrom,XSDDatatype.XSDdate));
		
		if(this.validThrough != null)
			ps.addProperty(GREnum.VALID_THROUGH.getProperty(model), model.createTypedLiteral(this.validThrough,XSDDatatype.XSDdate));
		
		owner.addProperty(GREnum.HAS_PRICE_SPECIFICATION.getProperty(model), ps);
	}
	
	/**
	 * Reads a PriceSpecification object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceSpecification.
	 * @param   model   Model where the resource is located.
	 * @return  A PriceSpec object populated with its information extracted from the Semantic Model.
	 */
	public static PriceSpec readFromModel(Resource resource,Model model)
	{
		PriceSpec ps = new PriceSpec();
		//populate the PricePlan
		
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			ps.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		else
			ps.setName(resource.getLocalName());
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			ps.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		if(resource.hasProperty(GREnum.VALUE_ADDED_TAX_INCLUDED.getProperty(model)))
			ps.setAddedTaxIncluded( resource.getProperty(GREnum.VALUE_ADDED_TAX_INCLUDED.getProperty(model)).getBoolean() );
		
		if(resource.hasProperty(GREnum.HAS_CURRENCY.getProperty(model)))
			ps.setCurrency(resource.getProperty(GREnum.HAS_CURRENCY.getProperty(model)).getString());
		
		if(resource.hasProperty(GREnum.HAS_CURRENCY_VALUE.getProperty(model)))
				ps.setValue(resource.getProperty(GREnum.HAS_CURRENCY_VALUE.getProperty(model)).getDouble());
		
		if(resource.hasProperty(GREnum.HAS_MAX_CURRENCY_VALUE.getProperty(model)))
			ps.setMaxValue(resource.getProperty(GREnum.HAS_MAX_CURRENCY_VALUE.getProperty(model)).getDouble());
		
		if(resource.hasProperty(GREnum.HAS_MIN_CURRENCY_VALUE.getProperty(model)))
			ps.setMinValue(resource.getProperty(GREnum.HAS_MIN_CURRENCY_VALUE.getProperty(model)).getDouble());
	
		if(resource.hasProperty(GREnum.VALID_FROM.getProperty(model)))
		{
			Resource temp = resource.getProperty(GREnum.VALID_FROM.getProperty(model)).getResource();
			ps.setValidFrom(((XSDDateTime)temp).asCalendar().getTime());
		}
		
		if(resource.hasProperty(GREnum.VALID_THROUGH.getProperty(model)))
		{
			Resource temp = resource.getProperty(GREnum.VALID_THROUGH.getProperty(model)).getResource();
			ps.setValidThrough(((XSDDateTime)temp).asCalendar().getTime());
		}
		return ps;
	}

	
	
}