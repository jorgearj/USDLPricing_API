package usdl.servicemodel.validations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import exceptions.ErrorMessagesEnum;
import exceptions.InvalidLinkedUSDLModelException;

public class LinkedUSDLValidator {
	
	public static void validateModel(Model model) throws InvalidLinkedUSDLModelException{
		if(model == null){
			throw new InvalidLinkedUSDLModelException(ErrorMessagesEnum.NO_MODEL.getMessage());
		}		
	}
	
	public static void checkDuplicateURI(Model model, Resource resource) throws InvalidLinkedUSDLModelException{
		System.out.println(resource.getURI());
		if(model.containsResource(resource))
			throw new InvalidLinkedUSDLModelException(ErrorMessagesEnum.DUPLICATE_RESOURCE.getMessage());
	}

}
