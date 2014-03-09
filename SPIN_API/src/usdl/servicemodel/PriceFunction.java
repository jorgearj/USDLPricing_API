package usdl.servicemodel;


import java.util.ArrayList;
import java.util.List;

import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.model.Function;
import org.topbraid.spin.model.Query;
import org.topbraid.spin.model.Select;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.vocabulary.SPIN;

import usdl.constants.enums.Prefixes;
import Factories.RDFPropertiesFactory;
import Factories.RDFSPropertiesFactory;
import Factories.USDLPricePropertiesFactory;
import FunctionParser.MathExp2SPARQL;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;


/**
 * The PriceFunction class represents an instance of a PriceFunction resource of the LinkedUSDL Pricing model. 
 * @author  Daniel Barrigas
 * @author Jorge Araujo
 * @version 1.0, March 09
 */
public class PriceFunction {
	private String name = null;
	private String stringFunction = null;
	private String SPARQLFunction = null;
	private List<Usage> usageVariables = null;
	private List<Provider> providerVariables = null;
	private List<Constraint> constraints = null;
	private String comment = null;
	
	public PriceFunction() {
		super();
		usageVariables = new ArrayList<Usage>();
		providerVariables = new ArrayList<Provider>();
		constraints = new ArrayList<Constraint>();
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStringFunction() {
		return stringFunction;
	}
	public void setStringFunction(String stringFunction) {
		this.stringFunction = stringFunction;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "PriceFunction [name=" + name + ", stringFunction="
				+ stringFunction + ", Usage variables=" + usageVariables
				+ ", Provider variables=" + providerVariables
				+ ", constraints=" + constraints + ", comment=" + comment + "]";
	}
	public List<Usage> getUsageVariables() {
		return usageVariables;
	}
	public void setUsageVariables(List<Usage> variables) {
		usageVariables = variables;
	}
	public List<Provider> getProviderVariables() {
		return providerVariables;
	}
	public void setProviderVariables(List<Provider> variables) {
		providerVariables = variables;
	}
	public String getSPARQLFunction() {
		return SPARQLFunction;
	}
	public void setSPARQLFunction(String sPARQLFunction) {
		SPARQLFunction = sPARQLFunction;
	}
	
	// New Functions
	
	/**
	 * Adds a UsageVariable to the Function
	 * @param   val   UsageVariable related to the Function
	 */
	public void addUsageVariable(Usage var)
	{
		this.usageVariables.add(var);
	}
	
	/**
	 * Removes a UsageVariable from the Function
	 * @param   index   UsageVariable's position
	 */
	public void removeUsageVariableAtIndex(int index)
	{
		this.usageVariables.remove(index);
	}
	
	/**
	 * Adds a ProviderVariable to the Function
	 * @param   val   ProviderVariable related to the Function
	 */
	public void addProviderVariable(Provider var)
	{
		this.providerVariables.add(var);
	}
	
	/**
	 * Removes a ProviderVariable from the Function
	 * @param   index   ProviderVariable's position
	 */
	public void removeProviderVariableAtIndex(int index)
	{
		this.providerVariables.remove(index);
	}
	
	/**
	 * Adds a Constraint to the Function. A constraint can be something like IF windows = 'Linux' or IF usageTime > 580
	 * @param   val   Constraint of the Function
	 */
	public void addConstraint(Constraint constraint)
	{
		this.constraints.add(constraint);
	}
	
	/**
	 * Removes a Constraint from the Function
	 * @param   index   Constraint's position
	 */
	public void removeConstraintAtIndex(int index)
	{
		this.constraints.remove(index);
	}
	
	/**
	 * Creates a Resource representation of the PriceFunction instance and writes it into the passed model.
	 * @param   owner    Resource that is linked to this object.
	 * @param   model    Model to where the object is to be written on.
	 */
	public void writeToModel(Resource owner,Model model)
	{
		
		// Initialize system functions and templates
		SPINModuleRegistry.get().init();
		USDLPricePropertiesFactory PriceProp = new USDLPricePropertiesFactory(model);
		RDFSPropertiesFactory RDFSProp = new RDFSPropertiesFactory(model);

		
		Function func = null;
		if(this.name != null)
		{
			func = model.createResource(Prefixes.BASE.getName() + this.name, SPIN.Function).as(Function.class);
			func.addProperty(RDFSProp.label(), this.name);
		}
		
		if(this.stringFunction != null)// Create a function from the string stringFunction
		{
			MathExp2SPARQL parser = new MathExp2SPARQL(this.stringFunction,this.providerVariables,this.usageVariables);
			//ArrayList<String> detectedVariables = parser.getParsedVariables();
			com.hp.hpl.jena.query.Query arqQuery = ARQFactory.get().createQuery(model,parser.getSPARQLQuery());
			Query spinQuery = new ARQ2SPIN(model).createQuery(arqQuery, null);
			func.addProperty(SPIN.body, spinQuery);
		}
		
		if(this.comment != null)
			func.addProperty(RDFSProp.comment(), this.comment);
		
		for(Usage var : this.usageVariables)
			var.writeToModel(func,model);
		
		for(Provider var2 : this.providerVariables)
			var2.writeToModel(func,model);
		
		owner.addProperty(PriceProp.hasPriceFunction(), func);//link the Price Component with the Price Plan
	}
	
	
	/**
	 * Reads a PriceFunction object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceFunction.
	 * @param   model   Model where the resource is located.
	 * @return  A PriceFunction object populated with its information extracted from the Semantic Model.
	 */
	public static PriceFunction readFromModel(Resource resource,Model model)
	{
		
		SPINModuleRegistry.get().init();
		PriceFunction pf = new PriceFunction();
		
		RDFSPropertiesFactory rdfsprop= new RDFSPropertiesFactory(model);
		USDLPricePropertiesFactory priceprop = new USDLPricePropertiesFactory(model);

		//populate the PriceFunction
		if(resource.hasProperty(rdfsprop.comment()))
			pf.setComment(resource.getProperty(rdfsprop.comment()).getString());
		
		if(resource.hasProperty(rdfsprop.label()))
			pf.setName(resource.getProperty(rdfsprop.label()).getString());
		
		Function ff = SPINModuleRegistry.get().getFunction(resource.getURI(), model);//get the function from the model
		com.hp.hpl.jena.query.Query narq = ARQFactory.get().createQuery((Select)ff.getBody());//transform the spin objects that define the function into a SPARQL query
		//QueryExecution qexecc = ARQFactory.get().createQueryExecution(narq, model);//function execution
		//ResultSet rsc = qexecc.execSelect();//function execution
		if(narq != null)
			pf.setSPARQLFunction(narq.toString());//we might need a reverse parser to extract the original formula from the SPARQL query
		
		if(resource.hasProperty(priceprop.hasVariable()))
		{
			//get metrics
			StmtIterator iter = resource.listProperties(priceprop.hasVariable());
			while (iter.hasNext()) {//while there's price metrics  left
				Resource variable = iter.next().getObject().asResource();
				if(variable.hasProperty(rdfsprop.subClassOf()))//if variable has subClassOf property
				{
					if(variable.getProperty(rdfsprop.subClassOf()).getResource().getLocalName().equals("Usage"))
						pf.addUsageVariable(Usage.readFromModel(variable,model));
					else
						pf.addProviderVariable(Provider.readFromModel(variable,model));
				}
			}
		}
		
		return pf;
	}
}