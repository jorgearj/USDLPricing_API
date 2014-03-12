package usdl.servicemodel;

import org.topbraid.spin.system.SPINModuleRegistry;

import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLPriceEnum;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class Provider extends PriceVariable {
	
	
	/**
	 * The Provider class represents an instance of a Constant resource of the LinkedUSDL Pricing model. 
	 * @author  Daniel Barrigas
	 * @author Jorge Araujo
	 * @version 1.0, March 06
	 */
	public Provider() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Creates a Resource representation of the Provider instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
		
		// Initialize system functions and templates
		Resource var = null;
		
		if (this.getName() != null) {
			var = model.createResource(Prefixes.BASE.getName() + this.getName());
			var.addProperty(RDFSEnum.LABEL.getProperty(model), this.getName());
			var.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.USDL_PRICE.getName() + "Constant"));
		}
		
		if(this.getComment() != null)
		{
			var.addProperty(RDFSEnum.COMMENT.getProperty(model), this.getComment());
		}
		
		if(this.getValue()!= null)
		{
			if(this.getValue() instanceof QualitativeValue)
			{
				QualitativeValue val = (QualitativeValue) this.getValue();
				val.writeToModel(var,model);
			}
			else
			{
				QuantitativeValue val = (QuantitativeValue) this.getValue();
				val.writeToModel(var,model);
			}
		}
		
		owner.addProperty(USDLPriceEnum.HAS_VARIABLE.getProperty(model), var);
	}
	
	
	/**
	 * Reads a Provider object from the Semantic Model. 
	 * @param   resource   The Resource object of the Usage variable.
	 * @param   model   Model where the resource is located.
	 * @return  A Provider object populated with its information extracted from the Semantic Model.
	 */
	public static Provider readFromModel(Resource resource,Model model)
	{
		Provider var = new Provider();

		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			var.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		else
			var.setName(resource.getLocalName());
			
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			var.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		if(resource.hasProperty(USDLPriceEnum.HAS_VALUE.getProperty(model)))//if the resource has a value
		{
			Resource val = resource.getProperty(USDLPriceEnum.HAS_VALUE.getProperty(model)).getResource();//fetch the resource
			if(val.hasProperty(RDFEnum.RDF_TYPE.getProperty(model)))//if the value has a type
			{
				if(val.getProperty(RDFEnum.RDF_TYPE.getProperty(model)).getResource().getLocalName().equals("QualitativeValue"))//check if the attribute is of the qualitative type
				{
					var.setValue(QuantitativeValue.readFromModel(val,model));
				}
				else//it's of the quantitative type
				{
					var.setValue(QuantitativeValue.readFromModel(val,model));
				}
			}
		}
			
		return var;
	}
}
