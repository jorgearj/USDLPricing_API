package usdl.constants.enums;

import usdl.constants.properties.PricingAPIProperties;

public enum ResourceNameEnum {
	
	OFFERING("Service Offering");
	
	private String name;
	
	private ResourceNameEnum(String n) {
		name = n;
	}

	public String getResourceName() {
		return name + " " + PricingAPIProperties.resourceCounter++;
	}
}
