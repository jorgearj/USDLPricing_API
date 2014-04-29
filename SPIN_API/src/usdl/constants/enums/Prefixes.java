package usdl.constants.enums;

public enum Prefixes {

	USDL_PRICE	("http://www.linked-usdl.org/ns/usdl-price#", 	"price"),
	USDL_CORE 	("http://www.linked-usdl.org/ns/usdl-core#", 	"core"),
	USDL_LEGAL	("http://www.linked-usdl.org/ns/usdl-legal#",	"legal"),
	RDF  		("http://www.w3.org/1999/02/22-rdf-syntax-ns#",	"rdf"),
	OWL  		("http://www.w3.org/2002/07/owl#", 				"owl"),
	DC   		("http://purl.org/dc/elements/1.1/",			"dc"),
	XSD  		("http://www.w3.org/2001/XMLSchema#",			"xsd"),
	VANN 		("http://purl.org/vocab/vann/",					"vann"),
	FOAF 		("http://xmlns.com/foaf/0.1/", 					"foaf"),
	USDK 		("http://www.linked-usdl.org/ns/usdl#",			"usdk"),
	RDFS 		("http://www.w3.org/2000/01/rdf-schema#", 		"rdfs"),
	GR   		("http://purl.org/goodrelations/v1#", 			"gr"),
	SKOS 		("http://www.w3.org/2004/02/skos/core#",		"skos"),
	ORG  		("http://www.w3.org/ns/org#",					"org"),
	CLOUD		("http://rdfs.genssiz.org/CloudTaxonomy#",		"cloudtaxonomy"),
	SP	 		("http://spinrdf.org/sp#",						"sp"),
	SPIN 		("http://spinrdf.org/spin#",					"spin"),
	SPL  		("http://spinrdf.org/spl#",					"spl"),
	PF			("http://jena.hpl.hp.com/ARQ/property#",		"pf");

	private String preffix;
	private String name;
 
	private Prefixes(String p, String n) {
		preffix = p;
		name = n;
	}
 
	public String getPrefix() {
		return preffix;
	}
	
	public String getName(){
		return name;
	}

}
