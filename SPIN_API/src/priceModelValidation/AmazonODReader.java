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

package priceModelValidation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.hp.hpl.jena.rdf.model.Model;

import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;
import usdl.servicemodel.Offering;
import usdl.servicemodel.PriceComponent;
import usdl.servicemodel.PriceFunction;
import usdl.servicemodel.PricePlan;
import usdl.servicemodel.QuantitativeValue;
import usdl.servicemodel.Usage;
import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;

public class AmazonODReader {
	
	
	@SuppressWarnings("resource")
	public static void main(String[] args)
	{
		LinkedUSDLModel jmodel;
		try {
			jmodel = LinkedUSDLModelFactory.createFromModel("./DebuggingFiles/amazonOD.ttl");//read a generic model
			//System.out.println(jmodel.toString());
			
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
							for(Usage var : usageVars)//for each usage var, set a value.
							{
								QuantitativeValue val = new QuantitativeValue();

								Scanner scan = new Scanner(System.in);
								System.out.println("Insert the value for the " + var.getName() +" variable.\nDetails: \n"+var.getComment());
								double num = Double.parseDouble(scan.nextLine());
								
								val.setValue(num);
								var.setValue(val);//add the new value to the usage variable 
							}
						}
					}
				}
			}
			jmodel.setBaseURI("http://PricingAPIAmazonODInstance.com");//you define a new base URI for the model or keep the old one
			Model instance = jmodel.WriteToModel();//after we've done our changes in the jmodels, we transform them into a new Semantic model
			//write model to file
			File outputFile = new File("./amazonODInstance.ttl");
			if (!outputFile.exists()) {
	        	outputFile.createNewFile();        	 
	        }

			FileOutputStream out = new FileOutputStream(outputFile);
			instance.write(out, "Turtle");
			out.close();
			
			//after applying the changes to the jmodels and transforming them to a semantic web representation, we can calculate the prices of every offerings.
			for(Offering off : myOfferings)
				System.out.println(""+off.getName()+", Price:"+off.getPricePlan().calculatePrice(instance));

			//jmodel.writeModelToFile("./serviceExamples/test.ttl", "baseURI/de_teste", "TTL");
		} catch (InvalidLinkedUSDLModelException | IOException
				| ReadModelException e) {
			e.printStackTrace();
		}
	}
}

