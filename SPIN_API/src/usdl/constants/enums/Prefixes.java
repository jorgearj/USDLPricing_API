 /*  ----------------------------------------------------------------------------------------
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
