 /*  ----------------------------------------------------------------------------------------
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

package usdl.constants.enums;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public enum RDFEnum {
	
	RDF_TYPE	("type",	"P");
	 
	private String property;
	private String type;
 
	private RDFEnum(String p, String t) {
		property = p;
		type = t;
	}
 
	public String getPropertyString(){
		return Prefixes.RDF.getName() + ":" + property;
	}
	
	public Property getProperty(Model model) {
		if(type.equalsIgnoreCase("P"))
			return model.createProperty(Prefixes.RDF.getPrefix() + property);
		else{
			return null;
		}
	}
	
	/**
	 * Return a ready to use Jena Resource of the Enumerator concept
	 * @param   model  Semantic model where the property is located.
	 * @return   A Jena Resource.
	 */
	public Resource getResource(Model model) {
		if(type.equalsIgnoreCase("C")){
			return model.createResource(Prefixes.RDF.getPrefix()  + property);
		}else{
			return null;
		}
	}

}
