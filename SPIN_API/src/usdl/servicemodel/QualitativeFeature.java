package usdl.servicemodel;

import java.util.ArrayList;

import usdl.constants.enums.GREnum;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLCoreEnum;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class QualitativeFeature extends Feature {
	private QualitativeValue value;
	
	public QualitativeFeature() {
		super();
	}


	@Override
	public String toString() {
		return super.toString() +", value= "+ value + "]";
	}


	public QualitativeValue getValue() {
		return value;
	}


	public void setValue(QualitativeValue value) {
		this.value = value;
	}

	/**
	 * Reads a QualitativeFeature object from the Semantic Model. 
	 * @param   resource   The Resource object of the Linked USDL ServiceOffering.
	 * @param   model   Model where the resource is located.
	 * @return  An QualitativeFeature object populated with its information extracted from the Semantic Model.
	 */
	public static QualitativeFeature readFromModel(Resource resource, Model model){
		
		QualitativeFeature qualFeature = new QualitativeFeature();
		
		
		//populate the Offering
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			qualFeature.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		else
			qualFeature.setName(resource.getLocalName());
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			qualFeature.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		
		return qualFeature;
	}
	
	/**
	 * Creates a Resource representation of the QualitativeFeature instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
		Resource offering = USDLCoreEnum.OFFERING.getResource(model);
		offering.addProperty(RDFEnum.RDF_TYPE.getProperty(model), USDLCoreEnum.OFFERING.getResource(model));//rdf type

	}
	
}