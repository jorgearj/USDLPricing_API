 /*
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
 */

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

import exceptions.InvalidLinkedUSDLModelException;


public class APINewModelExample {
	public static void main(String[] args) throws IOException, InvalidLinkedUSDLModelException {
		
		//Offerings container
		LinkedUSDLModel jmodel;
		jmodel = LinkedUSDLModelFactory.createEmptyModel();
		
		Service serv1 = new Service();// create an empty service
		serv1.setName("Service1");
		serv1.setComment("This is an imaginary service.");
		
		//add data to the qualitative features
		QualitativeValue qualfeat1 = new QualitativeValue();
		QualitativeValue qualfeat2 = new QualitativeValue();
		QualitativeValue qualfeat3 = new QualitativeValue();

		qualfeat1.addType(CLOUDEnum.PERFORMANCE.getConceptURI());//feature type, CloudTaxonomy-> Performance
		qualfeat1.setComment("Feature that specifies the performance of the service");//feature comment
		qualfeat1.setHasLabel("High");//feature value
		serv1.addQualFeature(qualfeat1);//add the feature to the service
		
		qualfeat2.addType(CLOUDEnum.LOCATION.getConceptURI());
		qualfeat2.setComment("Feature that specifies the location of the  service");
		qualfeat2.setHasLabel("Europe");
		serv1.addQualFeature(qualfeat2);
		
		qualfeat3.addType(CLOUDEnum.WINDOWS.getConceptURI());
		qualfeat3.setComment("Feature that specifies the OS of the  service");
		qualfeat3.setHasLabel("Windows Server 2012 R2");
		serv1.addQualFeature(qualfeat3);
		//////////////////////////////////////////////////////////
		//add data to the quantitative features
		
		QuantitativeValue quantfeat1 = new QuantitativeValue();
		QuantitativeValue quantfeat2 = new QuantitativeValue();
		QuantitativeValue quantfeat3 = new QuantitativeValue();
		
		quantfeat1.addType(CLOUDEnum.AVAILABILITY.getConceptURI());//feature type
		quantfeat1.setValue(99);//quantitative value
		quantfeat1.setUnitOfMeasurement("P1");//unit of measurement - P1
		quantfeat1.setComment("Feature that specifies the availability of the service");//feature comment
		serv1.addQuantFeature(quantfeat1);//link the feature to the service
		
		quantfeat2.addType(CLOUDEnum.DISKSIZE.getConceptURI());
		quantfeat2.setValue(400);
		quantfeat1.setUnitOfMeasurement("E34");//unit of measurement - E34
		quantfeat2.setComment("Feature that specifies the disk size of the service");
		serv1.addQuantFeature(quantfeat2);
		
		quantfeat3.addType(CLOUDEnum.MEMORYSIZE.getConceptURI());
		quantfeat3.setValue(6);
		quantfeat1.setUnitOfMeasurement("E34");//unit of measurement - E34
		quantfeat3.setComment("Feature that specifies the RAM size of the service");
		serv1.addQuantFeature(quantfeat3);
		////////////////////////////////////////////////////////////////////////////////
		
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
		pf1.setComment("Price Function to calculate the price of the PriceComponent1");
		pf1.setName("PriceFunction1");
		pc1.setPriceFunction(pf1);
		
		////////////////////////////////////////////////////////////////////////////////
		
		Provider provvar1 = new Provider();//constant variable of the function
		provvar1.setComment("Constant variable of the function1. Specifies the monthly cost of the VM instance.");
		provvar1.setName("serv1mcost");
		QuantitativeValue monthly_cost = new QuantitativeValue();//variable's value
		monthly_cost.setValue(160);//160euros per month for the instance specified above
		monthly_cost.setUnitOfMeasurement("EUR");
		provvar1.setValue(monthly_cost);
		pf1.addProviderVariable(provvar1);//link the variable to the function1
		
		Usage usagevar1 = new Usage();//dynamic  variable of the function - User dependent
		usagevar1.setComment("Usage variable of the function1. Represents the number of months the user will be renting this service");
		usagevar1.setName("serv1nmonths");
		pf1.addUsageVariable(usagevar1);//link the usage variable to the function1
		
		String form = provvar1.getName()+" * "+ usagevar1.getName();
		pf1.setStringFunction(form);//set the string mathematical expression 
		
		////////////////////////////////////////////////////////////////////////////////
		ArrayList<Offering> offerings = new ArrayList<Offering>();
		offerings.add(off1);
		
		jmodel.setOfferings(offerings);//add the created offering to the LinkedUSDLModel instance.
		jmodel.setBaseURI("http://PricingAPISimpleExample.com");
		Model instance = jmodel.WriteToModel();//transform the java models to a semantic representation

		File outputFile = new File("./DebuggingFiles/generic_model.ttl");
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(outputFile);
		instance.write(out, "Turtle");
		out.close();

	}
}
