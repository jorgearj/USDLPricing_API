package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

public enum RDFEnum {
	
	RDF_TYPE	(":type",	"P");
	 
	private String property;
	private String type;
 
	private RDFEnum(String p, String t) {
		property = p;
		type = t;
	}
 
	public String getPropertyString(){
		return Prefixes.RDF.getName() + property;
	}
	
	public Property getProperty(Model model) {
		if(type.equalsIgnoreCase("P"))
			return model.createProperty(property);
		else{
			return null;
		}
	}

}
