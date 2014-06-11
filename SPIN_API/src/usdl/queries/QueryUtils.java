/* *  ----------------------------------------------------------------------------------------
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
