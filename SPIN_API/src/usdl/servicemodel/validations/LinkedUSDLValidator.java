package usdl.servicemodel.validations;

import com.hp.hpl.jena.rdf.model.Model;

import exceptions.ErrorMessagesEnum;
import exceptions.InvalidLinkedUSDLModelException;

public class LinkedUSDLValidator {
	
	public static void validateModel(Model model) throws InvalidLinkedUSDLModelException{
		if(model == null){
			throw new InvalidLinkedUSDLModelException(ErrorMessagesEnum.NO_MODEL.getMessage());
		}		
	}

}
