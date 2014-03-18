package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The USDL Price model Enumeration of properties and classes necessary constants.
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 10
 */
public enum USDLPriceEnum {
	
	HAS_PRICE_PLAN	("hasPricePlan",	"P"),
	HAS_PRICE_COMPONENT	("hasPriceComponent",	"P"),
	HAS_PRICE_CAP ("hasPriceCap",	"P"),
	HAS_PRICE_FLOOR ("hasPriceFloor",	"P"),
	HAS_PRICE ("hasPrice",	"P"),
	HAS_COMPONENT_FLOOR ("hasComponentFloor","P"),
	HAS_COMPONENT_CAP ("hasComponentCap","P"),
	HAS_METRICS ("hasMetrics","P"),
	HAS_PRICE_FUNCTION ("hasPriceFunction","P"),
	HAS_VARIABLE ("hasVariable","P"),
	HAS_VALUE ("hasValue","P");

	private String property;
	private String type; // C = Class || P = Property
 
	/**
	 * USDLPriceEnum Enumerator constructor. 
	 * @param   p   String of the property.
	 * @param   t   type of the concept. Either "C" for classes or "P" for properties.
	 */
	private USDLPriceEnum(String p, String t) {
		property = p;
		type = t;
	}
 
	/**
	 * Return the string of the enumerator element. 
	 * @return   A String of the property with its prefix.
	 */
	public String getPropertyString(){
		return Prefixes.USDL_PRICE.getName() + ":" + property;
	}
	
	/**
	 * Return a ready to use Jena Property 
	 * @param   model  Semantic model where the property is located.
	 * @return   A Jena Property.
	 */
	public Property getProperty(Model model) {
		if(type.equalsIgnoreCase("P")){
			return model.createProperty(Prefixes.USDL_PRICE.getPrefix()  + property);
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
			return model.createResource(Prefixes.USDL_PRICE.getPrefix()  + property);
		}else{
			return null;
		}
	}

}
