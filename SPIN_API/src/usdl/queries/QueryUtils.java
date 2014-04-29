package usdl.queries;

import usdl.constants.enums.Prefixes;

public class QueryUtils {
	
	public static String startQueryWithBasicPrefixes(){
		String query = 
				" PREFIX "+Prefixes.USDL_CORE.getName()+": <"+ Prefixes.USDL_CORE.getPrefix()+"> " +
		        " PREFIX "+Prefixes.USDL_PRICE.getName()+": <"+ Prefixes.USDL_PRICE.getPrefix()+"> " +		
				" PREFIX "+Prefixes.PF.getName()+": <"+Prefixes.PF.getPrefix()+"> " +
		        " PREFIX "+Prefixes.RDF.getName()+": <"+Prefixes.RDF.getPrefix()+"> " +
				" PREFIX "+Prefixes.RDFS.getName()+": <"+Prefixes.RDFS.getPrefix()+"> " + 
				" PREFIX "+Prefixes.GR.getName()+": <"+Prefixes.GR.getPrefix()+"> " +
				" PREFIX "+Prefixes.XSD.getName()+": <"+Prefixes.XSD.getPrefix()+"> "+
				" PREFIX "+Prefixes.CLOUD.getName()+": <"+ Prefixes.CLOUD.getPrefix()+">" +
				" PREFIX "+Prefixes.SPIN.getName()+": <"+ Prefixes.SPIN.getPrefix()+">";

		
		return query;		
	}
	
	public static String appendPrefix(String query){
		return query;
	}

}
