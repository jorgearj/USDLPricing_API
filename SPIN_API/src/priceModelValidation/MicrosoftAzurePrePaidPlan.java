
/**
 * @author Daniel Guedes Barrigas - danielgbarrigas@hotmail.com / danielgbarrigas@gmail.com
 * 
 * Uses the LinkedUSDL Pricing API to model the Subscription plan from Microsoft Azure 
 * Info about their offerings can be seen at: http://www.windowsazure.com/en-us/pricing/details/virtual-machines/#linux
 * The pricing method adopted by Microsoft Azure's pre-paid plan is a VB Bundlded PrePaid Credit model. They provide pre-payment plans from which the customer can choose the one
 * that suits his needs the best.
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

public class MicrosoftAzurePrePaidPlan {
	
	public static void main(String[] args) throws IOException {
		LinkedUSDLModel jmodel;

		jmodel = LinkedUSDLModelFactory.createEmptyModel();

		MAzurePrePaidPlan(jmodel);

		jmodel.setBaseURI("http://PricingAPIMAzureSmallPrePaid6m.com");
		Model instance = jmodel.WriteToModel();// transform the java models to a
												// semantic representation

		File outputFile = new File("./DebuggingFiles/MAzureSmallPrePaid6m.ttl");
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(outputFile);
		instance.write(out, "Turtle");
		out.close();
	}
	
	public static void MAzurePrePaidPlan(LinkedUSDLModel jmodel) throws IOException
	{
		//first, create the services 
		Service s1 = new Service();
		s1.setName("SmallPrePaid6m");
		
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
		Availability.setValue(99.95);
		Availability.setUnitOfMeasurement("P1");
		s1QuantFeat.add(Availability);
		
		// support
		QualitativeValue sup_24_7 = new QualitativeValue();
		sup_24_7.addType(CLOUDEnum.SUPPORT_24_7.getConceptURI());
		sup_24_7.setHasLabel("Support 24/7 available.");
		s1QualFeat.add(sup_24_7);

		QualitativeValue other_support = new QualitativeValue();
		other_support.addType(CLOUDEnum.OTHER_SUPPORT.getConceptURI());
		other_support.setHasLabel("Email");
		s1QualFeat.add(other_support);
		
		QualitativeValue other_supportb = new QualitativeValue();
		other_supportb.addType(CLOUDEnum.OTHER_SUPPORT.getConceptURI());
		other_supportb.setHasLabel("Phone");
		s1QualFeat.add(other_supportb);

		QualitativeValue forum = new QualitativeValue();
		forum.addType(CLOUDEnum.FORUM.getConceptURI());
		forum.setHasLabel("Forum");
		s1QualFeat.add(forum);

		QualitativeValue manual = new QualitativeValue();
		manual.addType(CLOUDEnum.MANUAL.getConceptURI());
		manual.setHasLabel("Online manuals.");
		s1QualFeat.add(manual);
		
		// Management
		QualitativeValue WEB;
		WEB = new QualitativeValue();
		WEB.addType(CLOUDEnum.WEB.getConceptURI());
		WEB.setHasLabel("WEB");
		s1QualFeat.add(WEB);
		
		// Location
		QualitativeValue Location = new QualitativeValue();
		Location.addType(CLOUDEnum.LOCATION.getConceptURI());
		Location.setHasLabel("US East");
		s1QualFeat.add(Location);
		
		//Disk,RAM,CPUCores and CPUSpeed
		QuantitativeValue DiskSize = new QuantitativeValue();
		DiskSize.addType(CLOUDEnum.DISKSIZE.getConceptURI());
		DiskSize.setValue(420);
		DiskSize.setUnitOfMeasurement("E34");
		s1QuantFeat.add(DiskSize);
		
		QuantitativeValue MemorySize = new QuantitativeValue();
		MemorySize.addType(CLOUDEnum.MEMORYSIZE.getConceptURI());
		MemorySize.setValue(1.75);
		MemorySize.setUnitOfMeasurement("E34");
		s1QuantFeat.add(MemorySize);
		
		QuantitativeValue CPUCores = new QuantitativeValue();
		CPUCores.addType(CLOUDEnum.CPUCORES.getConceptURI());
		CPUCores.setValue(1);
		s1QuantFeat.add(CPUCores);
		
		QuantitativeValue CPUSpeed = new QuantitativeValue();
		CPUSpeed.addType(CLOUDEnum.CPUSPEED.getConceptURI());
		CPUSpeed.setUnitOfMeasurement("A86");//Ghz
		CPUSpeed.setValue(1.66);
		s1QuantFeat.add(CPUSpeed);
		
		//OS
		QualitativeValue OS = new QualitativeValue();
		OS.addType(CLOUDEnum.UNIX.getConceptURI());
		OS.setComment("Operating System");
		OS.setHasLabel("Linux");
		s1QualFeat.add(OS);
		
		//Backup Recovery, paid, first 5GB per month are free
		QualitativeValue br = new QualitativeValue();
		br.addType(CLOUDEnum.BACKUP_RECOVERY.getConceptURI());
		br.setHasLabel("yes");
		s1QualFeat.add(br);
		
		//storage type, has a different price per GB than the regular HDD
		QualitativeValue StorageType = new QualitativeValue();
		StorageType.addType(CLOUDEnum.STORAGETYPE.getConceptURI());
		StorageType.setHasLabel("HDD");
		s1QualFeat.add(StorageType);
		
		//data transferrals, there's no limit value for the data transferrals, inwards are free, outwards are charged by  GB
		QuantitativeValue DATAINEXTERNAL = new QuantitativeValue();
		QuantitativeValue DATAININTERNAL = new QuantitativeValue();
		QuantitativeValue DATAOUTEXTERNAL = new QuantitativeValue();
		QuantitativeValue DATAOUTINTERNAL = new QuantitativeValue();

		DATAINEXTERNAL.addType(CLOUDEnum.DATAINEXTERNAL.getConceptURI());
		DATAINEXTERNAL.setValue(Double.MAX_VALUE);// unlimited
		s1QuantFeat.add(DATAINEXTERNAL);

		DATAININTERNAL.addType(CLOUDEnum.DATAININTERNAL.getConceptURI());
		DATAININTERNAL.setValue(Double.MAX_VALUE);// unlimited
		s1QuantFeat.add(DATAININTERNAL);

		DATAOUTEXTERNAL.addType(CLOUDEnum.DATAOUTEXTERNAL.getConceptURI());
		DATAOUTEXTERNAL.setValue(Double.MAX_VALUE);// unlimited
		s1QuantFeat.add(DATAOUTEXTERNAL);

		DATAOUTINTERNAL.addType(CLOUDEnum.DATAOUTINTERNAL.getConceptURI());
		DATAOUTINTERNAL.setValue(Double.MAX_VALUE);// unlimited
		s1QuantFeat.add(DATAOUTINTERNAL);
		
		//finished modeling the service features, now we need to create its Offering and PricePlan
		
		Offering of = new Offering();
		of.setName("SmallPrePaid-Offering");
		of.addService(s1);
		
		PricePlan pp = new PricePlan();
		of.setPricePlan(pp);
		pp.setName("SmallPrePaid-O-PP");
		pp.setComment("PricePlan for Microsoft's Small Virtual Machine.");
		
		PriceComponent pc1 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the VM per month
		pp.addPriceComponent(pc1);
		pc1.setName("pc1-vmcost");
		pc1.setComment("Responsible for calculating the total cost of the VM.");
		
		PriceFunction pf1 = new PriceFunction();
		pc1.setPriceFunction(pf1);
		pf1.setName("smallvm-cost-pf");
		
		
		Provider VMCost6m = new Provider();
		pf1.addProviderVariable(VMCost6m);
		VMCost6m.setName("VMCost6m"+"TIME"+System.nanoTime());
		
		QuantitativeValue val = new QuantitativeValue();
		val.setValue(0.036);
		val.setUnitOfMeasurement("EUR");
		VMCost6m.setValue(val);
		
		pf1.setStringFunction( "6*31*24" + "*" +VMCost6m.getName());
		
		////////////////
		
		PriceComponent pc2 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the Data Transfers per month
		pp.addPriceComponent(pc2);
		pc2.setName("pc1-datatransf_cost");
		pc2.setComment("Responsible for calculating the cost of the data transfers.");
		
		PriceFunction pf2 = new PriceFunction();
		pc2.setPriceFunction(pf2);
		pf2.setName("datatransf-cost-pf");
		
		Usage NGBOut_z1 = new Usage();
		pf2.addUsageVariable(NGBOut_z1);
		NGBOut_z1.setName("ngboutz1"+"TIME"+System.nanoTime());
		NGBOut_z1.setComment("Number of GB you expect to send out per month to the US West, US East, US North Central, US South Central, Europe West, Europe North.");
		
		Usage NGBOut_z2 = new Usage();
		pf2.addUsageVariable(NGBOut_z2);
		NGBOut_z2.setName("ngboutz2"+"TIME"+System.nanoTime());
		NGBOut_z2.setComment("Number of GB you expect to send out per month to the Asia Pacific East, Asia Pacific Southeast, Japan East, Japan West.");
		
		//Zone 1: US West, US East, US North Central, US South Central, Europe West, Europe North
		//Zone 2: Asia Pacific East, Asia Pacific Southeast, Japan East, Japan West
		
		Provider gboutcost_z1 = new Provider();
		pf2.addProviderVariable(gboutcost_z1);
		gboutcost_z1.setName("gboutcostz1"+"TIME"+System.nanoTime());
		
		QuantitativeValue valb = new QuantitativeValue();
		valb.setValue(0.08);
		valb.setUnitOfMeasurement("USD");
		gboutcost_z1.setValue(valb);
		
		Provider gboutcost_z2 = new Provider();
		pf2.addProviderVariable(gboutcost_z2);
		gboutcost_z2.setName("gboutcostz2"+"TIME"+System.nanoTime());
		
		QuantitativeValue valc = new QuantitativeValue();
		valc.setValue(0.012);
		valc.setUnitOfMeasurement("USD");
		gboutcost_z2.setValue(valc);
		
		pf2.setStringFunction("("+NGBOut_z1.getName() +"*"+gboutcost_z1.getName() + "+" + NGBOut_z2.getName() + "*" + gboutcost_z2.getName() + ")*6");
		
		/////////////////////////
		
		PriceComponent pc3 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the backup_recovery feature
		pp.addPriceComponent(pc3);
		pc3.setName("pc3-brcost");
		pc3.setComment("Responsible for calculating the total cost of the backup recovery feature.");
		
		PriceFunction pf3 = new PriceFunction();
		pc3.setPriceFunction(pf3);
		pf3.setName("br-cost-pf");
		
		
		Provider backupcost = new Provider();
		pf3.addProviderVariable(backupcost);
		backupcost.setName("backupcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue vald = new QuantitativeValue();
		vald.setValue(0.30);
		vald.setUnitOfMeasurement("EUR");
		backupcost.setValue(vald);
		
		
		Usage NGBbr= new Usage();
		pf3.addUsageVariable(NGBbr);
		NGBbr.setName("NGBbr"+"TIME"+System.nanoTime());
		NGBbr.setComment("Number of GB you expect to backup per month.");
		
		pf3.setStringFunction( "6*(" + NGBbr.getName()+  "*" +backupcost.getName()+ ")");
		
		////////////////Deductions, 3 total, 1 for data transfer, another for backup recovery and finally one for the total price to pay
		
		PriceComponent pc4 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the backup_recovery feature
		pp.addPriceComponent(pc4);
		pc4.setName("pc4-datatransf-deduction");
		pc4.setComment("Responsible for calculating the discount of data transf.");
		pc4.setDeduction(true);
		
		PriceFunction pf4 = new PriceFunction();
		pc4.setPriceFunction(pf4);
		pf4.setName("datatrans-deduction-pf");
		
		
		Usage NGBOut_z1b = new Usage();
		pf4.addUsageVariable(NGBOut_z1b);
		NGBOut_z1b.setName("ngboutz1"+"TIME"+System.nanoTime());
		NGBOut_z1b.setComment("Number of GB you expect to send out per month to the US West, US East, US North Central, US South Central, Europe West, Europe North.");
		
		Usage NGBOut_z2b = new Usage();
		pf4.addUsageVariable(NGBOut_z2b);
		NGBOut_z2b.setName("ngboutz2"+"TIME"+System.nanoTime());
		NGBOut_z2b.setComment("Number of GB you expect to send out per month to the Asia Pacific East, Asia Pacific Southeast, Japan East, Japan West.");
		
		//Zone 1: US West, US East, US North Central, US South Central, Europe West, Europe North
		//Zone 2: Asia Pacific East, Asia Pacific Southeast, Japan East, Japan West
		
		Provider gboutcost_z1b = new Provider();
		pf4.addProviderVariable(gboutcost_z1b);
		gboutcost_z1b.setName("gboutcostz1"+"TIME"+System.nanoTime());
		
		QuantitativeValue valf = new QuantitativeValue();
		valf.setValue(0.08);
		valf.setUnitOfMeasurement("USD");
		gboutcost_z1b.setValue(valf);
		
		Provider gboutcost_z2b = new Provider();
		pf4.addProviderVariable(gboutcost_z2b);
		gboutcost_z2b.setName("gboutcostz2"+"TIME"+System.nanoTime());
		
		QuantitativeValue valg = new QuantitativeValue();
		valg.setValue(0.012);
		valg.setUnitOfMeasurement("USD");
		gboutcost_z2b.setValue(valg);
		
		pf4.setStringFunction("IF("+NGBOut_z1b.getName()+">5 && "+NGBOut_z2b.getName()+ " > 5); 6*(      5* "+gboutcost_z1b.getName()+"  + 5*   "+gboutcost_z2b.getName()+"  ) " + "~" +
												"ELSEIF ("+NGBOut_z1b.getName()+">5 &&"+NGBOut_z2b.getName()+ " < 5); 6*(      5* "+gboutcost_z1b.getName()+")" + "~" +
												"ELSEIF ("+NGBOut_z1b.getName()+"<5 &&"+NGBOut_z2b.getName()+ " > 5); 6*(      5* "+gboutcost_z2b.getName()+")"
				);
		
		////////////////////////////////
		
		PriceComponent pc5 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the backup_recovery feature
		pp.addPriceComponent(pc5);
		pc5.setName("pc5-brdeduction");
		pc5.setComment("Responsible for calculating the total deduction of the backup recovery feature.");
		pc5.setDeduction(true);
		
		PriceFunction pf5 = new PriceFunction();
		pc5.setPriceFunction(pf5);
		pf5.setName("br-deduction-pf");
		
		
		Provider backupcostb = new Provider();
		pf5.addProviderVariable(backupcostb);
		backupcostb.setName("backupcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue valh = new QuantitativeValue();
		valh.setValue(0.30);
		valh.setUnitOfMeasurement("EUR");
		backupcostb.setValue(valh);
		
		
		Usage NGBbrb= new Usage();
		pf5.addUsageVariable(NGBbrb);
		NGBbrb.setName("NGBbr"+"TIME"+System.nanoTime());
		NGBbrb.setComment("Number of GB you expect to backup per month.");
		
		pf5.setStringFunction( "IF ("+NGBbrb.getName()+">5) ;"+"6*(5*" +backupcost.getName()+ ")");
		
		
		////Deduction to be applied to the total price. The discount depends on the amount that the user will pay.
		
		
		///////////////////////////////////////////
		PriceComponent pc6 = new PriceComponent();//PriceComponent responsible for calculating the Cost of the backup_recovery feature
		pp.addPriceComponent(pc6);
		pc6.setName("pc6-totaldeduction");
		pc6.setComment("Responsible for calculating the total deduction to be applied to the final price.");
		pc6.setDeduction(true);
		
		PriceFunction pf6 = new PriceFunction();
		pc6.setPriceFunction(pf6);
		pf6.setName("totalprice-deduction-pf");
		
		
		Provider backupcostc = new Provider();
		pf6.addProviderVariable(backupcostc);
		backupcostc.setName("backupcost"+"TIME"+System.nanoTime());
		
		QuantitativeValue vali = new QuantitativeValue();
		vali.setValue(0.30);
		vali.setUnitOfMeasurement("EUR");
		backupcostc.setValue(vali);
		
		
		Usage NGBbrc= new Usage();
		pf6.addUsageVariable(NGBbrc);
		NGBbrc.setName("NGBbr"+"TIME"+System.nanoTime());
		NGBbrc.setComment("Number of GB you expect to backup per month.");

		//pf3.setStringFunction( "6*(" + NGBbr.getName()+  "*" +backupcost.getName()+ ")");
		
		
		Usage NGBOut_z1c = new Usage();
		pf6.addUsageVariable(NGBOut_z1c);
		NGBOut_z1c.setName("ngboutz1"+"TIME"+System.nanoTime());
		NGBOut_z1c.setComment("Number of GB you expect to send out per month to the US West, US East, US North Central, US South Central, Europe West, Europe North.");
		
		Usage NGBOut_z2c = new Usage();
		pf6.addUsageVariable(NGBOut_z2c);
		NGBOut_z2c.setName("ngboutz2"+"TIME"+System.nanoTime());
		NGBOut_z2c.setComment("Number of GB you expect to send out per month to the Asia Pacific East, Asia Pacific Southeast, Japan East, Japan West.");
		
		//Zone 1: US West, US East, US North Central, US South Central, Europe West, Europe North
		//Zone 2: Asia Pacific East, Asia Pacific Southeast, Japan East, Japan West
		
		Provider gboutcost_z1c = new Provider();
		pf6.addProviderVariable(gboutcost_z1c);
		gboutcost_z1c.setName("gboutcostz1"+"TIME"+System.nanoTime());
		
		QuantitativeValue valj = new QuantitativeValue();
		valj.setValue(0.08);
		valj.setUnitOfMeasurement("USD");
		gboutcost_z1c.setValue(valj);
		
		Provider gboutcost_z2c = new Provider();
		pf6.addProviderVariable(gboutcost_z2c);
		gboutcost_z2c.setName("gboutcostz2"+"TIME"+System.nanoTime());
		
		QuantitativeValue valk = new QuantitativeValue();
		valk.setValue(0.012);
		valk.setUnitOfMeasurement("USD");
		gboutcost_z2c.setValue(valk);
		
		//pf2.setStringFunction("("+NGBOut_z1.getName() +"*"+gboutcost_z1.getName() + "+" + NGBOut_z2.getName() + "*" + gboutcost_z2.getName() + ")*6");
		
		Provider VMCost6mb = new Provider();
		pf6.addProviderVariable(VMCost6mb);
		VMCost6mb.setName("VMCost6m"+"TIME"+System.nanoTime());
		
		QuantitativeValue vall = new QuantitativeValue();
		vall.setValue(0.036);
		vall.setUnitOfMeasurement("EUR");
		VMCost6mb.setValue(vall);
		
		//pf1.setStringFunction( "6*31*`24" + "*" +VMCost6m.getName());
		//€350-€11,149 -> 22.5%
		//€11,150 - €29,799 ->25.5%
		//€29,800 and above -> 29.5%
		pf6.setStringFunction("IF ( (6*31*24" + "*" +VMCost6mb.getName()+") + (("+NGBOut_z1c.getName() +"*"+gboutcost_z1c.getName() + "+" + NGBOut_z2c.getName() + "*" + gboutcost_z2c.getName() + ")*6) + ("+"6*(" + NGBbrc.getName()+  "*" +backupcostc.getName()+ "))) > 350  &&"+
				"( (6*31*24" + "*" +VMCost6mb.getName()+") + (("+NGBOut_z1c.getName() +"*"+gboutcost_z1c.getName() + "+" + NGBOut_z2c.getName() + "*" + gboutcost_z2c.getName() + ")*6) + ("+"6*(" + NGBbrc.getName()+  "*" +backupcostc.getName()+ "))) < 11149  ;"+
				"( (6*31*24" + "*" +VMCost6mb.getName()+") + (("+NGBOut_z1c.getName() +"*"+gboutcost_z1c.getName() + "+" + NGBOut_z2c.getName() + "*" + gboutcost_z2c.getName() + ")*6) + ("+"6*(" + NGBbrc.getName()+  "*" +backupcostc.getName()+ "))) * (1-0.225) "+ "~" +
				"ELSEIF ( (6*31*24" + "*" +VMCost6mb.getName()+") + (("+NGBOut_z1c.getName() +"*"+gboutcost_z1c.getName() + "+" + NGBOut_z2c.getName() + "*" + gboutcost_z2c.getName() + ")*6) + ("+"6*(" + NGBbrc.getName()+  "*" +backupcostc.getName()+ "))) >= 11149  &&"+
				"( (6*31*24" + "*" +VMCost6mb.getName()+") + (("+NGBOut_z1c.getName() +"*"+gboutcost_z1c.getName() + "+" + NGBOut_z2c.getName() + "*" + gboutcost_z2c.getName() + ")*6) + ("+"6*(" + NGBbrc.getName()+  "*" +backupcostc.getName()+ "))) < 29799  ;"+
				"( (6*31*24" + "*" +VMCost6mb.getName()+") + (("+NGBOut_z1c.getName() +"*"+gboutcost_z1c.getName() + "+" + NGBOut_z2c.getName() + "*" + gboutcost_z2c.getName() + ")*6) + ("+"6*(" + NGBbrc.getName()+  "*" +backupcostc.getName()+ "))) * (1-0.255) "+ "~" +
				"ELSEIF ( (6*31*24" + "*" +VMCost6mb.getName()+") + (("+NGBOut_z1c.getName() +"*"+gboutcost_z1c.getName() + "+" + NGBOut_z2c.getName() + "*" + gboutcost_z2c.getName() + ")*6) + ("+"6*(" + NGBbrc.getName()+  "*" +backupcostc.getName()+ "))) >= 29799 ;" +
				"( (6*31*24" + "*" +VMCost6mb.getName()+") + (("+NGBOut_z1c.getName() +"*"+gboutcost_z1c.getName() + "+" + NGBOut_z2c.getName() + "*" + gboutcost_z2c.getName() + ")*6) + ("+"6*(" + NGBbrc.getName()+  "*" +backupcostc.getName()+ "))) * (1-0.295) "+ "~"
				
				);
		
		// END

		ArrayList<Offering> offs = new ArrayList<Offering>();
		offs.add(of);

		jmodel.setOfferings(offs);
		
	}
}
