package Factories;

import usdl.servicemodel.LinkedUSDLModel;

import com.hp.hpl.jena.rdf.model.Model;

public class LinkedUSDLModelFactory {
	
	public static LinkedUSDLModel createEmptyModel(){
		return new LinkedUSDLModel();
	}
	
	public static LinkedUSDLModel createFromModel(Model model){
		LinkedUSDLModel linkedUSDL = new LinkedUSDLModel();
		linkedUSDL.readModel(model);
		return linkedUSDL;
	}

}
