package usdl.queries;

import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.USDLCoreEnum;

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
	
	public static String readAllServices(){
		String query = "";
		
		return query;
	}

}
