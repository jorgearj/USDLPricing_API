package usdl.constants.enums;

import usdl.constants.properties.PricingAPIProperties;

public enum ResourceNameEnum {
	
	OFFERING("ServiceOffering"),
	SERVICE("Service"),
	PRICEPLAN("PricePlan"),
	PRICECOMPONENT("PriceComponent"),
	PRICEFUNCTION("Function"),
	PRICESPEC("PriceSpecification"),
	QUANTVALUE("QuantitativeValue"),
	QUALVALUE("QualitativeValue"),
	CLOUDPROVIDER("BusinessEntity"),
	USAGE("Usage"),
	PROVIDER("Constant");
	
	private String name;
	
	private ResourceNameEnum(String n) {
		name = n;
	}
	
	public String getResourceType(){
		return name;
	}
	
	public String getResourceName() {
		return name + " " + PricingAPIProperties.resourceCounter++;
	}
}
