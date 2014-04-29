package usdl.queries;



import usdl.constants.enums.RDFEnum;

import usdl.constants.enums.USDLCoreEnum;
import usdl.constants.enums.USDLPriceEnum;

public class ReaderQueries {

	public static String readAllOfferings(String variableName){
		String query = QueryUtils.startQueryWithBasicPrefixes();
		query = query +
				" SELECT REDUCED ?"+variableName +
				" WHERE { " +
					" ?"+variableName+" "+RDFEnum.RDF_TYPE.getPropertyString()+" "+USDLCoreEnum.OFFERING.getPropertyString()+" . " + 
				" }";
		
		return query;
	}
	
	public static String readAllUsageVariables(String variableName){
		String query = QueryUtils.startQueryWithBasicPrefixes();
		query = query +
				" SELECT REDUCED ?pv" +
				" WHERE { " +
					" ?"+variableName+" "+RDFEnum.RDF_TYPE.getPropertyString()+" "+USDLCoreEnum.OFFERING.getPropertyString()+" . " + 
					" ?"+variableName+" "+USDLPriceEnum.HAS_PRICE_PLAN.getPropertyString()+" "+"?pp"+" . " + 
					" ?"+"pp"+" "+USDLPriceEnum.HAS_PRICE_COMPONENT.getPropertyString()+" "+"?pc"+" . " + 
					" ?"+"pc"+" "+USDLPriceEnum.HAS_PRICE_FUNCTION.getPropertyString()+" "+"?pf"+" . " + 
					" ?"+"pf"+" "+USDLPriceEnum.HAS_VARIABLE.getPropertyString()+" "+"?pv"+" . " + 
					" ?"+"pv"+" "+RDFEnum.RDF_TYPE.getPropertyString()+" "+USDLPriceEnum.USAGE.getPropertyString()+" . " + 
				" }";
		
		return query;
	}
	
	
	public static String readAllServices(){
		String query = "";
		
		return query;
	}

}
