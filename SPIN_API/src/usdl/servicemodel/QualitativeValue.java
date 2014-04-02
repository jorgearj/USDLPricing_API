package usdl.servicemodel;

import usdl.constants.enums.GREnum;
import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLPriceEnum;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
//http://www.heppnetz.de/ontologies/goodrelations/v1.html#QualitativeValue
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * The QualitativeValue class represents an instance of a QualitativeValue resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class QualitativeValue extends Value {
	public String hasLabel = null;

	public QualitativeValue() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public QualitativeValue(QualitativeValue source)  {//copy constructor
		
		if(source.getName() != null)
			this.setName(source.getName());
		
		if(source.getComment() != null)
			this.setComment(source.getComment());
		
		if(source.getTypes().size() > 0)
		{
			for(String s : source.getTypes())
				this.addType(s);
		}
		
		if(source.getHasValue() != null)
			this.setHasLabel(source.getHasValue());
    }
	

	public String getHasValue() {
		return hasLabel;
	}

	public void setHasLabel(String label) {
		this.hasLabel = label;
	}

	@Override
	public String toString() {
		return "QualitativeValue [hasLabel=" + hasLabel + ",types ="+this.getTypes().toString()+ ",comment ="+this.getComment()+"]" ;
	}
	
	//New functions
	
	/**
	 * Creates a Resource representation of the QualitativeValue instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model,int mode,String baseURI)
	{
		Resource qv = null;
		if(this.getName() != null)
		{
			qv = model.createResource(baseURI +"#"  + this.getName().replaceAll(" ", "_") + "_TIME" + System.nanoTime());
			qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.GR.getPrefix() + "QualitativeValue"));//rdf type
			for(String s : this.getTypes())
			{
				qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(s));//rdf type
			}
		}
		else
		{
			qv = model.createResource(baseURI +"#"  + "QualitativeValue" + "_TIME" + System.nanoTime());
			qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.GR.getPrefix() + "QualitativeValue"));//rdf type
			for(String s : this.getTypes())
			{
				qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(s));//rdf type
			}
		}
		
		if(this.getComment() != null)
			qv.addProperty(RDFSEnum.COMMENT.getProperty(model), this.getComment());
		
		if(this.getHasValue() != null)
			qv.addProperty(RDFSEnum.LABEL.getProperty(model), this.getHasValue());
		
		if(mode == 0)
			owner.addProperty(GREnum.QUAL_PROD_OR_SERV.getProperty(model),qv);
		else
			owner.addProperty(USDLPriceEnum.HAS_VALUE.getProperty(model),qv);
	}
	
	/**
	 * Reads a QualitativeValue object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceComponent.
	 * @param   model   Model where the resource is located.
	 * @return  A QualitativeValue object populated with its information extracted from the Semantic Model.
	 */
	public static QualitativeValue readFromModel(Resource resource,Model model)
	{
		QualitativeValue val = new QualitativeValue();
		
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			val.setHasLabel((resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString()));
		
		if(resource.getLocalName() != null)
			val.setName(resource.getLocalName().replaceAll("_TIME\\D+",""));
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			val.setComment((resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString()));
		
		if(resource.hasProperty(RDFEnum.RDF_TYPE.getProperty(model)))
		{
			StmtIterator iter = resource.listProperties(RDFEnum.RDF_TYPE.getProperty(model));
			while (iter.hasNext()) {
				String uri = iter.next().getObject().asResource().getURI();
				if(uri.toLowerCase().contains("qualitativevalue"))
					continue;
				else
					val.addType(uri );
			}
		}
		
		return val;
	}
}