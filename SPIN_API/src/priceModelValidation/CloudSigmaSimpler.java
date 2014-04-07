
/**
 * @author Daniel Guedes Barrigas - danielgbarrigas@hotmail.com / danielgbarrigas@gmail.com
 * 
 * Uses the LinkedUSDL Pricing API to model the Subscription plan from CloudSigma 
 * Info about their offerings can be seen at: http://www.cloudsigma.com/ and by talking to their customer service
 * The pricing method adopted by CloudSigma's subscription plan is an Unbundled Recurrent Subscription model. They provide pre-determined subscription plans from which the customer can choose the one
 * that suits his needs the best.
 * The current fees were extracted from their subscription pricing spreadsheet, which was provided by their customer support.
 * Subscription plans:
 * 1 Month
 * 3 Months
 * 6Months
 * 12Months
 * 24Months
 * 36Months
 * 
 * For this example we chose to model the 6Months Subscription plan located in Zurich.
 */
package priceModelValidation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;

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

public class CloudSigmaSimpler {
	
	public static void main(String[] args) throws IOException 
	{
		LinkedUSDLModel jmodel;

		jmodel = LinkedUSDLModelFactory.createEmptyModel();
		
		CloudSigmaRecurrentSubsOffering(jmodel);
		
		jmodel.setBaseURI("http://PricingAPICloudSigma6MRecurrentSubscription.com");
		Model instance = jmodel.WriteToModel();//transform the java models to a semantic representation

		File outputFile = new File("./DebuggingFiles/CloudsigmaRS.ttl");
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(outputFile);
		instance.write(out, "Turtle");
		out.close();
	}
	
	public static void CloudSigmaRecurrentSubsOffering(LinkedUSDLModel jmodel) throws IOException
	{
		//first, create the services 
		Service s1 = new Service();
		s1.setName("6MRecurrentSubscription");
		
		ArrayList<QuantitativeValue> s1QuantFeat = new ArrayList<QuantitativeValue>();//container for the Quantitative Features
		ArrayList<QualitativeValue> s1QualFeat = new ArrayList<QualitativeValue>();//container for the Qualitative Features
		
		//architecture type
		QualitativeValue Arch = new QualitativeValue();
		Arch.addType(CLOUDEnum.BIT64.getConceptURI());
		Arch.setHasLabel("32Bits");
		s1QualFeat.add(Arch);
		
		//availability
		QuantitativeValue Availability = new QuantitativeValue();
		Availability.addType(CLOUDEnum.AVAILABILITY.getConceptURI());
		Availability.setValue(100);
		Availability.setUnitOfMeasurement("P1");
		s1QuantFeat.add(Availability);
		
		//support
		QualitativeValue LiveChat = new QualitativeValue();
		LiveChat.addType(CLOUDEnum.LIVECHAT.getConceptURI());
		LiveChat.setHasLabel("Live Chat");
		s1QualFeat.add(LiveChat);
		
		QualitativeValue sup_24_7 = new QualitativeValue();
		sup_24_7.addType(CLOUDEnum.SUPPORT_24_7.getConceptURI());
		sup_24_7.setHasLabel("Support 24/7 available.");
		s1QualFeat.add(sup_24_7);
		
		QualitativeValue other_support = new QualitativeValue();
		other_support.addType(CLOUDEnum.OTHER_SUPPORT.getConceptURI());
		other_support.setHasLabel("Email");
		s1QualFeat.add(other_support);
		
		QualitativeValue forum = new QualitativeValue();
		forum.addType(CLOUDEnum.FORUM.getConceptURI());
		forum.setHasLabel("Forum");
		s1QualFeat.add(forum);
		
		QualitativeValue manual = new QualitativeValue();
		manual.addType(CLOUDEnum.MANUAL.getConceptURI());
		manual.setHasLabel("Online manuals.");
		s1QualFeat.add(manual);
		
		//Management
		QualitativeValue API , GUI  ;
		API=new QualitativeValue();
		API.addType(CLOUDEnum.API.getConceptURI());
		API.setHasLabel("Rest");
		s1QualFeat.add(API);
		
		GUI=new QualitativeValue();
		GUI.addType(CLOUDEnum.GUI.getConceptURI());
		GUI.setHasLabel("Browser based");
		s1QualFeat.add(manual);
		s1QualFeat.add(GUI);
		//Encryption
		QualitativeValue Encryption = new QualitativeValue();
		Encryption.addType(CLOUDEnum.ENCRYPTION.getConceptURI());
		Encryption.setHasLabel("256bit AES­XTS ");
		s1QualFeat.add(Encryption);
		
		//Location
		QualitativeValue Location = new QualitativeValue();
		Location.addType(CLOUDEnum.LOCATION.getConceptURI());
		Location.setHasLabel("Zurich");
		s1QualFeat.add(Location);
		
		//Elastic IP (static ip), billable feature, needs to be taken into account when creating the price plan for this service
		QualitativeValue staticIP = new QualitativeValue();
		staticIP.addType(CLOUDEnum.ELASTICIP.getConceptURI());
		staticIP.setHasLabel("Yes");
		s1QualFeat.add(staticIP);
		
		//latency
		QuantitativeValue Latency = new QuantitativeValue();
		Latency.addType(CLOUDEnum.NETWORKLATENCY.getConceptURI());
		Latency.setValue(1);
		Latency.setUnitOfMeasurement("C26"); //milliseconds
		s1QuantFeat.add(Latency);
		
		//Disk,RAM,CPUCores and CPUSpeed
		QuantitativeValue DiskSize = new QuantitativeValue();
		DiskSize.addType(CLOUDEnum.DISKSIZE.getConceptURI());
		DiskSize.setMinValue(1);
		DiskSize.setMaxValue(128*1024);//128TB=128*1024
		DiskSize.setUnitOfMeasurement("E34");
		s1QuantFeat.add(DiskSize);
		
		QuantitativeValue MemorySize = new QuantitativeValue();
		MemorySize.addType(CLOUDEnum.MEMORYSIZE.getConceptURI());
		MemorySize.setMinValue(0.25);
		MemorySize.setMaxValue(128);
		MemorySize.setUnitOfMeasurement("E34");
		s1QuantFeat.add(MemorySize);
		
		QuantitativeValue CPUCores = new QuantitativeValue();
		CPUCores.addType(CLOUDEnum.CPUCORES.getConceptURI());
		CPUCores.setMinValue(1);
		CPUCores.setMaxValue(40);
		s1QuantFeat.add(CPUCores);
		
		QuantitativeValue CPUSpeed = new QuantitativeValue();
		CPUSpeed.addType(CLOUDEnum.CPUSPEED.getConceptURI());
		CPUSpeed.setUnitOfMeasurement("A86");//Ghz
		CPUSpeed.setMinValue(0.25);
		CPUSpeed.setMaxValue(80);
		s1QuantFeat.add(CPUSpeed);
		
		//OS
		QualitativeValue OS = new QualitativeValue();
		OS.addType(CLOUDEnum.WINDOWS.getConceptURI());
		OS.setComment("Operating System");
		OS.setHasLabel("Windows");
		s1QualFeat.add(OS);
		
		//storage type, has a different price per GB than the regular HDD
		QualitativeValue StorageType = new QualitativeValue();
		StorageType.addType(CLOUDEnum.STORAGETYPE.getConceptURI());
		StorageType.setHasLabel("SSD");
		s1QualFeat.add(StorageType);
		
		
		//data transferrals, there's no limit value for the data transferrals, inwards are free, outwards are charged by  GB
		QuantitativeValue DATAINEXTERNAL = new QuantitativeValue();
		QuantitativeValue DATAININTERNAL = new QuantitativeValue();
		QuantitativeValue DATAOUTEXTERNAL = new QuantitativeValue();
		QuantitativeValue DATAOUTINTERNAL = new QuantitativeValue();

		DATAINEXTERNAL.addType(CLOUDEnum.DATAINEXTERNAL.getConceptURI());
		DATAINEXTERNAL.setMaxValue(Double.MAX_VALUE);// unlimited
		s1QuantFeat.add(DATAINEXTERNAL);

		DATAININTERNAL.addType(CLOUDEnum.DATAININTERNAL.getConceptURI());
		DATAININTERNAL.setMaxValue(Double.MAX_VALUE);// unlimited
		s1QuantFeat.add(DATAININTERNAL);

		DATAOUTEXTERNAL.addType(CLOUDEnum.DATAOUTEXTERNAL.getConceptURI());
		DATAOUTEXTERNAL.setMaxValue(Double.MAX_VALUE);// unlimited
		s1QuantFeat.add(DATAOUTEXTERNAL);

		DATAOUTINTERNAL.addType(CLOUDEnum.DATAOUTINTERNAL.getConceptURI());
		DATAOUTINTERNAL.setMaxValue(Double.MAX_VALUE);// unlimited
		s1QuantFeat.add(DATAOUTINTERNAL);
		
		//finished modeling the service features, now we need to create its Offering and PricePlan
		
		Offering of = new Offering();
		of.setName("S16MSubscription-Offering");
		of.addService(s1);
		
		PricePlan pp = new PricePlan();//There'll be a total of 5 which represent the billable attributes of the service. Each of the components implements its own discount since the discount to be applied depends on local information from each  of this components.
		of.setPricePlan(pp);
		pp.setName("S16mSubs-O-PP");
		pp.setComment("PricePlan for CloudSigma's s1 6msubscription plan");
		
		PriceComponent pc1 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the DiskSize (SSD)
		pp.addPriceComponent(pc1);
		pc1.setName("pc1-ssdcost");
		pc1.setComment("Responsible for calculating the cost of the SSD hardware.");
		
		PriceFunction pf1 = new PriceFunction();
		pc1.setPriceFunction(pf1);
		pf1.setName("ssd-pf");
		
		Usage ssdGB = new Usage();
		pf1.addUsageVariable(ssdGB);
		ssdGB.setName("gbdisk"+"TIME"+System.nanoTime());
		ssdGB.setComment("Number of GB that you need for the instance disk.");
		
		Provider ssdcost = new Provider();
		pf1.addProviderVariable(ssdcost);
		ssdcost.setName("ssdcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue val = new QuantitativeValue();
		val.setValue(0.22);
		val.setUnitOfMeasurement("USD");
		
		ssdcost.setValue(val);
		
		pf1.setStringFunction("IF " + ssdGB.getName() +"<15360 ;" +
				                         "("+ssdcost.getName() + " * " + ssdGB.getName() + ") * 6  * (1-0.1) "+  "~ " +
				                         "ELSEIF " +  ssdGB.getName() +">= 15360 && " + ssdGB.getName() + "< 25600  ;" +
				                         "("+ssdcost.getName() + " * " + ssdGB.getName() + ") * 6 * (1-0.075) * (1- 0.1) "+  "~ " +
				                         "ELSEIF " +  ssdGB.getName() +">= 25600 && " + ssdGB.getName() + "< 51200 ;" +
				                         "("+ ssdcost.getName() + " * " + ssdGB.getName() + ") * 6 * (1-0.125) * (1- 0.1) "+  "~ " +
				                         "ELSEIF " +  ssdGB.getName() +">= 51200 && " + ssdGB.getName() + "< 256000  ;" +
				                         "("+ ssdcost.getName() + " * " + ssdGB.getName() + ") * 6 * (1-0.2) * (1- 0.1) "+  "~ " +
				                         "ELSEIF " +  ssdGB.getName() +">= 256000 && " + ssdGB.getName() + "< 1024000 ;" +
				                         "("+  ssdcost.getName() + " * " + ssdGB.getName() + ") * 6 * (1-0.3) * (1- 0.1) "+  "~ " +
				                         "ELSEIF " +  ssdGB.getName() +">= 1024000  ;" +
				                         "("+   ssdcost.getName() + " * " + ssdGB.getName() + ") * 6 * (1-0.425) * (1- 0.1) "            
				);


		//this price function takes into account the volume discount provided by cloudsigma
		/*---------------------------------------------------------------------*/
		
		PriceComponent pc2 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the RAM 
		pp.addPriceComponent(pc2);
		pc2.setName("pc2-ramcost");
		pc2.setComment("Responsible for calculating the cost of the ram.");
		
		PriceFunction pf2 = new PriceFunction();
		pc2.setPriceFunction(pf2);
		pf2.setName("ram-pf");
		
		Usage ramGB = new Usage();
		pf2.addUsageVariable(ramGB);
		ramGB.setName("gbram"+"TIME"+System.nanoTime());
		ramGB.setComment("Number of GB of RAM you need.");
		
		Provider ramcost = new Provider();
		pf2.addProviderVariable(ramcost);
		ramcost.setName("ramcost"+"TIME"+System.nanoTime());
		
		
		
		QuantitativeValue valb = new QuantitativeValue();
		valb.setValue(0.0229);
		valb.setUnitOfMeasurement("USD");
		ramcost.setValue(valb);
		
		pf2.setStringFunction("IF "+ramGB.getName() +"<50  ;" +
                "("+ ramcost.getName() + " * " + ramGB.getName() + ") * 4320  * (1-0.1) "+  "~ " +
                "ELSEIF " +  ramGB.getName() +">= 50 && " + ramGB.getName() + "< 100  ;" +
                "("+   ramcost.getName() + " * " + ramGB.getName() + ") * 4320 * (1-0.075) * (1- 0.1) "+  "~ " +
                "ELSEIF " +  ramGB.getName() +">= 100 && " + ramGB.getName() + "< 500  ;" +
                "("+   ramcost.getName() + " * " + ramGB.getName() + ") * 4320 * (1-0.125) * (1- 0.1) "+  "~ " +
                "ELSEIF " +  ramGB.getName() +">= 500 && " + ramGB.getName() + "< 4000  ;" +
                "("+    ramcost.getName() + " * " + ramGB.getName() + ") * 4320 * (1-0.2) * (1- 0.1) "+  "~ " +
                "ELSEIF " +  ramGB.getName() +">= 4000 && " + ramGB.getName() + "< 10000 ;" +
                "("+   ramcost.getName() + " * " + ramGB.getName() + ") * 4320 * (1-0.3) * (1- 0.1) "+  "~ " +
                "ELSEIF " +  ramGB.getName() +">= 10000  ;" +
                "("+ ramcost.getName() + " * " + ramGB.getName() + ") * 4320 * (1-0.425) * (1- 0.1) "
				);
		
		/*---------------------------------------------------------------------*/
		
		PriceComponent pc3 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the CPU Speed 
		pp.addPriceComponent(pc3);
		pc3.setName("pc3-cpuspeedcost");
		pc3.setComment("Responsible for calculating the cost of the CPUSpeed.");
		
		PriceFunction pf3 = new PriceFunction();
		pc3.setPriceFunction(pf3);
		pf3.setName("cpuspeed-pf");
		
		Usage cpuSpeed = new Usage();
		pf3.addUsageVariable(cpuSpeed);
		cpuSpeed.setName("cpuspeed"+"TIME"+System.nanoTime());
		cpuSpeed.setComment("CPUSpeed you need.");

		Provider cpuspeedcost = new Provider();
		pf3.addProviderVariable(cpuspeedcost);
		cpuspeedcost.setName("cpuspeedcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue valc = new QuantitativeValue();
		valc.setValue(0.0187);
		valc.setUnitOfMeasurement("USD");
		cpuspeedcost.setValue(valc);
		
		pf3.setStringFunction("IF "+cpuSpeed.getName() +"<50  ;" +
                "("+ cpuspeedcost.getName() + " * " + cpuSpeed.getName() + ") * 4320  * (1-0.1) "+  "~ " +
                "ELSEIF " +  cpuSpeed.getName() +">= 50 && " + cpuSpeed.getName() + "< 100 ;" +
                "("+  cpuspeedcost.getName() + " * " + cpuSpeed.getName() + ") * 4320 * (1-0.075) * (1- 0.1) "+  "~ " +
                "ELSEIF " +  cpuSpeed.getName() +">= 100 && " + cpuSpeed.getName() + "< 500  ;" +
                "("+  cpuspeedcost.getName() + " * " + cpuSpeed.getName() + ") * 4320 * (1-0.125) * (1- 0.1) "+  "~ " +
                "ELSEIF " +  cpuSpeed.getName() +">= 500 && " + cpuSpeed.getName() + "< 4000 ;" +
                "("+   cpuspeedcost.getName() + " * " + cpuSpeed.getName() + ") * 4320 * (1-0.2) * (1- 0.1) "+  "~ " +
                "ELSEIF " +  cpuSpeed.getName() +">= 4000 && " + cpuSpeed.getName() + "< 10000  ;" +
                "("+    cpuspeedcost.getName() + " * " + cpuSpeed.getName() + ") * 4320 * (1-0.3) * (1- 0.1) "+  "~ " +
                "ELSEIF " +  cpuSpeed.getName() +">= 10000  ;" +
                "("+   cpuspeedcost.getName() + " * " + cpuSpeed.getName() + ") * 4320 * (1-0.425) * (1- 0.1) "
);
		
		/*---------------------------------------------------------------------*/
		
		PriceComponent pc4 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the StaticIP 
		pp.addPriceComponent(pc4);
		pc4.setName("pc4-staticipcost");
		pc4.setComment("Responsible for calculating the cost of the StaticIPs.");
		
		PriceFunction pf4 = new PriceFunction();
		pc4.setPriceFunction(pf4);
		pf4.setName("staticip-pf");
		
		Usage NStaticIP = new Usage();
		pf4.addUsageVariable(NStaticIP);
		NStaticIP.setName("nstaticip"+"TIME"+System.nanoTime());
		NStaticIP.setComment("Number of StaticIP you need.");

		Provider StaticIPCost = new Provider();
		pf4.addProviderVariable(StaticIPCost);
		StaticIPCost.setName("staticipcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue vald = new QuantitativeValue();
		vald.setValue(4.5);
		vald.setUnitOfMeasurement("USD");
		StaticIPCost.setValue(vald);
		
		pf4.setStringFunction(StaticIPCost.getName() + "*" + NStaticIP.getName() + "* 6 * (1-0.1)" );
		
		/*---------------------------------------------------------------------*/
		
		PriceComponent pc5 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the Outgoing Data transfer 
		pp.addPriceComponent(pc5);
		pc5.setName("pc5-DATAOUT");
		pc5.setComment("Responsible for calculating the cost of the outgoing data transferral.");
		
		PriceFunction pf5 = new PriceFunction();
		pc5.setPriceFunction(pf5);
		pf5.setName("dataout-pf");
		
		Usage NGBOut = new Usage();
		pf5.addUsageVariable(NGBOut);
		NGBOut.setName("ngbout"+"TIME"+System.nanoTime());
		NGBOut.setComment("Number of GB you expect to send out per month.");

		Provider gboutcost = new Provider();
		pf5.addProviderVariable(gboutcost);
		gboutcost.setName("gboutcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue vale = new QuantitativeValue();
		vale.setValue(0.0715);
		vale.setUnitOfMeasurement("USD");
		gboutcost.setValue(vale);
		
		pf5.setStringFunction(gboutcost.getName() + "*" + NGBOut.getName() + "* 6 * (1-0.1)" );
		
		// END

		ArrayList<Offering> offs = new ArrayList<Offering>();
		offs.add(of);

		jmodel.setOfferings(offs);
	}
	
	
}
