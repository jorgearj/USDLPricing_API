package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public enum RDFEnum {
	
	RDF_TYPE	("type",	"P");
	 
	private String property;
	private String type;
 
	private RDFEnum(String p, String t) {
		property = p;
		type = t;
	}
 
	public String getPropertyString(){
		return Prefixes.RDF.getName() + ":" + property;
	}
	
	public Property getProperty(Model model) {
		if(type.equalsIgnoreCase("P"))
			return model.createProperty(Prefixes.RDF.getPrefix() + ":" + property);
		else{
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
			return model.createResource(Prefixes.RDF.getPrefix() + ":" + property);
		}else{
			return null;
		}
	}

}
