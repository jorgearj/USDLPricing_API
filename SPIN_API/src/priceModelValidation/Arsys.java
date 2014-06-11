/**
 * @author Daniel Guedes Barrigas - danielgbarrigas@hotmail.com / danielgbarrigas@gmail.com
 * 
 * Uses the LinkedUSDL Pricing API to model Service Offerings provided by Arsys.
 * Info about their offerings can be seen at: http://www.arsys.net/servers/dedicated.html
 * The pricing method adopted by arsys is a fully bundled Recurring VM Access
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
 */

package priceModelValidation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;

import exceptions.InvalidLinkedUSDLModelException;
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




public class Arsys {
	public static void main(String[] args) throws IOException, InvalidLinkedUSDLModelException {
		LinkedUSDLModel jmodel;

		jmodel = LinkedUSDLModelFactory.createEmptyModel();
		ArsysOffering(jmodel);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private static void ArsysOffering(LinkedUSDLModel jmodel) throws IOException, InvalidLinkedUSDLModelException {
		//first, create the services 
		Service s1 = new Service();

		s1.setName("ServerS2");//give it a name, in this case, Server S2 is the name of an offering provided by arsys
		ArrayList<QuantitativeValue> s1QuantFeat = new ArrayList<QuantitativeValue>();//container for the Quantitative Features
		ArrayList<QualitativeValue> s1QualFeat = new ArrayList<QualitativeValue>();//container for the Qualitative Features
		
		//add features to the service
		QuantitativeValue DATAINEXTERNAL = new QuantitativeValue();
		QuantitativeValue DATAININTERNAL  = new QuantitativeValue();
		QuantitativeValue DATAOUTEXTERNAL  = new QuantitativeValue();
		QuantitativeValue DATAOUTINTERNAL  = new QuantitativeValue();
		
		DATAINEXTERNAL.addType(CLOUDEnum.DATAINEXTERNAL.getConceptURI());
		DATAINEXTERNAL.setValue(Double.MAX_VALUE);//unlimited
		s1QuantFeat.add(DATAINEXTERNAL);
		
		DATAININTERNAL.addType(CLOUDEnum.DATAININTERNAL.getConceptURI());
		DATAININTERNAL.setValue(Double.MAX_VALUE);//unlimited
		s1QuantFeat.add(DATAININTERNAL);
		
		DATAOUTEXTERNAL.addType(CLOUDEnum.DATAOUTEXTERNAL.getConceptURI());
		DATAOUTEXTERNAL.setValue(Double.MAX_VALUE);//unlimited
		s1QuantFeat.add(DATAOUTEXTERNAL);
		
		DATAOUTINTERNAL.addType(CLOUDEnum.DATAOUTINTERNAL.getConceptURI());
		DATAOUTINTERNAL.setValue(Double.MAX_VALUE);//unlimited
		s1QuantFeat.add(DATAOUTINTERNAL);
		
		QuantitativeValue TRANSFERRATE  = new QuantitativeValue();
		TRANSFERRATE.addType(CLOUDEnum.TRANSFERRATE.getConceptURI());
		TRANSFERRATE.setValue(100);
		TRANSFERRATE.setUnitOfMeasurement("4L");//Mbps
		s1QuantFeat.add(TRANSFERRATE);
		
		
		QuantitativeValue CPUCores=null,CPUSpeed=null;
		CPUCores =new QuantitativeValue();
		CPUSpeed = new QuantitativeValue();
		
		CPUCores.addType(CLOUDEnum.CPUCORES.getConceptURI());
		CPUCores.setValue(2);
	
		CPUSpeed.addType(CLOUDEnum.CPUSPEED.getConceptURI());
		CPUSpeed.setValue(1.87);
		CPUSpeed.setUnitOfMeasurement("A86");//Ghz
		s1QuantFeat.add(CPUCores);
		s1QuantFeat.add(CPUSpeed);
		
		// /MEMORYSIZE
		QuantitativeValue MemorySize =  new QuantitativeValue();
		MemorySize.addType(CLOUDEnum.MEMORYSIZE.getConceptURI());
		MemorySize.setValue(2);
		MemorySize.setUnitOfMeasurement("E34");//GB
		s1QuantFeat.add(MemorySize);

		//DiskSize, StorageType
		QuantitativeValue DiskSize = new QuantitativeValue();
		QualitativeValue StorageType = new QualitativeValue();
		

		DiskSize.addType(CLOUDEnum.DISKSIZE.getConceptURI());
		DiskSize.setValue(320);
		DiskSize.setUnitOfMeasurement("E34");//GB
		
		StorageType.addType(CLOUDEnum.STORAGETYPE.getConceptURI());
		StorageType.setHasLabel("SATA");
		s1QuantFeat.add(DiskSize);
		s1QualFeat.add(StorageType);
		
		//Feature
		QualitativeValue feature_installation_launch= new QualitativeValue();
		feature_installation_launch.addType(CLOUDEnum.FEATURE.getConceptURI());
		feature_installation_launch.setComment("Time it takes for the instance to be available for consumption.");
		feature_installation_launch.setHasLabel("In hours.");
		s1QualFeat.add(feature_installation_launch);
		//MONITORING
		QualitativeValue monitoring = new QualitativeValue();
		monitoring.addType(CLOUDEnum.MONITORING.getConceptURI());
		monitoring.setHasLabel("Basic");
		monitoring.setComment(" servicio gratuito que monitoriza el estado (encendido/apagado) del servidor y los recursos CPU, RAM y transferencia. A trav�s del panel recibir� avisos cuando se produzca una alarma y en el caso de encendido/apagado del servidor tambi�n recibir� notificaci�n por mail. En el panel podr� ver el estado actual de cada uno de los recursos, un apartado de gr�ficas con la evoluci�n temporal y un hist�rico de los eventos generados durante el �ltimo mes. Los umbrales de generaci�n de alarmas no son editables y se han definido como: Warning Critical ");
		s1QualFeat.add(monitoring);
		//Programming languagues, PHP 5, Perl, Python
		QualitativeValue Language = new QualitativeValue();
		Language.addType(CLOUDEnum.LANGUAGE.getConceptURI());
		Language.setHasLabel("PHP 5");
		s1QualFeat.add(Language);
		
		Language = new QualitativeValue();
		Language.addType(CLOUDEnum.LANGUAGE.getConceptURI());
		Language.setHasLabel("Perl");
		s1QualFeat.add(Language);
		
		Language = new QualitativeValue();
		Language.addType(CLOUDEnum.LANGUAGE.getConceptURI());
		Language.setHasLabel("Python");
		s1QualFeat.add(Language);
		
		
		//Platform
		QualitativeValue Platform = new QualitativeValue();
		Platform.addType(CLOUDEnum.PLATFORM.getConceptURI());
		Platform.setHasLabel("MySQL 5");
		s1QualFeat.add(Platform);
		
		Platform = new QualitativeValue();
		Platform.addType(CLOUDEnum.PLATFORM.getConceptURI());
		Platform.setHasLabel("SQL Server");
		s1QualFeat.add(Platform);
		
		//This ends the base offering however, we still miss the OS of the service. The basic available OS is CentOS which is free but, we can choose other OS from the available list in exchange for an aditional cost to the final price of the offering
		
		//First, lets create the offering for the created service
		//offering for the basic ServerS2 VM. Basic means that this VM includes the CentOS operating system which has no additional charge to the consumer. There are 4 other OS available to the consumer but, each has an additional fee to the price of the VM.
		Offering of1 = new Offering();
		Offering of2 = new Offering();
		Offering of3 = new Offering();

		
		//since there's another 4 possible OS system, means that the created server actually gives birth to 5 possible offerings instead of one:
		
		//s1 includes  CentOS
		Service s2 = new Service(s1);//s2 includes Red Hat Enterprise Linux 6
		ArrayList<QuantitativeValue> s2QuantFeat = (ArrayList<QuantitativeValue>) s1QuantFeat.clone();//container for the Quantitative Features
		ArrayList<QualitativeValue> s2QualFeat = (ArrayList<QualitativeValue>) s1QualFeat.clone();//container for the Qualitative Features
		Service s3 =new Service( s1);//s3 includes Windows Server R2 2008 Web Ed.
		ArrayList<QuantitativeValue> s3QuantFeat = (ArrayList<QuantitativeValue>) s1QuantFeat.clone();//container for the Quantitative Features
		ArrayList<QualitativeValue> s3QualFeat = (ArrayList<QualitativeValue>) s1QualFeat.clone();//container for the Qualitative Features
		//every new service is the same as the s1 but with the difference that each possesses a different OS
		
		//add the respective OS to the services and their management method
		
		QualitativeValue OS = new QualitativeValue();
		OS.addType(CLOUDEnum.UNIX.getConceptURI());
		OS.setComment("Operating System");
		OS.setHasLabel("CentOS 6");
		s1QualFeat.add(OS);
		
		OS = new QualitativeValue();
		OS.addType(CLOUDEnum.UNIX.getConceptURI());
		OS.setComment("Operating System");
		OS.setHasLabel(" Red Hat Enterprise Linux 6");
		s2QualFeat.add(OS);
		
		OS = new QualitativeValue();
		OS.addType(CLOUDEnum.WINDOWS.getConceptURI());
		OS.setComment("Operating System");
		OS.setHasLabel("Windows Server R2 2008 Web Ed.");
		s3QualFeat.add(OS);
		
		///////////////////////////
		
		//add their management control methods, all offerings can be managed via WEB or, depending on their OS, via Console or GUI
		
		QualitativeValue console , gui , web ;
		console=new QualitativeValue();
		gui=new QualitativeValue();
		web=new QualitativeValue();
		web.addType(CLOUDEnum.WEB.getConceptURI());
		web.setHasLabel("Server Control Panel");
		s1QualFeat.add(web);
		s2QualFeat.add(web);
		s3QualFeat.add(web);

		
		console.addType(CLOUDEnum.CONSOLE.getConceptURI());
		console.setHasLabel("SSH");
		s1QualFeat.add(console);
		s2QualFeat.add(console);
		
		
		gui.addType(CLOUDEnum.GUI.getConceptURI());
		gui.setHasLabel("Remote Desktop");
		s3QualFeat.add(gui);

		
		/////////////////////
		of1.setComment("Basic Offering of the Service 1 (ServerS2)");
		of1.setName("CentOSServerS2");
		of1.addService(s1);
		
		of2.setComment("Offering of the Service 2 (ServerS2)");
		of2.setName("REDHATServerS2");
		of2.addService(s2);
		
		of3.setComment("Offering of the Service 3 (ServerS2)");
		of3.setName("Windows2008WEBServerS2");
		of3.addService(s3);

		
		/////////////////////////////////////////////////////////////////
		//elaborate the price plan of offering1
		PricePlan pp1 = new PricePlan();
		of1.setPricePlan(pp1);//link the priceplan to the offering
		
		pp1.setComment("PricePlan of the Service1");
		pp1.setName("PricePlan1");
		
		//create the PriceComponents of the priceplan
		PriceComponent pc1 = new PriceComponent();
		pp1.addPriceComponent(pc1);//add the price component to the price plan
		pc1.setComment("PriceComponent1 of the PricePlan1");
		
		//create the price function of the component
		PriceFunction pf1 = new PriceFunction();
		pc1.setPriceFunction(pf1);//link the price function to the price component
		
		pf1.setComment("PriceFunction of the PriceComponent1");
		pf1.setStringFunction("standards2 * standards2months");//VM price * number of months that the VM will be used
		
		//create the variables used by the function!
		Provider standards2 = new Provider();
		Usage standards2months = new Usage();
		
		standards2months.setName("standards2months");
		
		standards2.setName("standards2");
		standards2.setComment("price of the VM per month");
		
		QuantitativeValue ss2price = new QuantitativeValue();//constant value set by the provider
		ss2price.setValue(125);
		ss2price.setUnitOfMeasurement("EUR");
		
		standards2.setValue(ss2price);
		
		standards2months.setComment("Number of months that you'll be using the VM.");
		
		pf1.addUsageVariable(standards2months);//link the variables to the function
		pf1.addProviderVariable(standards2);//link the variables to the function
		
		////////////////////////////////////////////END of the Specification of the s1 PricePlan/////////////////////////////////////////////

		// ///////////////////////////////////////////////////////////////
		// elaborate the price plan of offering2
		PricePlan pp2 = new PricePlan();
		of2.setPricePlan(pp2);// link the priceplan to the offering

		pp2.setComment("PricePlan of the Service2");
		pp2.setName("PricePlan2");

		// create the PriceComponents of the priceplan2 - Here, we have 2 Price Components, one is the same as the standard VM while the other specifies the additional cost of a different OS
		PriceComponent pc2 = new PriceComponent();
		pp2.addPriceComponent(pc2);// add the price component to the price plan
		pc2.setComment("PriceComponent2 of the PricePlan2");
		
		PriceComponent pc2_b = new PriceComponent();
		pp2.addPriceComponent(pc2_b);// add the price component to the price plan
		pc2_b.setComment("PriceComponent2_b of the PricePlan2");
		
		// create the price function of the component
		PriceFunction pf2 = new PriceFunction();
		pc2.setPriceFunction(pf2);// link the price function to the price
									// component

		pf2.setComment("PriceFunction of the PriceComponent2");
		pf2.setStringFunction("redhats2 * redhats2months");// VM price *
																// number of
																// months that
																// the VM will
																// be used

		// create the variables used by the function!
		Provider redhats2 = new Provider();
		Usage redhats2months = new Usage();
		
		redhats2months.setName("redhats2months");
		redhats2months.setComment("Number of months that you'll be using the VM.");
		
		redhats2.setName("redhats2");
		redhats2.setComment("price of the VM per month");

		QuantitativeValue redhats2price = new QuantitativeValue();// constant value
																// set by the
																// provider
		redhats2price.setValue(125);
		redhats2price.setUnitOfMeasurement("EUR");

		redhats2.setValue(redhats2price);


		pf2.addUsageVariable(redhats2months);// link the variables to the
												// function
		pf2.addProviderVariable(redhats2);// link the variables to the
											// function
		
		
		
		//specification of the PriceComponentB
		
		// create the price function of the component
		PriceFunction pf2_b = new PriceFunction();
		pc2_b.setPriceFunction(pf2_b);// link the price function to the price
										// component

		pf2_b.setComment("PriceFunction of the PriceComponent2_b");
		pf2_b.setStringFunction("redhatprice * redhats2monthsb");// VM price *
																	// number of
																	// months
																	// that
																	// the VM
																	// will
																	// be used

		// create the variables used by the function!
		Provider redhatprice = new Provider();
		Usage redhats2monthsb = new Usage();

		redhats2monthsb.setName("redhats2monthsb");
		redhats2monthsb
				.setComment("Number of months that you'll be using the VM.");

		redhatprice.setName("redhatprice");
		redhatprice.setComment("price of the Red Hat Enterprise OS per month");

		QuantitativeValue redhatpricevalue = new QuantitativeValue();// constant
																		// value
		// set by the
		// provider
		redhatpricevalue.setValue(25);
		redhatpricevalue.setUnitOfMeasurement("EUR");

		redhatprice.setValue(redhatpricevalue);

		pf2_b.addUsageVariable(redhats2monthsb);// link the variables to the
												// function
		pf2_b.addProviderVariable(redhatprice);// link the variables to the
												// function
		
		// //////////////////////////////////////////END of the Specification of the s2 PricePlan/////////////////////////////////////////////
		
		
		// ///////////////////////////////////////////////////////////////
		// elaborate the price plan of offering3
		PricePlan pp3 = new PricePlan();
		of3.setPricePlan(pp3);// link the priceplan to the offering

		pp3.setComment("PricePlan of the Service3");
		pp3.setName("PricePlan3");

		// create the PriceComponents of the priceplan3 - Here, we have 2 Price
		// Components, one is the same as the standard VM while the other
		// specifies the additional cost of a different OS
		PriceComponent pc3 = new PriceComponent();
		pp3.addPriceComponent(pc3);// add the price component to the price plan
		pc3.setComment("PriceComponent3 of the PricePlan3");

		PriceComponent pc3_b = new PriceComponent();
		pp3.addPriceComponent(pc3_b);// add the price component to the price
										// plan
		pc3_b.setComment("PriceComponen3_b of the PricePlan3");

		// create the price function of the component
		PriceFunction pf3 = new PriceFunction();
		pc3.setPriceFunction(pf3);// link the price function to the price
									// component

		pf3.setComment("PriceFunction of the PriceComponent3");
		pf3.setStringFunction("windowswebs2 * windowswebs2months");// VM price *
																	// number of
																	// months
																	// that
																	// the VM
																	// will
																	// be used

		// create the variables used by the function!
		Provider windowswebs2 = new Provider();
		Usage windowswebs2months = new Usage();

		windowswebs2months.setName("windowswebs2months");
		windowswebs2months
				.setComment("Number of months that you'll be using the VM.");

		windowswebs2.setName("windowswebs2");
		windowswebs2.setComment("price of the VM per month");

		QuantitativeValue windowswebs2price = new QuantitativeValue();// constant
																		// value
		// set by the
		// provider
		windowswebs2price.setValue(125);
		windowswebs2price.setUnitOfMeasurement("EUR");

		windowswebs2.setValue(windowswebs2price);

		pf3.addUsageVariable(windowswebs2months);// link the variables to the
													// function
		pf3.addProviderVariable(windowswebs2);// link the variables to the
												// function

		// specification of the PriceComponentB

		// create the price function of the component
		PriceFunction pf3_b = new PriceFunction();
		pc3_b.setPriceFunction(pf3_b);// link the price function to the price
										// component

		pf3_b.setComment("PriceFunction of the PriceComponent3_b");
		pf3_b.setStringFunction("windowswebprice * windowswebs2monthsb");// VM
																			// price
																			// *
		// number of
		// months
		// that
		// the VM
		// will
		// be used

		// create the variables used by the function!
		Provider windowswebprice = new Provider();
		Usage windowswebs2monthsb = new Usage();

		windowswebs2monthsb.setName("windowswebs2monthsb");
		windowswebs2monthsb
				.setComment("Number of months that you'll be using the VM.");

		windowswebprice.setName("windowswebprice");
		windowswebprice
				.setComment("price of the Windows Server R2 2008 Web Ed. OS per month");

		QuantitativeValue windowswebpricevalue = new QuantitativeValue();// constant
																			// value
		// set by the
		// provider
		windowswebpricevalue.setValue(15);
		windowswebpricevalue.setUnitOfMeasurement("EUR");

		windowswebprice.setValue(windowswebpricevalue);

		pf3_b.addUsageVariable(windowswebs2monthsb);// link the variables to the
												// function
		pf3_b.addProviderVariable(windowswebprice);// link the variables to the
												// function
				
		// //////////////////////////////////////////END of the Specification of the s3 PricePlan/////////////////////////////////////////////
		
		ArrayList<Offering> offs = new ArrayList<Offering>();
		offs.add(of1);
		offs.add(of2);
		offs.add(of3);
		
		jmodel.setOfferings(offs);
		
		
		jmodel.setBaseURI("http://PricingAPIArsysOfferings.com");
		Model instance = jmodel.WriteToModel();//transform the java models to a semantic representation

		File outputFile = new File("./DebuggingFiles/arsys.ttl");
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(outputFile);
		instance.write(out, "Turtle");
		out.close();
	}
}
