/* Copyright (C) 2013 Jorge Araújo. All rights reserved.
* 
 *  ----------------------------------------------------------------------------------------
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
 *
* 
* Id: test.java, Project: CloudAid, 13 Apr 2013 Author: Jorge Araújo
*/
package Examples;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;
import usdl.servicemodel.Offering;
import usdl.servicemodel.PriceComponent;
import usdl.servicemodel.PriceFunction;
import usdl.servicemodel.PricePlan;
import usdl.servicemodel.QuantitativeValue;
import usdl.servicemodel.Usage;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;

public class APIUsageExample{


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

		
		LinkedUSDLModel jmodel;
		try {
			jmodel = LinkedUSDLModelFactory.createFromModel("./ServiceExamples/");//read a generic model
//			System.out.println(jmodel.toString());
//			model.writeModelToFile("./DebuggingFiles/test.ttl", "TTL");
			
			//set variables values
			List<Offering> myOfferings = jmodel.getOfferings();
		
			for(Offering off : myOfferings)//for each offering
			{
				if(off.getPricePlan() != null)//if it has a price plan
				{
					PricePlan pp = off.getPricePlan();
					for(PriceComponent pc : pp.getPriceComponents())//for every component of the priceplan
				{
					if(pc.getPriceFunction() != null)//if it has a price function
					{
						PriceFunction pf = pc.getPriceFunction();

							List<Usage> usageVars = pf.getUsageVariables();//fetch its usage variables

							Collections.sort(usageVars, new Comparator<Usage>() {//sort them alphabetically, this can be included in the API
							    public int compare(Usage s1, Usage s2) {
							        return s1.getName().compareTo(s2.getName());
						    }
							});
							for(Usage var : usageVars)//for each usage var, set a value. In the heroku case, there are two usage variables related to the number of expected hours of usage.
							{
								//var.setName(var.getName().replaceAll(" ", "_"));
								QuantitativeValue val = new QuantitativeValue();
							val.setValue(8760);//365*24 hours -> 1 year
							var.setValue(val);//add the new value to the usage variable 
							System.out.println(var.toString());
						}
					}
				}
			}
			else
				System.out.println("Offering with a null price plan");
		}
		jmodel.setBaseURI("http://rdfs.genssiz.org/herokuv2");
		Model instance = jmodel.WriteToModel();//after we've done our changes in the jmodels, we transform them into a new Semantic model
		
		//write model to file
		/*File outputFile = new File("C:/Users/daniel/Desktop/model.ttl");
	if (!outputFile.exists()) {
       	outputFile.createNewFile();        	 
      }

		FileOutputStream out = new FileOutputStream(outputFile);
		instance.write(out, "Turtle");
		out.close();*/
		
		//after applying the changes to the jmodels and transforming them to a semantic web representation, we can calculate the prices of every offerings.
			for(Offering off : myOfferings)
			System.out.println(""+off.getName()+", Price:"+off.getPricePlan().calculatePrice(instance));

			jmodel.writeModelToFile("./DebuggingFiles/test.ttl", "TTL");
		} catch (InvalidLinkedUSDLModelException | IOException
				| ReadModelException e) {
			e.printStackTrace();
		}
	}
	
}
