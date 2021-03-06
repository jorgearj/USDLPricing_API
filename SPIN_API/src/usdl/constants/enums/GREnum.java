package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The GR model Enumeration of properties and classes necessary constants.
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 10
 * 
 * 
 *  *  ----------------------------------------------------------------------------------------
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
public enum GREnum {
	
	QUAL_PROD_OR_SERV	("qualitativeProductOrServiceProperty",		"P"),
	QUANT_PROD_OR_SERV	("quantitativeProductOrServiceProperty",	"P"),
	BUSINESS_ENTITY		("BusinessEntity",							"C"),
	PRICE_SPEC		("PriceSpecification",							"C"),
	QUANT_VALUE		("QuantitativeValue",							"C"),
	QUAL_VALUE		("QuantitativeValue",							"C"),
	VALUE_ADDED_TAX_INCLUDED ("valueAddedTaxIncluded",				"P"),
	HAS_CURRENCY ("hasCurrency","P"),
	HAS_CURRENCY_VALUE ("hasCurrencyValue","P"),
	HAS_MIN_CURRENCY_VALUE ("hasMinCurrencyValue", "P") ,
	HAS_MAX_CURRENCY_VALUE ("hasMaxCurrencyValue","P"),
	VALID_FROM ("validFrom","P"),
	VALID_THROUGH ("validThrough","P"),
	HAS_UNIT_OF_MEASUREMENT ("hasUnitOfMeasurement","P"),
	HAS_MIN_VALUE ("hasMinValue","P"),
	HAS_MAX_VALUE ("hasMaxValue","P"),
	HAS_VALUE ("hasValue","P"),
	HAS_PRICE_SPECIFICATION ("hasPriceSpecification","P");
	private String property;
	private String type; // C = Class || P = Property
 
	/**
	 * GR Enumerator constructor. 
	 * @param   p   String of the property.
	 * @param   t   type of the concept. Either "C" for classes or "P" for properties.
	 */
	private GREnum(String p, String t) {
		property = p;
		type = t;
	}
 
	/**
	 * Return the string of the enumerator element. 
	 * @return   A String of the property with its prefix.
	 */
	public String getPropertyString(){
		return Prefixes.GR.getName() + ":" + property;
	}
	
	/**
	 * Return a ready to use Jena Property 
	 * @param   model  Semantic model where the property is located.
	 * @return   A Jena Property.
	 */
	public Property getProperty(Model model) {
		if(type.equalsIgnoreCase("P")){
			return model.createProperty(Prefixes.GR.getPrefix()  + property);
		}else{
			return null;
		}
	}
	
	/**
	 * Return a ready to use Jena Resource of the Enumerator concept
	 * @param   model  Semantic model where the property is located.
	 * @return   A Jena Resource.
	 */
	public Resource getResource(Model model) {
		if(type.equalsIgnoreCase("C")){
			return model.createResource(Prefixes.GR.getPrefix() + property);
		}else{
			return null;
		}
	}

}
