package usdl.servicemodel;

import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLCoreEnum;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class QuantitativeFeature extends Feature {
	private QuantitativeValue value;

	public QuantitativeFeature() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return super.toString() +", value= "+ value + "]";
	}

	public QuantitativeValue getValue() {
		return value;
	}

	public void setValue(QuantitativeValue value) {
		this.value = value;
	}
	
	/**
	 * Reads a QuantitativeFeature object from the Semantic Model. 
	 * @param   resource   The Resource object of the Linked USDL ServiceOffering.
	 * @param   model   Model where the resource is located.
	 * @return  An QuantitativeFeature object populated with its information extracted from the Semantic Model.
	 */
	public static QuantitativeFeature readFromModel(Resource resource, Model model){
		
		QuantitativeFeature quantFeature = new QuantitativeFeature();
		
		
		//populate the Offering
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			quantFeature.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		else
			quantFeature.setName(resource.getLocalName());
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			quantFeature.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		
		return quantFeature;
	}
	
	/**
	 * Creates a Resource representation of the QuantitativeFeature instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
		Resource offering = USDLCoreEnum.OFFERING.getResource(model);
		offering.addProperty(RDFEnum.RDF_TYPE.getProperty(model), USDLCoreEnum.OFFERING.getResource(model));//rdf type

	}

}