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

package exceptions;

public class InvalidLinkedUSDLModelException extends Exception{
	
	private ErrorEnum errorCode;
	private String[] errorArguments;
	
	public InvalidLinkedUSDLModelException(ErrorEnum error){
		super(error.getMessage());
		errorCode = error;
	}
	
	public InvalidLinkedUSDLModelException(ErrorEnum error, String... args){
		super(error.getMessage(args));
		errorCode = error;
		errorArguments = args;
	}

	public InvalidLinkedUSDLModelException(ErrorEnum error, Throwable cause, String... args){
		super(error.getMessage(args), cause);
		errorCode = error;
		errorArguments = args;
	}	

}
