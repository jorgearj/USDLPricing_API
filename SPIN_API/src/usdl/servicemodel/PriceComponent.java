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


import java.util.ArrayList;

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
 * The PriceComponent class represents an instance of a PriceComponent resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
 
public class PriceComponent {
	private String name = null;
	private boolean isDeduction = false;
	private PriceSpec componentCap = null;
	private PriceSpec componentFloor = null;
	private PriceSpec price = null;
	private PriceFunction priceFunction = null;
	private ArrayList<QuantitativeValue> metrics = null;
	private String comment = null;
	private String localName = null;
	private String namespace = null;
	private final String resourceType = ResourceNameEnum.PRICECOMPONENT.getResourceType();
	
	
	public PriceComponent(){
		this(ResourceNameEnum.OFFERING.getResourceName(), null);
	}
	
	public PriceComponent(String name){
		this(name, null);
	}
	public PriceComponent(String name, String nameSpace) {
		metrics = new ArrayList<QuantitativeValue>();
		this.setName(name);
		this.namespace = nameSpace;
	}
	
	public PriceComponent(PriceComponent source) {//copy construct
		super();
		metrics = new ArrayList<QuantitativeValue>();

		if(source.getName() != null)
			this.setName(source.getName() +"_" + PricingAPIProperties.resourceCounter++);

		if(source.getComment() != null)
			this.setComment(source.getComment());

		if(source.getComponentCap() != null)
			this.setComponentCap(new PriceSpec(source.getComponentCap()));

		if(source.getComponentFloor() != null)
			this.setComponentFloor(new PriceSpec(source.getComponentFloor()));

		if(source.getPrice() != null)
			this.setPrice(new PriceSpec(source.getPrice()));

		if(source.getPriceFunction() != null)
			this.setPriceFunction(new PriceFunction(source.getPriceFunction()));

		for(QuantitativeValue met : source.getMetrics())
			this.addMetric(new QuantitativeValue(met));

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			this.name = name;
		}else{
			this.name = ResourceNameEnum.PRICECOMPONENT.getResourceName();
		}
		this.setLocalName(this.name);
	}

	public boolean isDeduction() {
		return isDeduction;
	}

	public void setDeduction(boolean isDeduction) {
		this.isDeduction = isDeduction;
	}

	public PriceSpec getComponentCap() {
		return componentCap;
	}

	public void setComponentCap(PriceSpec componentCap) {
		this.componentCap = componentCap;
	}

	public PriceSpec getComponentFloor() {
		return componentFloor;
	}

	public void setComponentFloor(PriceSpec componentFloor) {
		this.componentFloor = componentFloor;
	}

	public PriceSpec getPrice() {
		return price;
	}

	public void setPrice(PriceSpec price) {
		this.price = price;
	}

	public PriceFunction getPriceFunction() {
		return priceFunction;
	}

	public void setPriceFunction(PriceFunction priceFunction) {
		this.priceFunction = priceFunction;
	}

	public ArrayList<QuantitativeValue> getMetrics() {
		return metrics;
	}

	public void setMetrics(ArrayList<QuantitativeValue> metrics) {
		this.metrics = metrics;
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
		return "PriceComponent [name=" + name + ", isDeduction=" + isDeduction
				+ ", componentCap=" + componentCap + ", componentFloor="
				+ componentFloor + ", price=" + price + ", priceFunction="
				+ priceFunction + ", metrics=" + metrics + ", comment="
				+ comment + "]";
	}

	// New Functions
	/**
	 * Adds a Metric to the PriceComponent
	 * @param   val   Metric related to the PriceComponent
	 */
	public void addMetric(QuantitativeValue val)
	{
		this.metrics.add(val);
	}
	/**
	 * Removes a Metric from the PriceComponent
	 * @param   index   Metric's position
	 */
	public void removeMetricAtIndex(int index)
	{
		this.metrics.remove(index);
	}
	
	/**
	 * Calculates the price value of the PriceComponent
	 * @return   price   PriceSpec instance that contains the value of the price component
	 */
	public PriceSpec calculatePrice()
	{
		PriceSpec price = new PriceSpec();
		//call the function of the component from the model
		return price;
	}
	
	/**
	 * Creates a Resource representation of the PriceComponent instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 * @throws InvalidLinkedUSDLModelException 
	 */

	protected void writeToModel(Resource owner, Model model,String baseURI) throws InvalidLinkedUSDLModelException
	{
		Resource pc = null;
		

		if(baseURI != null ) // the baseURI argument is valid
			if(!baseURI.equalsIgnoreCase(""))
				this.namespace = baseURI;
		else if(this.getNamespace() == null)  //use the default baseURI
			this.namespace = PricingAPIProperties.defaultBaseURI;
		
		if(this.localName != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.namespace + this.localName),Prefixes.USDL_PRICE.getName()+":"+this.resourceType);
			pc = model.createResource(this.namespace + this.localName);
			
			if(this.isDeduction)
				pc.addProperty(RDFEnum.RDF_TYPE.getProperty(model), USDLPriceEnum.DEDUCTION.getResource(model));
			else{
				pc.addProperty(RDFEnum.RDF_TYPE.getProperty(model), USDLPriceEnum.PRICE_COMPONENT.getResource(model));//rdf type
			}
			
			if(this.name != null)
				pc.addProperty(RDFSEnum.LABEL.getProperty(model), model.createLiteral(this.name));//label name
			
			if(this.comment != null)
				pc.addProperty(RDFSEnum.COMMENT.getProperty(model), model.createLiteral(this.comment)); // a comment
			
			if(this.componentCap != null)
				this.componentCap.writeToModel(pc,model,baseURI);
			
			if(this.componentFloor != null)
				this.componentFloor.writeToModel(pc,model,baseURI);
			
			if(this.price != null)
				this.price.writeToModel(pc,model,baseURI);
			
			if(this.priceFunction != null)
				this.priceFunction.writeToModel(pc,model,baseURI);
			
			for(QuantitativeValue metric : this.metrics)
			{
				metric.writeToModel(pc,model,2,baseURI);
			}
				
			owner.addProperty(USDLPriceEnum.HAS_PRICE_COMPONENT.getProperty(model), pc);//link the Price Component with the Price Plan
		}
	}
	
	
	/**
	 * Reads a PriceComponent object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceComponent.
	 * @param   model   Model where the resource is located.
	 * @return  A PriceComponent object populated with its information extracted from the Semantic Model.
	 */
	protected static PriceComponent readFromModel(Resource resource,Model model)
	{
		PriceComponent pc = null;
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			pc = new PriceComponent(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());
			//populate the PriceComponent
	
			//if this condition is not verified the name is already defined to be the resource localname
			if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
				pc.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
			
			if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
				pc.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
			
			if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_CAP.getProperty(model)))//if the resource has a pricecap
			{
				Resource pricecap = resource.getProperty(USDLPriceEnum.HAS_PRICE_CAP.getProperty(model)).getResource();
				PriceSpec priceCap = PriceSpec.readFromModel(pricecap,model);
				if(priceCap != null){
					pc.setComponentCap(priceCap);//read it and add it to the price comp
				}
			}
			
			if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_FLOOR.getProperty(model)))//if the resource has a pricefloor
			{
				Resource pricefloor = resource.getProperty(USDLPriceEnum.HAS_PRICE_FLOOR.getProperty(model)).getResource();
				PriceSpec priceFloor = PriceSpec.readFromModel(pricefloor,model);
				if(priceFloor != null){
					pc.setComponentFloor(priceFloor);//read it and add it to the price comp
				}
			}
			
			if(resource.hasProperty(USDLPriceEnum.HAS_PRICE.getProperty(model)))//if the resource has a price
			{
				Resource price = resource.getProperty(USDLPriceEnum.HAS_PRICE.getProperty(model)).getResource();
				PriceSpec compPrice = PriceSpec.readFromModel(price,model);
				if(compPrice != null){
					pc.setPrice(compPrice);//read it and add it to the price plan
				}
			}
			
			if(resource.hasProperty(RDFEnum.RDF_TYPE.getProperty(model))){
				if(resource.getProperty(RDFEnum.RDF_TYPE.getProperty(model)).getResource().getURI().equals(USDLPriceEnum.DEDUCTION.getResource(model).getURI())){
					pc.setDeduction(true);
				}else{
					pc.setDeduction(false);
				}
			}
				
			if(resource.hasProperty(USDLPriceEnum.HAS_METRICS.getProperty(model)))
			{
				//get metrics
				StmtIterator iter = resource.listProperties(USDLPriceEnum.HAS_METRICS.getProperty(model));
				while (iter.hasNext()) {//while there's price metrics  left
					Resource metric = iter.next().getObject().asResource();
					QuantitativeValue compMetric = QuantitativeValue.readFromModel(metric,model);
					if(compMetric != null){
						pc.addMetric(compMetric);
					}
				}
			}
			
			if(resource.hasProperty(USDLPriceEnum.HAS_PRICE_FUNCTION.getProperty(model)))//if it has a function
			{
				Resource function = resource.getProperty(USDLPriceEnum.HAS_PRICE_FUNCTION.getProperty(model)).getResource();//fetch the resource
				PriceFunction priceFunction = PriceFunction.readFromModel(function,model);
				if(priceFunction != null){
					pc.setPriceFunction(priceFunction);//and read it
				}
			}
				
		}	
		return pc;
	}
	
	protected void validate() throws InvalidLinkedUSDLModelException{
		
		this.validateSelfData();
		
		if(this.price == null && this.priceFunction == null){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.COMPONENT_WITHOUT_PRICE, this.name);
		}else{
			if(this.price != null){
				this.price.validate();
			}
			if(this.priceFunction != null){
//				this.priceFunction.validate();
			}
		}
		
		if(this.componentCap != null){
			this.componentCap.validate();
		}
		if(this.componentFloor != null){
			this.componentFloor.validate();
		}
		
		if(this.getMetrics().size() > 0){
			for(QuantitativeValue metric : this.getMetrics()){
				if(metric != null){
//					metric.validate();
				}else{
					throw new InvalidLinkedUSDLModelException(ErrorEnum.NULL_RESOURCE, new String[]{this.getName(), ResourceNameEnum.QUANTVALUE.getResourceType()});
				}
			}
		}
		
	}
	
	private void validateSelfData() throws InvalidLinkedUSDLModelException{
		if(this.getName() == null || this.getName().equalsIgnoreCase("")){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_RESOURCE_DATA, new String[]{this.name, "name"});
		}
	}
}
