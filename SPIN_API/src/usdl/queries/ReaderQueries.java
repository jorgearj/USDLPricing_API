package usdl.queries;

import usdl.constants.enums.Prefixes;

public class ReaderQueries {

	public static String readAllOfferings(String variableName){
		String query = QueryUtils.startQueryWithBasicPrefixes();
		query = query +
				" SELECT REDUCED ?"+variableName +
				" WHERE { " +
					" ?"+variableName+" "+Prefixes.RDF.getName()+":type "+Prefixes.USDL_CORE.getName()+":ServiceOffering . " +
				" }";
		
		return query;
	}
	
	public static String readAllServices(){
		String query = "";
		
		return query;
	}

}
