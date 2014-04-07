
/**
 * @author Daniel Guedes Barrigas - danielgbarrigas@hotmail.com / danielgbarrigas@gmail.com
 * 
 * Uses the LinkedUSDL Pricing API to model the 3 months Subscription plan from VMWare Hybrid vCloud
 * Info about their offerings can be seen at: http://vcloud.vmware.com/about_services/pricing
 * The pricing method adopted by VMWare's Hybrid vCloud service is a PB Bundled Recurring Resource Pooling plan. 
 */


package priceModelValidation;

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

public class VMWareVPCloud {
	
	
	public static void main(String[] args) throws IOException {
		LinkedUSDLModel jmodel;

		jmodel = LinkedUSDLModelFactory.createEmptyModel();

		VMWareVPCloudOffering(jmodel);

		jmodel.setBaseURI("http://PricingAPIVMWareVPCloud.com");
		Model instance = jmodel.WriteToModel();// transform the java models to a
												// semantic representation

		File outputFile = new File("./DebuggingFiles/VMWareVPCloud.ttl");
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(outputFile);
		instance.write(out, "Turtle");
		out.close();
	}
	
	public static void VMWareVPCloudOffering(LinkedUSDLModel jmodel)
	{
		//first, create the services 
		Service s1 = new Service();
		s1.setName("VMWareVPC");

		ArrayList<QuantitativeValue> s1QuantFeat = new ArrayList<QuantitativeValue>();// container for the QuantitativeFeatures
		ArrayList<QualitativeValue> s1QualFeat = new ArrayList<QualitativeValue>();// container for the Qualitative Features
		//add some features to the service
		
		// Disk,RAM,CPUCores and CPUSpeed
		QuantitativeValue DiskSize = new QuantitativeValue();
		DiskSize.addType(CLOUDEnum.DISKSIZE.getConceptURI());
		DiskSize.setMinValue(2*1024);
		DiskSize.setMaxValue(Double.MAX_VALUE);
		DiskSize.setUnitOfMeasurement("E34");
		s1QuantFeat.add(DiskSize);

		QuantitativeValue MemorySize = new QuantitativeValue();
		MemorySize.addType(CLOUDEnum.MEMORYSIZE.getConceptURI());
		MemorySize.setMinValue(20);
		MemorySize.setMaxValue(Double.MAX_VALUE);
		MemorySize.setUnitOfMeasurement("E34");
		s1QuantFeat.add(MemorySize);

		QuantitativeValue CPUSpeed = new QuantitativeValue();
		CPUSpeed.addType(CLOUDEnum.CPUSPEED.getConceptURI());
		CPUSpeed.setUnitOfMeasurement("A86");// Ghz
		CPUSpeed.setMinValue(5);
		CPUSpeed.setMaxValue(Double.MAX_VALUE);
		s1QuantFeat.add(CPUSpeed);
		
		QualitativeValue sup_24_7 = new QualitativeValue();
		sup_24_7.addType(CLOUDEnum.SUPPORT_24_7.getConceptURI());
		sup_24_7.setHasLabel("Support 24/7 available.");
		s1QualFeat.add(sup_24_7);
		
		// Location
		QualitativeValue Location = new QualitativeValue();
		Location.addType(CLOUDEnum.LOCATION.getConceptURI());
		Location.setHasLabel("United States");
		s1QualFeat.add(Location);
		
		//Elastic IP (static ip), billable feature, needs to be taken into account when creating the price plan for this service
		QualitativeValue staticIP = new QualitativeValue();
		staticIP.addType(CLOUDEnum.ELASTICIP.getConceptURI());
		staticIP.setHasLabel("Yes");
		s1QualFeat.add(staticIP);
		
		QuantitativeValue TRANSFERRATE  = new QuantitativeValue();
		TRANSFERRATE.addType(CLOUDEnum.TRANSFERRATE.getConceptURI());
		TRANSFERRATE.setMinValue(10);
		TRANSFERRATE.setMaxValue(Double.MAX_VALUE);
		TRANSFERRATE.setUnitOfMeasurement("4L");//Mbps
		s1QuantFeat.add(TRANSFERRATE);
		
		//finished modeling the service features, now we need to create its Offering and PricePlan
		
		Offering of = new Offering();
		of.setName("VPMonthlySubscription-Offering");
		of.addService(s1);
				
		PricePlan pp = new PricePlan();
		of.setPricePlan(pp);
		pp.setName("VPMonthlySubs-O-PP");
		pp.setComment("PricePlan for VMWare's Virtual Price Cloud subscription plan");
				
		PriceComponent pc1 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the MemorySize + CPU 
		pp.addPriceComponent(pc1);
		pc1.setName("pc1-cpu_ramcost");
		pc1.setComment("Responsible for calculating the cost of the CPU and RAM hardware.");
		
		PriceFunction pf1 = new PriceFunction();
		pc1.setPriceFunction(pf1);
		pf1.setName("cpu_ram-pf");
		
		Usage CPU = new Usage();
		pf1.addUsageVariable(CPU);
		CPU.setName("cpuspeed"+"TIME"+System.nanoTime());
		CPU.setComment("Number of GHz you need for the CPU.");
		
		Usage RAM = new Usage();
		pf1.addUsageVariable(RAM);
		RAM.setName("ramsize"+"TIME"+System.nanoTime());
		RAM.setComment("GB of RAM you need..");

		Provider cpuramcost = new Provider();
		pf1.addProviderVariable(cpuramcost);
		cpuramcost.setName("cpuramcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue val = new QuantitativeValue();
		val.setValue(519);
		val.setUnitOfMeasurement("USD");
		
		cpuramcost.setValue(val);
		//1/20=0.05 ; 1/5 = 0.02		
		pf1.setStringFunction("IF("+CPU.getName()+" <= 5 && "+RAM.getName()+"<= 20) ; "+cpuramcost.getName() +"*3"+ "~" +
				"ELSEIF("+CPU.getName() +"<= 5 && "+RAM.getName() +"> 20) ; CEIL("+RAM.getName()+"/ 20) *"+cpuramcost.getName() +"*3" +"~" +
				"ELSEIF("+CPU.getName() +"> 5 && "+RAM.getName() +"<= 20) ; CEIL("+CPU.getName()+"/5) *"+cpuramcost.getName() +"*3" +"~" +
				"ELSEIF("+CPU.getName() +"> 5 && "+RAM.getName() +"> 20 && CEIL("+CPU.getName()+"/5) > CEIL("+RAM.getName()+"/20)"+") ; CEIL("+CPU.getName()+"/5) *"+cpuramcost.getName() +"*3"+"~" +
				"ELSEIF("+CPU.getName() +"> 5 && "+RAM.getName() +"> 20 && CEIL("+CPU.getName()+"/5) < CEIL("+RAM.getName()+"/20)"+") ; CEIL("+RAM.getName()+"/20) *"+cpuramcost.getName() +"*3" +"~" +
				"ELSEIF("+CPU.getName() +"> 5 && "+RAM.getName() +"> 20 && CEIL("+CPU.getName()+"/5) == CEIL("+RAM.getName()+"/20)"+") ; CEIL("+RAM.getName()+"/20) *"+cpuramcost.getName() +"*3" 
				);
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		
		PriceComponent pc2 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the support provided by VMWare 
		pp.addPriceComponent(pc2);
		pc2.setName("pc2-supportcost");
		pc2.setComment("Responsible for calculating the cost of the support.");
		
		PriceFunction pf2 = new PriceFunction();
		pc2.setPriceFunction(pf2);
		pf2.setName("support-pf");
		
		Provider supportcost = new Provider();
		pf2.addProviderVariable(supportcost);
		supportcost.setName("supportcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue valb = new QuantitativeValue();
		valb.setValue(90);
		valb.setUnitOfMeasurement("USD");
		
		supportcost.setValue(valb);
		
		pf2.setStringFunction(supportcost.getName() +"*3");
		
		//////////////////////////////////////////////////////
		
		
		PriceComponent pc3 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the DiskSize
		pp.addPriceComponent(pc3);
		pc3.setName("pc3-diskcost");
		pc3.setComment("Responsible for calculating the cost of the disk size.");
		
		PriceFunction pf3 = new PriceFunction();
		pc3.setPriceFunction(pf3);
		pf3.setName("disksize-pf");
		
		Usage disk_size = new Usage();
		pf3.addUsageVariable(disk_size);
		disk_size.setName("disksize"+"TIME"+System.nanoTime());
		disk_size.setComment("GB of Disk you need.");
		
		Provider diskcost = new Provider();
		pf3.addProviderVariable(diskcost);
		diskcost.setName("diskcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue valc = new QuantitativeValue();
		valc.setValue(249);
		valc.setUnitOfMeasurement("USD");
		
		diskcost.setValue(valc);
		//1/20=0.05 ; 1/5 = 0.02		
		pf3.setStringFunction("IF("+disk_size.getName()+" <= 2048 ) ; "+diskcost.getName() +"*3"+ "~" +
				"ELSEIF("+disk_size.getName()+" > 2048 ) ;"+" CEIL("+disk_size.getName()+"/ 2024) *"+diskcost.getName() +"*3"
				);
		
		//////////////////////////////////////////////////////
				
				
		PriceComponent pc4 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the Bandwidth
		pp.addPriceComponent(pc4);
		pc4.setName("pc4-bandwidth");
		pc4.setComment("Responsible for calculating the cost of the bandwidth.");
		
		PriceFunction pf4 = new PriceFunction();
		pc4.setPriceFunction(pf4);
		pf4.setName("bandwidth-pf");
		
		Usage band = new Usage();
		pf4.addUsageVariable(band);
		band.setName("bandwidth"+"TIME"+System.nanoTime());
		band.setComment("Mbps of Bandwith you need.");

		Provider bandwidthcost = new Provider();
		pf4.addProviderVariable(bandwidthcost);
		bandwidthcost.setName("bandwidthcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue vald = new QuantitativeValue();
		vald.setValue(197);
		vald.setUnitOfMeasurement("USD");
		
		bandwidthcost.setValue(vald);
		//1/20=0.05 ; 1/5 = 0.02		
		pf4.setStringFunction("IF("+band.getName()+" <= 10 ) ; "+bandwidthcost.getName() +"*3"+ "~" +
		"ELSEIF("+band.getName()+" > 10 ) ;"+" CEIL("+band.getName()+"/ 10) *"+bandwidthcost.getName() +"*3"
		);
		
		//////////////////////////////////////////////////////
				
				
		PriceComponent pc5 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the StaticIPs
		pp.addPriceComponent(pc5);
		pc5.setName("pc5-staticip");
		pc5.setComment("Responsible for calculating the cost of the number of StaticIPs chosen by the customer.");
		
		PriceFunction pf5 = new PriceFunction();
		pc5.setPriceFunction(pf5);
		pf5.setName("staticip-pf");
		
		Usage NStatic = new Usage();
		pf5.addUsageVariable(NStatic);
		NStatic.setName("nstaticip"+"TIME"+System.nanoTime());
		NStatic.setComment("Number of Static IPs you need.");
		
		Provider staticipcost = new Provider();
		pf5.addProviderVariable(staticipcost);
		staticipcost.setName("staticipcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue vale = new QuantitativeValue();
		vale.setValue(21.50);
		vale.setUnitOfMeasurement("USD");
		
		staticipcost.setValue(vale);
		//1/20=0.05 ; 1/5 = 0.02		
		pf5.setStringFunction("IF("+NStatic.getName()+" <= 2 ) ; "+staticipcost.getName() +"*3"+ "~" +
		"ELSEIF("+NStatic.getName()+" > 2 ) ;"+" CEIL("+NStatic.getName()+"/ 2) *"+staticipcost.getName() +"*3"
		);
		
		//END
		
		ArrayList<Offering> offs = new ArrayList<Offering>();
		offs.add(of);

		jmodel.setOfferings(offs);
	}
}
