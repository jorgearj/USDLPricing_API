
package priceModelValidation;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;
import usdl.servicemodel.Offering;
import usdl.servicemodel.PriceComponent;
import usdl.servicemodel.PriceFunction;
import usdl.servicemodel.PricePlan;
import usdl.servicemodel.QuantitativeValue;
import usdl.servicemodel.Usage;

import com.hp.hpl.jena.rdf.model.Model;

import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;

public class AmazonSIReader {
	
	
	@SuppressWarnings("resource")
	public static void main(String[] args)
	{
		LinkedUSDLModel jmodel;
		try {
			jmodel = LinkedUSDLModelFactory.createFromModel("./DebuggingFiles/amazonSI.ttl");//read a generic model
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
							//System.out.println("Off: "+off.getName());
							//System.out.println(pf.getSPARQLFunction());
							
							List<Usage> usageVars = pf.getUsageVariables();//fetch its usage variables
							Collections.sort(usageVars, new Comparator<Usage>() {//sort them alphabetically, this can be included in the API
							    public int compare(Usage s1, Usage s2) {
							        return s1.getName().compareTo(s2.getName());
							    }
							});
							
							for(Usage var : usageVars)//for each usage var, set a value.
							{
								QuantitativeValue val = new QuantitativeValue();

								Scanner scan = new Scanner(System.in);
								System.out.println("Insert the value for the " + var.getName().replaceAll("TIME\\d+.*", "") +" variable.\nDetails: \n"+var.getComment());
								double num = Double.parseDouble(scan.nextLine());
								
								val.setValue(num);//1month
								var.setValue(val);//add the new value to the usage variable 
							}
						}
					}
				}
				else
					System.out.println("Offering with a null price plan");
			}
			jmodel.setBaseURI("http://PricingAPIAmazonRIInstance.com");
			Model instance = jmodel.WriteToModel();//after we've done our changes in the jmodels, we transform them into a new Semantic model
			
			//write model to file
			/*File outputFile = new File("your_directory/amazonRIInstance.ttl");
			if (!outputFile.exists()) {
	        	outputFile.createNewFile();        	 
	        }

			FileOutputStream out = new FileOutputStream(outputFile);
			instance.write(out, "Turtle");
			out.close();*/
			
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


