/* Copyright (C) 2013 Jorge Araújo. All rights reserved.
* 
* This program and the accompanying materials are made available under
* the terms of the Common Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/cpl-v10.html
* 
* Id: test.java, Project: CloudAid, 13 Apr 2013 Author: Jorge Araújo
*/
package Examples;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;

public class APIUsageExample{
	
	//prefixes
	private String PRICE= "http://www.linked-usdl.org/ns/usdl-price#";
	private String CORE = "http://www.linked-usdl.org/ns/usdl-core#";
	private String LEGAL= "http://www.linked-usdl.org/ns/usdl-legal#";
	private String RDF  = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private String OWL  = "http://www.w3.org/2002/07/owl#";
	private String DC   = "http://purl.org/dc/elements/1.1/";
	private String XSD  = "http://www.w3.org/2001/XMLSchema#";
	private String VANN = "http://purl.org/vocab/vann/";
	private String FOAF = "http://xmlns.com/foaf/0.1/";
	private String USDK = "http://www.linked-usdl.org/ns/usdl#";
	private String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	private String GR   = "http://purl.org/goodrelations/v1#";
	private String SKOS = "http://www.w3.org/2004/02/skos/core#";
	private String ORG  = "http://www.w3.org/ns/org#";
	private String CLOUD= "http://rdfs.genssiz.org/CloudTaxonomy#";
	private String BASE = "http://rdfs.genssiz.org/serviceSet#";
	private String SP	= "http://spinrdf.org/sp#";
	private String SPIN = "http://spinrdf.org/spin#";
	private String SPL  = "http://spinrdf.org/spl#>";
	
	private final String serviceSetFolder = "./ServiceExamples/";

	private Model serviceSet;
	
	public APIUsageExample(){
		serviceSet = ModelFactory.createDefaultModel();		
	}

	public Model getServiceSet() {
		return this.serviceSet;
	}

	public void setServiceSet(Model serviceSet) {
		this.serviceSet = serviceSet;
	}
	
	public static void main(String[] args) {
		//APIUsageExample test = new APIUsageExample();
		//test.load();
		//test.writeUSDLModeltoFile();
		//test.test();
		
		LinkedUSDLModel model;
		try {
			model = LinkedUSDLModelFactory.createFromModel("./ServiceExamples/");
			System.out.println(model.toString());
			model.writeModelToFile("./serviceExamples/test.ttl", "baseURI/de_teste", "TTL");
		} catch (InvalidLinkedUSDLModelException | IOException
				| ReadModelException e) {
			e.printStackTrace();
		}
		
		//test.setServiceSet(model.createModelFromOfferings());
		//test.writeUSDLModeltoFile();
		//Offering offeringtest = model.getOfferings().get(5);
		//System.out.println("OFFERING: " + offeringtest.getName());
		/*for(Service serv : offeringtest.getIncludes()){
			System.out.println("  - SERVICE: "+serv.getName());
			System.out.println("     - PROVIDER: " + serv.getProvider().toString());
			System.out.println("     - QUALITATIVE: ");
			for(QualitativeFeature qual : serv.getQualfeatures()){
				System.out.println("        - : "+qual.toString());
			}
			System.out.println("     - QUANTITATIVE: ");
			for(QuantitativeFeature quant : serv.getQuantfeatures()){
				System.out.println("        - : "+quant.toString());
			}
			System.out.println("     - SERVICE MODEL: ");
			for(Service servModel : serv.getIncludes()){
				System.out.println("        - : "+servModel.getName());
				System.out.println("        - QUALITATIVE: ");
				for(QualitativeFeature qual : servModel.getQualfeatures()){
					System.out.println("           - : "+qual.toString());
				}
				System.out.println("        - QUANTITATIVE: ");
				for(QuantitativeFeature quant : servModel.getQuantfeatures()){
					System.out.println("           - : "+quant.toString());
				}
			}
		}*/
//		System.out.println(model.toString());
		
	}
	
}
