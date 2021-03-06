/**
 * @author Daniel Guedes Barrigas - danielgbarrigas@hotmail.com / danielgbarrigas@gmail.com
 * 
 * Uses the LinkedUSDL Pricing API to model a Spot instance Instance from Amazon EC2.
 * Info about their offerings can be seen at: https://aws.amazon.com/ec2/pricing/
 * The pricing method adopted by Amazon's Spot Instances is a VB bundled Spot Instance model
 * It also makes use of the json.simple library in order to extract the price on the instance directly from the JSON provided by Amazon EC2. This way, any time the price is updated accoding to the market needs, this program will be able to extract the new price
 * as long as the structure of the JSON document remains the same.
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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



public class AmazonSpotInstaces {
	private static String url = "http://aws-assets-pricing-prod.s3.amazonaws.com/pricing/ec2/spot.js";
	public static void main(String[] args) throws MalformedURLException, IOException, ParseException, InvalidLinkedUSDLModelException
	{
		LinkedUSDLModel jmodel;

		jmodel = LinkedUSDLModelFactory.createEmptyModel();
		
		AmazonSpotInstanceOffering(jmodel);
		
		jmodel.setBaseURI("http://PricingAPIAmazonSpotInstance.com");
		Model instance = jmodel.WriteToModel();//transform the java models to a semantic representation

		File outputFile = new File("./DebuggingFiles/amazonSI.ttl");
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(outputFile);
		instance.write(out, "Turtle");
		out.close();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static void AmazonSpotInstanceOffering(LinkedUSDLModel jmodel) throws IOException, ParseException
	{	
		
		//First, we fetch the JSON String from AmazonEC2
		
		String JsonString = IOUtils.toString(new URL(url).openStream());
		JsonString = JsonString.substring(9, JsonString.length()-1);
		
        JSONParser parser = new JSONParser(); 
        Object obj = parser.parse(JsonString);  
        
        JSONObject jsonObject = (JSONObject) obj;  
       
        //Double version = (Double) jsonObject.get("vers");  
       
        JSONObject configs = (JSONObject) jsonObject.get("config");
       
        JSONArray regions = (JSONArray) (configs.get("regions"));  
        Iterator<JSONObject> iterator = regions.iterator();  
        
        //first region
        JSONObject reg = iterator.next();
        //System.out.println(reg.get("region"));
        //String region = (String) reg.get("region");
        
        JSONArray instance_types = (JSONArray) reg.get("instanceTypes");
        Iterator<JSONObject> itemp = instance_types.iterator(); 
        
        JSONObject fdt= itemp.next();
        //System.out.println(fdt.get("type"));
        String instanceType = (String) fdt.get("type");
        
        JSONArray instance_details = (JSONArray) fdt.get("sizes");
        Iterator<JSONObject> itdetail = instance_details.iterator(); 
        
        JSONObject cont = itdetail.next();
        //System.out.println(cont.get("size"));
        String instname = (String) cont.get("size");
        
        JSONArray osoption = (JSONArray) cont.get("valueColumns");
        Iterator<JSONObject> osit = osoption.iterator(); 
        
        JSONObject osj = osit.next();
        //System.out.println(osj.get("name"));
        //String OSName = (String) osj.get("name");
        
        JSONObject instprice = (JSONObject) osj.get("prices");
       // System.out.println(osprice.get("USD"));
		Double instpricej = (Double.parseDouble( (String)instprice.get("USD")));
		
		//first, create the services 
		Service s1 = new Service();


		s1.setName(instname+"-SI-"+instanceType);

		ArrayList<QuantitativeValue> s1QuantFeat = new ArrayList<QuantitativeValue>();//container for the Quantitative Features
		ArrayList<QualitativeValue> s1QualFeat = new ArrayList<QualitativeValue>();//container for the Qualitative Features
		
		//64 bits	1	3	3,75	1 x 4 SSD*6	�	Moderada
		QualitativeValue Arch = new QualitativeValue();
		Arch.addType(CLOUDEnum.BIT64.getConceptURI());
		Arch.setHasLabel("64Bits");
		s1QualFeat.add(Arch);
		
		QuantitativeValue CPUCores=null,CPUSpeed=null;
		CPUCores =new QuantitativeValue();
		CPUSpeed = new QuantitativeValue();
		
		CPUCores.addType(CLOUDEnum.CPUCORES.getConceptURI());
		CPUCores.setValue(4);
	
		CPUSpeed.addType(CLOUDEnum.CPUSPEED.getConceptURI());
		CPUSpeed.setValue(13);
		CPUSpeed.setUnitOfMeasurement("ECU");//EC2 Compute Unit
		s1QuantFeat.add(CPUCores);
		s1QuantFeat.add(CPUSpeed);
		
		// /MEMORYSIZE
		QuantitativeValue MemorySize =  new QuantitativeValue();
		MemorySize.addType(CLOUDEnum.MEMORYSIZE.getConceptURI());
		MemorySize.setValue(15);
		MemorySize.setUnitOfMeasurement("E34");//GB
		s1QuantFeat.add(MemorySize);
		
		
		//DiskSize, StorageType
		QuantitativeValue DiskSize = new QuantitativeValue();
		QualitativeValue StorageType = new QualitativeValue();

		DiskSize.addType(CLOUDEnum.DISKSIZE.getConceptURI());
		DiskSize.setValue(80);
		DiskSize.setUnitOfMeasurement("E34");// GB

		StorageType.addType(CLOUDEnum.STORAGETYPE.getConceptURI());
		StorageType.setHasLabel("SSD");
		s1QuantFeat.add(DiskSize);
		s1QualFeat.add(StorageType);
		
		//MONITORING
		QualitativeValue monitoring = new QualitativeValue();
		monitoring.addType(CLOUDEnum.MONITORING.getConceptURI());
		monitoring.setHasLabel("Basic");
		monitoring.setComment("As m�tricas de monitoramento b�sico (com frequ�ncia de cinco minutos) para inst�ncias do Amazon EC2 s�o gratuitas, assim como todas as m�tricas para os volumes do Amazon EBS, Elastic Load Balancers e as inst�ncias do banco de dados do Amazon RDS.");
		s1QualFeat.add(monitoring);
		
		//Performance
		QualitativeValue performance = new QualitativeValue();
		performance.addType(CLOUDEnum.PERFORMANCE.getConceptURI());
		performance.setHasLabel("Moderate");
		
		//OS
		QualitativeValue os = new QualitativeValue();
		os.addType(CLOUDEnum.UNIX.getConceptURI());
		os.setHasLabel("Linux");
		s1QualFeat.add(os);
		
		
		//Location
		QualitativeValue Location = new QualitativeValue();
		Location.addType(CLOUDEnum.LOCATION.getConceptURI());
		Location.setHasLabel("East USA - North Virginia");
		
		//DATA, in order to simplify the modeling of the offering, we'll only consider traffic from the internet into the EC2 instance and from the EC2 instance to the internet

		QuantitativeValue DATAINEXTERNAL = new QuantitativeValue();
		QuantitativeValue DATAININTERNAL  = new QuantitativeValue();
		QuantitativeValue DATAOUTEXTERNAL  = new QuantitativeValue();
		//QuantitativeValue DATAOUTINTERNAL  = new QuantitativeValue(); 
		
		DATAINEXTERNAL.addType(CLOUDEnum.DATAINEXTERNAL.getConceptURI());
		DATAINEXTERNAL.setValue(Double.MAX_VALUE);//unlimited
		s1QuantFeat.add(DATAINEXTERNAL);
		
		DATAININTERNAL.addType(CLOUDEnum.DATAININTERNAL.getConceptURI());
		DATAININTERNAL.setValue(Double.MAX_VALUE);//unlimited
		s1QuantFeat.add(DATAININTERNAL);
		
		DATAOUTEXTERNAL.addType(CLOUDEnum.DATAOUTEXTERNAL.getConceptURI());
		DATAOUTEXTERNAL.setMaxValue(350*1024);//let's assume a maximum of 350TB since in their website there isn't any detailed info about their pricing for transferrals of 350TB++
		DATAOUTEXTERNAL.setUnitOfMeasurement("E34");
		
		s1QuantFeat.add(DATAOUTEXTERNAL);
		
		s1.setQuantfeatures(s1QuantFeat);
		s1.setQualfeatures(s1QualFeat);
		//now we create an offering and its priceplan for the created service.
		
		Offering of = new Offering();
		of.addService(s1);
		of.setName(instname+"-SI Offering");
		
		PricePlan pp = new PricePlan();
		of.setPricePlan(pp);
		
		pp.setName("PricePlan-"+instname+"-SI");
		
		PriceComponent pc_hourly = new PriceComponent();//Component that is responsible for calculating the price per hour of the instance
		pp.addPriceComponent(pc_hourly);
		pc_hourly.setName("HourlyPC-PP"+instname+"-SI");
		
		PriceFunction pf_hourly = new PriceFunction();
		pc_hourly.setPriceFunction(pf_hourly);
		pf_hourly.setName(instname+"-SI-hourly_cost");
		
		Usage NumberOfHours = new Usage();
		pf_hourly.addUsageVariable(NumberOfHours);
		NumberOfHours.setName("NumberOfHours"+"TIME"+System.nanoTime());
		NumberOfHours.setComment("The number of hours that you'll be using the instance.");
		
		Provider CostPerHour = new Provider();
		pf_hourly.addProviderVariable(CostPerHour);
		CostPerHour.setName("CostPerHour" + "TIME"+System.nanoTime());
		QuantitativeValue val = new QuantitativeValue();
		CostPerHour.setValue(val);
		val.setValue(instpricej);
		val.setUnitOfMeasurement("USD");
		
		pf_hourly.setStringFunction(CostPerHour.getName() + "*" +NumberOfHours.getName() );
		
		PriceComponent traffic_pc = new PriceComponent();//Component responsible for calculating the total price to pay related only to the Data transferral on Amazon EC2
		pp.addPriceComponent(traffic_pc);
		traffic_pc.setName("DataCostPC-PP"+instname+"-OD");
		
		PriceFunction data_cost_pf = new PriceFunction();
		traffic_pc.setPriceFunction(data_cost_pf);
		data_cost_pf.setName(instname+"-SI-data_transferrals_cost");
		
		Provider price10 = new Provider();
		data_cost_pf.addProviderVariable(price10);
		price10.setName("price10"+"TIME"+System.nanoTime());
		QuantitativeValue valb = new QuantitativeValue();
		price10.setValue(valb);
		valb.setValue(0.12);
		valb.setUnitOfMeasurement("USD");
		
		Provider price40 = new Provider();
		data_cost_pf.addProviderVariable(price40);
		price40.setName("price40"+"TIME"+System.nanoTime());
		QuantitativeValue valc = new QuantitativeValue();
		price40.setValue(valc);
		valc.setValue(0.09);
		valc.setUnitOfMeasurement("USD");
		
		Provider price100 = new Provider();
		data_cost_pf.addProviderVariable(price100);
		price100.setName("price100"+"TIME"+System.nanoTime());
		QuantitativeValue vald = new QuantitativeValue();
		price100.setValue(vald);
		vald.setValue(0.07);
		vald.setUnitOfMeasurement("USD");
		
		Provider price350 = new Provider();
		data_cost_pf.addProviderVariable(price350);
		price350.setName("price350"+"TIME"+System.nanoTime());
		QuantitativeValue vale = new QuantitativeValue();
		price350.setValue(vale);
		vale.setValue(0.05);
		vale.setUnitOfMeasurement("USD");
		
		Usage gbout = new Usage();
		data_cost_pf.addUsageVariable(gbout);
		gbout.setName("gbout"+"TIME"+System.nanoTime());
		gbout.setComment("Total GB of data that you expect to send out from Amazon EC2 to the internet.");
		
		data_cost_pf.setStringFunction("  IF ("+gbout.getName()+"<= 1) ; 1 * 0.00 ~ ELSEIF"+gbout.getName()+" > 1 && "+gbout.getName()+" <= 10*1024 ; 1*0.00 + ("+gbout.getName()+"-1) * "+price10.getName()+" ~ ELSEIF ("+gbout.getName()+" > 10*1024) && ("+gbout.getName()+"<= 40*1024) ; 1*0.00 + 10*1024*"+price10.getName()+" + ("+gbout.getName()+"-10*1024-1)*"+price40.getName()+" ~ ELSEIF ("+gbout.getName()+" >= 40*1024) && ("+gbout.getName()+" < 100*1024) ; 1*0.00 + 10*1024*"+price10.getName()+" + 40*1024*"+price40.getName()+" + ("+gbout.getName()+"-1-10*1024-40*1024)*"+price100.getName()+" ~ ELSEIF ("+gbout.getName()+" >= 100*1024) && ("+gbout.getName()+" < 350*1024) ; 1*0.00 + 10*1024*"+price10.getName()+" + 40*1024*"+price40.getName()+" + 100*1024*"+price100.getName()+" + ("+gbout.getName()+"-1-10*1024-40*1024-100*1024)*"+price350.getName()+"");
	
	
		//END
		
		ArrayList<Offering> offs = new ArrayList<Offering>();
		offs.add(of);
		
		jmodel.setOfferings(offs);

	}
}
