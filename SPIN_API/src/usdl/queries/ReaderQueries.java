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
	
	public static String findResource(String uri, String classType){
		String query = QueryUtils.startQueryWithBasicPrefixes();
		query = query +
				" SELECT REDUCED ?r" +
				" WHERE { " +
					" ?r"+" a"+" "+classType+" . " + 
					" FILTER(str(?r) = \""+uri+"\" ) . " + 
				" }";
		
		return query;
	}
	
	
	public static String readAllServices(){
		String query = "";
		
		return query;
	}

}
