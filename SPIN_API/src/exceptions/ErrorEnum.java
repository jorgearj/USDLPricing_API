package exceptions;

import java.text.MessageFormat;

import usdl.constants.enums.Prefixes;

public enum ErrorEnum {
	FILE_NOT_FOUND ("File not found"),
	NULL_RESOURCE ("Element \"{0}\" is linked to a null resource of the type {1}."),
	MISSING_RESOURCE_DATA ("Resource {0} is missing information about atribute \"{1}\""),
	NO_MODEL ("There is no RDF model."),
	NO_BASE_URI("Model without BaseURI."),
	MISSING_PREFIX("Missing necessary prefix: {0}"),
	DUPLICATE_RESOURCE("Attempted to create new resource \"{0}\" with already existing URI: {1}"),
	MISSING_CORE_PREFIX("Linked USDL core prefix not found"),
	MISSING_PRICE_PREFIX("Linked USDL price prefix not found"),
	NO_OFFERINGS_FOUND("The model has no Offerings. At least an Offering must be defined"),
	OFFERING_WITHOUT_SERVICE("ServiceOffering \"{0}\" has no included Services. At least one Service must be included."),
	OFFERING_WITHOUT_PRICEPLAN("ServiceOffering \"{0}\" has no PricePlan defined. A PricePlan must be defined."),
	PRICEPLAN_WITHOUT_COMPONENTS("PricePlan \"{0}\" has no PriceComponent defined. At least one PriceComponent must be defined."),
	COMPONENT_WITHOUT_PRICE("PriceComponent \"{0}\" has no mean to calculate its price. It must have either a PriceSpecification or a PriceFunction"),
	VARIABLE_WITHOUT_VALUE("PriceVariable \"{0}\" has no value assigned. A PriceVariable of the type {1} must have a value.");
	
	private String message;
 
	/**
	 * GR Enumerator constructor. 
	 * @param   msg   Error message.
	 */
	private ErrorEnum(String msg) {
		message = msg;
	}
 
	/**
	 * Return the string message of the enumerator element.
	 * @param args 	list of arguments to use in the error message
	 * @return   A String of the error message.
	 */
	public String getMessage(String... args){
		MessageFormat messageForm = new MessageFormat(message);
		
		return messageForm.format(args);
	}

}
