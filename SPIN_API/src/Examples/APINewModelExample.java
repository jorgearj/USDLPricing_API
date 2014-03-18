package Examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import usdl.constants.enums.CLOUDEnum;
import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;
import usdl.servicemodel.Offering;
import usdl.servicemodel.PriceComponent;
import usdl.servicemodel.PriceFunction;
import usdl.servicemodel.PricePlan;
import usdl.servicemodel.Provider;
import usdl.servicemodel.QualitativeValue;
import usdl.servicemodel.QuantitativeValue;
import usdl.servicemodel.Service;
import usdl.servicemodel.Usage;

import com.hp.hpl.jena.rdf.model.Model;


public class APINewModelExample {
	public static void main(String[] args) throws IOException {

		LinkedUSDLModel jmodel;

		jmodel = LinkedUSDLModelFactory.createEmptyModel();
		Service serv1 = new Service();// create an empty service
		serv1.setName("Service1");
		serv1.setComment("This is the first imaginary service.");
		
		//add data to the qualitative features
		
		QualitativeValue qualfeat1 = new QualitativeValue();
		QualitativeValue qualfeat2 = new QualitativeValue();
		QualitativeValue qualfeat3 = new QualitativeValue();
		
		ArrayList<QualitativeValue> qualfeat = new ArrayList<QualitativeValue>();
		ArrayList<QuantitativeValue> quantfeat = new ArrayList<QuantitativeValue>();
		
		qualfeat1.addType(CLOUDEnum.PERFORMANCE.getConceptURI());
		qualfeat1.setComment("Feature that specifies the performance of the service");
		qualfeat1.setHasLabel("High");
		qualfeat.add(qualfeat1);
		
		qualfeat2.addType(CLOUDEnum.LOCATION.getConceptURI());
		qualfeat2.setComment("Feature that specifies the location of the  service");
		qualfeat2.setHasLabel("Europe");
		qualfeat.add(qualfeat2);
		
		qualfeat3.addType(CLOUDEnum.WINDOWS.getConceptURI());
		qualfeat3.setComment("Feature that specifies the OS of the  service");
		qualfeat3.setHasLabel("Windows");
		qualfeat.add(qualfeat3);
		//////////////////////////////////////////////////////////
		//add data to the quantitative features
		
		QuantitativeValue quantfeat1 = new QuantitativeValue();
		QuantitativeValue quantfeat2 = new QuantitativeValue();
		QuantitativeValue quantfeat3 = new QuantitativeValue();
		
		quantfeat1.addType(CLOUDEnum.AVAILABILITY.getConceptURI());
		quantfeat1.setValue(99);
		quantfeat1.setComment("Feature that specifies the availability of the service");
		quantfeat.add(quantfeat1);
		
		quantfeat2.addType(CLOUDEnum.DISKSIZE.getConceptURI());
		quantfeat2.setValue(400);
		quantfeat2.setComment("Feature that specifies the disk size of the service");
		quantfeat.add(quantfeat2);
		
		quantfeat3.addType(CLOUDEnum.MEMORYSIZE.getConceptURI());
		quantfeat3.setValue(6);
		quantfeat3.setComment("Feature that specifies the RAM size of the service");
		quantfeat.add(quantfeat3);
		////////////////////////////////////////////////////////////////////////////////
		serv1.setQualfeatures(qualfeat);//add the qualitative features to the service
		serv1.setQuantfeatures(quantfeat);//add the quantitative features to the service
		
		////////////////////////////////////////////////////////////////////////////////
		Offering off1 = new Offering();
		
		off1.setComment("Offering that includes the serv1");
		off1.addService(serv1);//link serv1 to the offering
		off1.setName("Offering1");
		////////////////////////////////////////////////////////////////////////////////
		PricePlan pp1 = new PricePlan();//Price plan of the Offering1
		pp1.setComment("PricePlan of the offering1");
		pp1.setName("PricePlan1");
		off1.setPricePlan(pp1);//link the price plan to the offering
		////////////////////////////////////////////////////////////////////////////////
		PriceComponent pc1 = new PriceComponent();//PriceComponent of the PricePlan1
		pc1.setComment("PriceComponent of the PricePlan1");
		pc1.setName("PriceComponent1");
		pp1.addPriceComponent(pc1);//link the price component to the price plan
		////////////////////////////////////////////////////////////////////////////////
		PriceFunction pf1 = new PriceFunction();
		pf1.setComment("Price Function to calculate the price of the PriceComponent1, which in turn will give us the price of the Price Plan. The price of the PricePlan corresponds to the fee that is charged to the customer in exchange for its usage. The price model adopted by the service1 is a fully bundled recurring pre-paid vm access model, where the user pays a fixed fee per month for a pre-defined (by the user) quantity of resources. ");
		pf1.setName("PriceFunction1");
		pc1.setPriceFunction(pf1);
		
		////////////////////////////////////////////////////////////////////////////////
		
		Provider provvar1 = new Provider();//constant variable of the function
		provvar1.setComment("Constant variable of the function1. Specifies the monthly cost of the VM instance.");
		provvar1.setName("serv1mcost");
		QuantitativeValue monthly_cost = new QuantitativeValue();
		monthly_cost.setValue(160);//160euros per month for the instance specified above
		provvar1.setValue(monthly_cost);
		pf1.addProviderVariable(provvar1);//link the variable to the function1
		
		Usage usagevar1 = new Usage();
		usagevar1.setComment("Usage variable of the function1. Represents the number of months the user will be renting this service");
		usagevar1.setName("serv1nmonths");
		pf1.addUsageVariable(usagevar1);//link the usage variable to the function1
		
		pf1.setStringFunction("serv1mcost * serv1nmonths");//Function that calculates the price of the service1. It's a basic mathematical expression where we multiply the number of months by the cost of usage per month.
		
		////////////////////////////////////////////////////////////////////////////////
		ArrayList<Offering> offerings = new ArrayList<Offering>();
		offerings.add(off1);
		
		jmodel.setOfferings(offerings);//add the created offering to the LinkedUSDLModel instance.
		jmodel.setBaseURI("http://PricingAPIGenericModelExample.com");
		Model instance = jmodel.WriteToModel();//transform the java models to a semantic representation

		File outputFile = new File("./generic_model.ttl");
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(outputFile);
		instance.write(out, "Turtle");
		out.close();

		//for (Offering off : jmodel.getOfferings())
			//System.out.println("" + off.getName() + ", Price:"+ off.getPricePlan().calculatePrice(instance));

		// jmodel.writeModelToFile("./serviceExamples/test.ttl",
		// "baseURI/de_teste", "TTL");

	}
}
