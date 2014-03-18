package exceptions;

import usdl.constants.enums.Prefixes;

public enum ErrorMessagesEnum {
	NO_MODEL ("There is no RDF model."),
	NO_BASE_URI("Model without BaseURI."),
	MISSING_PREFIX("Missing necessary prefix.");
	
	private String message;
 
	/**
	 * GR Enumerator constructor. 
	 * @param   msg   Error message.
	 */
	private ErrorMessagesEnum(String msg) {
		message = msg;
	}
 
	/**
	 * Return the string message of the enumerator element. 
	 * @return   A String of the error message.
	 */
	public String getMessage(){
		return message;
	}

}
