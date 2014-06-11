 /*  ----------------------------------------------------------------------------------------
 *  This file is part of LinkedUSDLPricingAPI.
 *
 *  LinkedUSDLPricingAPI is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  LinkedUSDLPricingAPI is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with LinkedUSDLPricingAPI.  If not, see <http://www.gnu.org/licenses/>.
 *  ---------------------------------------------------------------------------------------
 */

package usdl.servicemodel;

import java.util.Date;

import usdl.constants.enums.GREnum;
import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.ResourceNameEnum;
import usdl.constants.properties.PricingAPIProperties;
import usdl.servicemodel.validations.LinkedUSDLValidator;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;

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
	private String localName = null;
	private String namespace = null;
	private final String resourceType = ResourceNameEnum.OFFERING.getResourceType();
	

	
	public PriceSpec(){
		this(ResourceNameEnum.PRICESPEC.getResourceName(), null);
	}
	
	public PriceSpec(String name){
		this(name, null);
	}
	
	public PriceSpec(String name, String nameSpace) {
		this.setName(name);
		this.namespace = nameSpace;
	}
	
	public PriceSpec(PriceSpec source)//copy constructor
	{
		if(source.getName() != null)
			this.setName(source.getName() +"_" + PricingAPIProperties.resourceCounter++);

		if(source.getCurrency() != null)
			this.setCurrency(source.getCurrency());

		if(source.getComment() != null)
			this.setComment(source.getComment());

		if(source.getMaxValue() >= 0)
			this.setMaxValue(source.getMaxValue());

		if(source.getMinValue() >= 0)
			this.setMinValue(source.getMinValue());

		if(source.isAddedTaxIncluded())
			this.setAddedTaxIncluded(true);

		if(source.getValue() >= 0)
			this.setValue(source.getValue());

		if(source.getValidFrom() != null)
			this.setValidFrom((Date)source.getValidFrom().clone());

		if(source.getValidThrough() != null)
			this.setValidFrom((Date)source.getValidThrough().clone());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			this.name = name;
		}else{
			this.name = ResourceNameEnum.PRICESPEC.getResourceName();
		}
		this.setLocalName(this.name);
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
	 * @throws InvalidLinkedUSDLModelException 
	 */
	@SuppressWarnings("null")
	protected void writeToModel(Resource owner,Model model,String baseURI) throws InvalidLinkedUSDLModelException
	{
		Resource ps = null;

		if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
			this.namespace = baseURI;
		else if(this.getNamespace() == null)  //use the default baseURI
			this.namespace = PricingAPIProperties.defaultBaseURI;
		
		if(this.localName != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.namespace + this.localName),Prefixes.GR.getName()+":"+this.resourceType);			
			ps = model.createResource(this.namespace + this.localName);
			
			ps.addProperty(RDFEnum.RDF_TYPE.getProperty(model), GREnum.PRICE_SPEC.getResource(model));//rdf type
			
			if(this.name != null)
				ps.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name));//label name
		
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
		
	}
	
	/**
	 * Reads a PriceSpecification object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceSpecification.
	 * @param   model   Model where the resource is located.
	 * @return  A PriceSpec object populated with its information extracted from the Semantic Model.
	 */
	protected static PriceSpec readFromModel(Resource resource,Model model)
	{
		PriceSpec ps = null;
		
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			
			ps = new PriceSpec(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());
			//populate the PricePlan
	
	
			if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
				ps.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
			
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
		}
		return ps;
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
