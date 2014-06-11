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
		return name + "_" + PricingAPIProperties.resourceCounter++;
	}
}
