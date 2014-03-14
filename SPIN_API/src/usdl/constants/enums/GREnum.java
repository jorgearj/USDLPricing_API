package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The GR model Enumeration of properties and classes necessary constants.
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 10
 */
public enum GREnum {
	
	QUAL_PROD_OR_SERV	("qualitativeProductOrServiceProperty",		"P"),
	QUANT_PROD_OR_SERV	("quantitativeProductOrServiceProperty",	"P"),
	BUSINESS_ENTITY		("BusinessEntity",							"C"),
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
		return Prefixes.GR.getName() + property;
	}
	
	/**
	 * Return a ready to use Jena Property 
	 * @param   model  Semantic model where the property is located.
	 * @return   A Jena Property.
	 */
	public Property getProperty(Model model) {
		if(type.equalsIgnoreCase("P")){
			return model.createProperty(Prefixes.GR.getPrefix() + property);
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
