package usdl.constants.enums;

import usdl.constants.properties.PricingAPIProperties;

public enum ResourceNameEnum {
	
	OFFERING("Service Offering"),
	SERVICE("Service"),
	PRICEPLAN("Price Plan"),
	PRICECOMPONENT("Price Component");
	
	private String name;
	
	private ResourceNameEnum(String n) {
		name = n;
	}

	public String getResourceName() {
		return name + " " + PricingAPIProperties.resourceCounter++;
	}
}
