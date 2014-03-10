package usdl.servicemodel;

import org.topbraid.spin.model.Function;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.vocabulary.SPIN;

import usdl.constants.enums.Prefixes;
import Factories.RDFPropertiesFactory;
import Factories.RDFSPropertiesFactory;
import Factories.USDLPricePropertiesFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import usdl.servicemodel.*;
public class Usage extends PriceVariable {

	
	/**
	 * The Usage class represents an instance of an Usage resource of the LinkedUSDL Pricing model. 
	 * @author  Daniel Barrigas
	 * @author Jorge Araujo
	 * @version 1.0, March 06
	 */
	public Usage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Reads a Usage object from the Semantic Model. 
	 * @param   resource   The Resource object of the Usage variable.
	 * @param   model   Model where the resource is located.
	 * @return  A Usage object populated with its information extracted from the Semantic Model.
	 */
	public static Usage readFromModel(Resource resource,Model model)
	{
		Usage var = new Usage();
		
		RDFSPropertiesFactory rdfsprop= new RDFSPropertiesFactory(model);
		RDFPropertiesFactory rdfprop= new RDFPropertiesFactory(model);
		USDLPricePropertiesFactory priceprop = new USDLPricePropertiesFactory(model);
		
		if(resource.hasProperty(rdfsprop.label()))
			var.setName(resource.getProperty(rdfsprop.label()).getString());
		
		if(resource.hasProperty(rdfsprop.comment()))
			var.setComment(resource.getProperty(rdfsprop.comment()).getString());
		
		if(resource.hasProperty(priceprop.hasValue()))//if the resource has a value
		{
			Resource val = resource.getProperty(priceprop.hasValue()).getResource();
			if(val.hasProperty(rdfprop.type()))//if the value has a type
			{
				if(val.getProperty(rdfprop.type()).getResource().getLocalName().equals("QualitativeValue"))//check if the attribute is of the qualitative type
				{
					//var.setValue(QuantitativeValue.readFromModel(val,model));
				}
				else//it's of the quantitative type
				{
					//var.setValue(QuantitativeValue.readFromModel(val,model));
				}
			}
		}
			
		return var;
	}
	
	/**
	 * Creates a Resource representation of the Usage instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
		
		// Initialize system functions and templates
		SPINModuleRegistry.get().init();
		RDFSPropertiesFactory RDFSProp = new RDFSPropertiesFactory(model);
		RDFPropertiesFactory RDFProp = new RDFPropertiesFactory(model);
		USDLPricePropertiesFactory PriceProp = new USDLPricePropertiesFactory(model);
		Resource var = null;
		if (this.getName() != null) {
			var = model.createResource(Prefixes.BASE.getName() + this.getName());
			var.addProperty(RDFSProp.label(), this.getName());
			var.addProperty(RDFProp.type(), model.createResource(Prefixes.USDL_PRICE.getName() + "Usage"));
		}
		
		if(this.getComment() != null)
		{
			var.addProperty(RDFSProp.comment(), this.getComment());
		}
		
		if(this.getValue()!= null)
		{
			if(this.getValue() instanceof QualitativeValue)
			{
				QualitativeValue val = (QualitativeValue) this.getValue();
				//val.writeToModel(var,model);
			}
			else
			{
				QuantitativeValue val = (QuantitativeValue) this.getValue();
				//val.writeToModel(var,model);
			}
		}
		
		owner.addProperty(PriceProp.hasVariable(), var);
	}
	
}