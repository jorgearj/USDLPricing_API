package Factories;

import usdl.constants.enums.Prefixes;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

public class RDFPropertiesFactory {
	
	private Property type;
	public RDFPropertiesFactory(Model model)
	{
		type = model.createProperty(Prefixes.RDF.getName()+"type");
	}
	
	public Property type(){return type;}
}
