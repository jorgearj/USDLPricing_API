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

import usdl.constants.enums.RDFEnum;
import usdl.constants.enums.RDFSEnum;
import usdl.constants.enums.ResourceNameEnum;
import usdl.constants.enums.USDLPriceEnum;
import usdl.constants.properties.PricingAPIProperties;
import usdl.servicemodel.validations.LinkedUSDLValidator;
import FunctionParser.MathExp2SPARQL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import exceptions.ErrorEnum;
import exceptions.InvalidLinkedUSDLModelException;


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
	private String localName = null;
	private String namespace = null;
	private final String resourceType = ResourceNameEnum.PRICEFUNCTION.getResourceType();
	
	
	public PriceFunction(){
		this(ResourceNameEnum.PRICEFUNCTION.getResourceName(), null);
	}
	
	public PriceFunction(String name){
		this(name, null);
	}
	
	public PriceFunction(String name, String nameSpace) {
		usageVariables = new ArrayList<Usage>();
		providerVariables = new ArrayList<Provider>();
		constraints = new ArrayList<Constraint>();
		this.setName(name);
		this.namespace = nameSpace;
	}
	
	public PriceFunction(PriceFunction source) {//copy constructor
		super();
		usageVariables = new ArrayList<Usage>();
		providerVariables = new ArrayList<Provider>();
		constraints = new ArrayList<Constraint>();

		if(source.getName() != null)
			this.setName(source.getName());

		if(source.getComment() != null)
			this.setComment(source.getComment());

		if(source.getSPARQLFunction() != null)
			this.setSPARQLFunction(source.getSPARQLFunction());

		if(source.getStringFunction() != null)
			this.setStringFunction(source.getStringFunction());

		for(Usage uv : source.getUsageVariables())
			this.addUsageVariable(new Usage(uv));

		for(Provider cv : source.getProviderVariables())
			this.addProviderVariable(new Provider(cv));
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name != null && !name.equalsIgnoreCase("")){
			this.name = name;
		}else{
			this.name = ResourceNameEnum.PRICEFUNCTION.getResourceName();
		}
		this.setLocalName(this.name);
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
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName.replaceAll(" ", "_");
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	@Override
	public String toString() {
		return "PriceFunction [name=" + name + ", stringFunction="
				+ stringFunction +",SPARQLFunction = "+ this.SPARQLFunction+ ", Usage variables=" + usageVariables
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
	 * @throws InvalidLinkedUSDLModelException 
	 */
	protected void writeToModel(Resource owner,Model model,String baseURI) throws InvalidLinkedUSDLModelException
	{
		
		Function func = null;
		if(this.namespace == null){ //no namespace defined for this resource, we need to define one
			if(baseURI != null || !baseURI.equalsIgnoreCase("")) // the baseURI argument is valid
				this.namespace = baseURI;
			else //use the default baseURI
				this.namespace = PricingAPIProperties.defaultBaseURI;
		}
		
		if(this.localName != null){
			LinkedUSDLValidator validator = new LinkedUSDLValidator();
			validator.checkDuplicateURI(model, ResourceFactory.createResource(this.namespace + this.localName));
			func = model.createResource(this.namespace + this.localName, SPIN.Function).as(Function.class);
			
			if(this.name != null){
				func.addProperty(RDFSEnum.LABEL.getProperty(model), this.name);
			}
			
			if(this.stringFunction != null)// Create a function from the string stringFunction
			{
				MathExp2SPARQL parser = new MathExp2SPARQL(this.stringFunction,this.providerVariables,this.usageVariables);
				//System.out.println(parser.getSPARQLQuery());
				//ArrayList<String> detectedVariables = parser.getParsedVariables();
				com.hp.hpl.jena.query.Query arqQuery = ARQFactory.get().createQuery(model,parser.getSPARQLQuery());
				Query spinQuery = new ARQ2SPIN(model).createQuery(arqQuery, null);
				func.addProperty(SPIN.body, spinQuery);
			}
			else if(this.SPARQLFunction != null && stringFunction == null)
			{
//				if(this.getOldBaseURI() != null){
//					this.setSPARQLFunction(this.getSPARQLFunction().replaceAll(this.getOldBaseURI(), this.namespace));
//				}
				com.hp.hpl.jena.query.Query arqQuery = ARQFactory.get().createQuery(model,this.getSPARQLFunction());
				Query spinQuery = new ARQ2SPIN(model).createQuery(arqQuery, null);
				func.addProperty(SPIN.body, spinQuery);
			}
			
			if(this.comment != null)
				func.addProperty(RDFSEnum.COMMENT.getProperty(model), this.comment);
			
			for(Usage var : this.usageVariables){
				if(var != null){
					var.writeToModel(func,model,baseURI);
				}
			}
			for(Provider var2 : this.providerVariables){
				if(var2 != null){
					var2.writeToModel(func,model,baseURI);
				}
			}
			owner.addProperty(USDLPriceEnum.HAS_PRICE_FUNCTION.getProperty(model), func);//link the Price Component with the Price Plan
		}
	}
	
	/**
	 * Reads a PriceFunction object from the Semantic Model. 
	 * @param   resource   The Resource object of the PriceFunction.
	 * @param   model   Model where the resource is located.
	 * @return  A PriceFunction object populated with its information extracted from the Semantic Model.
	 */
	protected static PriceFunction readFromModel(Resource resource,Model model)
	{
		PriceFunction pf = null;
		if(resource.getLocalName() != null && resource.getNameSpace() != null){
			pf = new PriceFunction(resource.getLocalName().replaceAll("_", " "), resource.getNameSpace());
			//populate the PriceFunction
			
			if(resource.hasProperty(RDFSEnum.COMMENT.getProperty(model)))
				pf.setComment(resource.getProperty(RDFSEnum.COMMENT.getProperty(model)).getString());
			
			if(resource.hasProperty(RDFSEnum.LABEL.getProperty(model)))
				pf.setName(resource.getProperty(RDFSEnum.LABEL.getProperty(model)).getString());
			
			Function ff = SPINModuleRegistry.get().getFunction(resource.getURI(), model);//get the function from the model
			com.hp.hpl.jena.query.Query narq = ARQFactory.get().createQuery((Select)ff.getBody());//transform the spin objects that define the function into a SPARQL query
			
			//QueryExecution qexecc = ARQFactory.get().createQueryExecution(narq, model);//function execution
			//ResultSet rsc = qexecc.execSelect();//function execution
			if(narq != null)
				pf.setSPARQLFunction(narq.toString());//we might need a reverse parser to extract the original formula from the SPARQL query
			
			if(resource.hasProperty(USDLPriceEnum.HAS_VARIABLE.getProperty(model)))
			{
				StmtIterator iter = resource.listProperties(USDLPriceEnum.HAS_VARIABLE.getProperty(model));
				while (iter.hasNext()) {//while there's price variables  left
					Resource variable = iter.next().getObject().asResource();
					if(variable.getProperty(RDFEnum.RDF_TYPE.getProperty(model)).getResource().getLocalName().equals(USDLPriceEnum.USAGE.getResource(model).getURI()))
						pf.addUsageVariable(Usage.readFromModel(variable,model));
					else if(variable.getProperty(RDFEnum.RDF_TYPE.getProperty(model)).getResource().getLocalName().equals(USDLPriceEnum.PROVIDER.getResource(model).getURI()))
						pf.addProviderVariable(Provider.readFromModel(variable,model));
				}
			}
		}
		
		return pf;
	}
	
	//TODO: maybe we could validate if at least one type of function is defined: string or SPIN
	protected void validate() throws InvalidLinkedUSDLModelException{
		
		this.validateSelfData();
		
		for(Usage usage : this.getUsageVariables()){
			if(usage != null){
				usage.validate();
			}else{
				throw new InvalidLinkedUSDLModelException(ErrorEnum.NULL_RESOURCE, new String[]{this.getName(), ResourceNameEnum.USAGE.getResourceType()});
			}
		}
		for(Provider constant : this.getProviderVariables()){
			if(constant != null){
				constant.validate();
			}else{
				throw new InvalidLinkedUSDLModelException(ErrorEnum.NULL_RESOURCE, new String[]{this.getName(), ResourceNameEnum.PROVIDER.getResourceType()});
			}
		}
		
	}
	
	private void validateSelfData() throws InvalidLinkedUSDLModelException{
		if(this.getName() == null || this.getName().equalsIgnoreCase("")){
			throw new InvalidLinkedUSDLModelException(ErrorEnum.MISSING_RESOURCE_DATA, new String[]{this.name, "name"});
		}
	}

}