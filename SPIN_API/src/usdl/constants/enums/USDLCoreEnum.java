package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

public enum USDLCoreEnum {
	
	OFFERING	(":ServiceOffering",	"C");
	 
	private String property;
	private String type; // C = Class || P = Property
 
	private USDLCoreEnum(String p, String t) {
		property = p;
		type = t;
	}
 
	public String getPropertyString(){
		return Prefixes.USDL_CORE.getName() + property;
	}
	
	public Property getProperty(Model model) {
		if(type.equalsIgnoreCase("P"))
			return model.createProperty(property);
		else{
			return null;
		}
	}

}
