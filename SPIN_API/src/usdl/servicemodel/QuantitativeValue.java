package usdl.servicemodel;

import usdl.constants.enums.GREnum;
import usdl.constants.enums.Prefixes;
import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.USDLPriceEnum;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;


/**
 * The QuantitativeValue class represents an instance of a QuantitativeValue resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 06
 */
public class QuantitativeValue extends Value {
	private double value = -1;
	private double minValue = -1;
	private double maxValue = -1;
	private String unitOfMeasurement = null;
	
	public QuantitativeValue() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public QuantitativeValue(QuantitativeValue source)  {//copy constructor
		
		if(source.getName() != null)
			this.setName(source.getName());
		
		if(source.getComment() != null)
			this.setComment(source.getComment());
		
		if(source.getTypes().size() > 0)
		{
			for(String s : source.getTypes())
				this.addType(s);
		}
		
		if(source.getMaxValue() >= 0)
			this.setMaxValue(source.getMaxValue());
		
		if(source.getMinValue() >= 0)
			this.setMinValue(source.getMinValue());
		
		if(source.getUnitOfMeasurement() != null)
			this.setUnitOfMeasurement(source.getUnitOfMeasurement());
		
		if(source.getValue() >= -1)
			this.setValue(source.getValue());
    }

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	@Override
	public String toString() {
		return "QuantitativeValue [value=" + value + ", minValue=" + minValue
				+ ", maxValue=" + maxValue + ", unitOfMeasurement="
				+ unitOfMeasurement + ",Name="+this.getName() +"  ,comment=  "+this.getComment() + ",types="+this.getTypes().toString() +"]";
	}
	
//	new functions
	
	/**
	 * Creates a Resource representation of the QuantitativeValue instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model,int mode,String baseURI)
	{
		Resource qv = null;
		if(this.getName() != null)
		{
			qv = model.createResource(baseURI +"#"  + this.getName().replaceAll(" ", "_") + "_TIME" + System.nanoTime());
			qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.GR.getPrefix() + "QuantitativeValue"));//rdf type
			for(String s : this.getTypes())
			{
				qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(s));//rdf type
			}
		}
		else
		{
			qv = model.createResource(baseURI +"#"  + "QuantitativeValue" + "_TIME" + System.nanoTime());
			qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(Prefixes.GR.getPrefix() + "QuantitativeValue"));//rdf type
			for(String s : this.getTypes())
			{
				qv.addProperty(RDFEnum.RDF_TYPE.getProperty(model), model.createResource(s));//rdf type
			}
		}
			

		if(this.getComment() != null)
			qv.addProperty(RDFSEnum.COMMENT.getProperty(model), this.getComment());
		
		if(this.unitOfMeasurement != null)
			qv.addProperty(GREnum.HAS_UNIT_OF_MEASUREMENT.getProperty(model), this.unitOfMeasurement);
		
		if(this.maxValue >= 0)
			qv.addLiteral(GREnum.HAS_MAX_VALUE.getProperty(model), this.maxValue);
		
		if(this.minValue >= 0)
			qv.addLiteral(GREnum.HAS_MIN_VALUE.getProperty(model), this.minValue);
		
		if(this.value >= 0)
			qv.addLiteral(GREnum.HAS_VALUE.getProperty(model), this.value);
		
		if(mode == 0)
			owner.addProperty(GREnum.QUANT_PROD_OR_SERV.getProperty(model),qv);
		else if(mode == 1)
			owner.addProperty(USDLPriceEnum.HAS_VALUE.getProperty(model),qv);
		else
			owner.addProperty(USDLPriceEnum.HAS_METRICS.getProperty(model),qv);
	}
	
	/**
	 * Reads a QuantitativeValue object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceComponent.
	 * @param   model   Model where the resource is located.
	 * @return  A QuantitativeValue object populated with its information extracted from the Semantic Model.
	 */
	public static QuantitativeValue readFromModel(Resource resource,Model model)
	{
		QuantitativeValue val = new QuantitativeValue();
		
		if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
			val.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
		else
		{
			if(resource.getLocalName() != null)
				val.setName(resource.getLocalName().replaceAll("_TIME\\d+",""));
		}
			
		
		if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
			val.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
		
		if(resource.hasProperty(RDFEnum.RDF_TYPE.getProperty(model)))
		{
			StmtIterator iter = resource.listProperties(RDFEnum.RDF_TYPE.getProperty(model));
			while (iter.hasNext()) {
				String uri = iter.next().getObject().asResource().getURI();
				if(uri.toLowerCase().contains("quantitativevalue"))
					continue;
				else
					val.addType(uri);
			}
		}
		
		if(resource.hasProperty(GREnum.HAS_UNIT_OF_MEASUREMENT.getProperty(model)))
			val.setUnitOfMeasurement(resource.getProperty(GREnum.HAS_UNIT_OF_MEASUREMENT.getProperty(model)).getString());
		
		if(resource.hasProperty(GREnum.HAS_MAX_VALUE.getProperty(model)))
			val.setMaxValue(resource.getProperty(GREnum.HAS_MAX_VALUE.getProperty(model)).getDouble());
		
		if(resource.hasProperty(GREnum.HAS_MIN_VALUE.getProperty(model)))
			val.setMinValue(resource.getProperty(GREnum.HAS_MIN_VALUE.getProperty(model)).getDouble());
		
		if(resource.hasProperty(GREnum.HAS_VALUE.getProperty(model)))
			val.setValue(resource.getProperty(GREnum.HAS_VALUE.getProperty(model)).getDouble());
		
		return val;
	}
}