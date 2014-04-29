package usdl.servicemodel.validations;

import usdl.constants.properties.PricingAPIProperties;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;

public class LinkedUSDLValidator {
	
	
	public LinkedUSDLValidator(){
		
	}
	
//	/**
//	 * Validates a LinkedUSDLModel instance regarding its Linked USDL structure.
//	 * @param   model   the LinkedUSDLModel instance to be validated
//	 * @throws InvalidLinkedUSDLModelException
//	 */
//	public void validateModel(LinkedUSDLModel model) throws InvalidLinkedUSDLModelException{
//		
//	}
	
	/**
	 * Validates an rdf model against the linked USDL schema.
	 * @param   model   the rdf model to be validated
	 * @throws InvalidLinkedUSDLModelException
	 */
	public void validateModel(Model model) throws InvalidLinkedUSDLModelException{
		if(model == null){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.NO_MODEL);
		}else{
			this.checkCorePrefix(model);
			this.checkPricePrefix(model);
		}
	}
	
	public void checkDuplicateURI(Model model, Resource resource) throws InvalidLinkedUSDLModelException{
		if(model.containsResource(resource))
			throw new InvalidLinkedUSDLModelException(ErrorEnum.DUPLICATE_RESOURCE, new String[]{resource.getLocalName(), resource.getNameSpace()});
	}
	
	private void checkCorePrefix(Model model) throws InvalidLinkedUSDLModelException{
		if(model.getNsURIPrefix(PricingAPIProperties.corePrefix) == null){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_CORE_PREFIX);
		}
	}
	
	private void checkPricePrefix(Model model) throws InvalidLinkedUSDLModelException{
		if(model.getNsURIPrefix(PricingAPIProperties.pricePrefix) == null){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_PRICE_PREFIX);
		}
	}
	
//	private void validateMandatoryStructure(LinkedUSDLModel model) throws InvalidLinkedUSDLModelException{
//		boolean valid = false;
//		
//		for(Offering off : model.getOfferings()){
//			if(off != null){
//				valid = true;
//				break;
//			}
//			
//		}
//		if(!valid){
//			throw new InvalidLinkedUSDLModelException(ErrorMessagesEnum.NO_OFFERINGS_FOUND.getMessage());
//
//	}

}
