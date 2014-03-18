package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * The FOAF Enumeration of properties and classes necessary constants.
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 10
 */
public enum FOAFEnum {
	
	HOMEPAGE		("homepage",	"P"),
	NAME 			("name",		"P");
	 
	private String property;
	private String type; // C = Class || P = Property
 
	/**
	 * FOAF Enumerator constructor. 
	 * @param   p   String of the property.
	 * @param   t   type of the concept. Either "C" for classes or "P" for properties.
	 */
	private FOAFEnum(String p, String t) {
		property = p;
		type = t;
	}
 
	/**
	 * Return the string of the enumerator element. 
	 * @return   A String of the property with its prefix.
	 */
	public String getPropertyString(){
		return Prefixes.FOAF.getName() + ":" + property;
	}
	
	/**
	 * Return a ready to use Jena Property 
	 * @param   model  Semantic model where the property is located.
	 * @return   A Jena Property.
	 */
	public Property getProperty(Model model) {
		if(type.equalsIgnoreCase("P")){
			return model.createProperty(Prefixes.FOAF.getPrefix() + property);
		}else{
			return null;
		}
	}

}
