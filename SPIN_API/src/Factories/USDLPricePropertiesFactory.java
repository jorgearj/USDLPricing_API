package Factories;

import usdl.constants.enums.Prefixes;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

public class USDLPricePropertiesFactory {

	private Property hasPricePlan;
	private Property hasPriceCap;
	private Property hasPriceFloor;
	private Property hasPrice;
	private Property hasComponentCap;
	private Property hasComponentFloor;
	private Property hasMetrics;
	private Property hasPriceComponent;
	private Property hasPriceFunction;
	private Property hasVariable;
	private Property hasValue;

	public USDLPricePropertiesFactory(Model model) {

		hasPricePlan = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasPricePlan");
		hasPriceCap = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasPriceCap");
		hasPriceFloor = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasPriceFloor");
		hasPrice = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasPrice");
		hasComponentCap = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasComponentCap");
		hasComponentFloor = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasComponentFloor");
		hasMetrics = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasMetrics");
		hasPriceComponent = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasPriceComponent");
		hasPriceFunction = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasPriceFunction");
		hasVariable = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasVariable");
		hasValue = model.createProperty(Prefixes.USDL_PRICE.getPrefix()+"hasValue");
	}
	
	public Property hasPricePlan(){return this.hasPricePlan;}
	public Property hasPriceCap(){return this.hasPriceCap;}
	public Property hasPriceFloor(){return this.hasPriceFloor;}
	public Property hasPrice(){return this.hasPrice;}
	public Property hasComponentCap(){return this.hasComponentCap;}
	public Property hasComponentFloor(){return this.hasComponentFloor;}
	public Property hasMetrics(){return this.hasMetrics;}
	public Property hasPriceComponent(){return this.hasPriceComponent;}
	public Property hasPriceFunction(){return this.hasPriceFunction;}
	public Property hasVariable(){return this.hasVariable;}
	public Property hasValue(){return this.hasValue;}
}
