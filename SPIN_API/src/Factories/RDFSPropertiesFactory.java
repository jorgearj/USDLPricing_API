package Factories;

import usdl.constants.enums.Prefixes;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

public class RDFSPropertiesFactory {
	
	private Property comment;
	private Property label;
	private Property subClassOf;
	public RDFSPropertiesFactory(Model model)
	{
		comment = model.createProperty(Prefixes.RDFS.getName()+"comment");
		label = model.createProperty(Prefixes.RDFS.getName()+"label");
		subClassOf = model.createProperty(Prefixes.RDFS.getName() + "subClassOf");
	}
	
	public Property comment() {return comment;} 
	public Property label() {return label;} 
	public Property subClassOf() {return subClassOf;} 
}