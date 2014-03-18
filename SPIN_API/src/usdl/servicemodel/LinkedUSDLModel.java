package usdl.servicemodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;

import usdl.constants.enums.Prefixes;
import usdl.queries.ReaderQueries;
import usdl.servicemodel.validations.LinkedUSDLValidator;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import exceptions.InvalidLinkedUSDLModelException;

public class LinkedUSDLModel {
	private List<Service> services;
	private List<Offering> offerings;
	private String baseURI;
	private Map<String,String> prefixes; // inverted prefixes map KEY = URI, VALUE = prefix name
	
	protected LinkedUSDLModel(String baseURI) {
		super();
		this.services = new ArrayList<Service>();
		this.offerings = new ArrayList<Offering>();
		this.prefixes = new HashMap<String, String>();
		this.baseURI = baseURI;
		// Initialize system functions and templates
		SPINModuleRegistry.get().init();
		
	}


	public List<Service> getServices() {
		return services;
	}


	public void setServices(List<Service> services) {
		this.services = services;
	}


	public List<Offering> getOfferings() {
		return offerings;
	}


	public void setOfferings(List<Offering> offerings) {
		this.offerings = offerings;
	}

	public String getBaseURI() {
		return baseURI;
	}


	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	protected Map<String, String> getPrefixes() {
		return prefixes;
	}

	protected void setPrefixes(Map<String, String> prefixes) {
		this.prefixes = prefixes;
	}

	protected void addPrefix(String key, String value){
		this.prefixes.put(key, value);
	}
	
	/**
	 * get the name of prefixe based on its URI
	 * @param   key   the URI String
	 * @return  a String with the prefix name
	 */
	protected String getPrefixName(String key){
		return this.prefixes.get(key);
	}

	/**
	 * Imports an RDF model and maps it to java objects populating the LinkedUSDLModel structure  
	 * @param   model   The model to read from
	 */
	public void readModel(Model model){
		System.out.println("READING FROM MODEL");
		this.setPrefixes(this.processPrefixes(model));
		
		this.offerings = this.readAllOfferings(model);
//		this.services = this.readAllServices(model);
	}
	
	
	private List<Offering> readAllOfferings(Model model){
		List<Offering> offeringsList = new ArrayList<>();
		String variableName = "offering";
		
		String queryString = ReaderQueries.readAllOfferings(variableName);
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
        QueryExecution exec = QueryExecutionFactory.create(query, model);
		
		ResultSet results = exec.execSelect();

		while(results.hasNext()){
			QuerySolution row = results.next();
			Offering offering = Offering.readFromModel(row.getResource(variableName), model);
//			System.out.println(offering.toString());
			offeringsList.add(offering);
		}
		
		exec.close();
		
		return offeringsList;
	}
	
	private List<Service> readAllServices(Model model){
		List<Service> servicesList = new ArrayList<>();
		
		return servicesList;
	}
	
	/**
	 * Creates a Jena Model representation of the LinkedUSDLModel.  
	 * @param   baseURI   The string representing the baseURI to use in the resulting file. defaults to null.
	 */
	public Model WriteToModel()
	{
		// Create main model
		Model model = JenaUtil.createDefaultModel();
	    
		model = this.setModelPrefixes(model);
		model.setNsPrefix("", this.baseURI + "#");
		for(Offering of : this.offerings)
			of.writeToModel(model,this.baseURI);
		
		return model;
	}
	
	private Model setModelPrefixes(Model model){

		Iterator<Entry<String, String>> it = this.prefixes.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
	        String key = (String)pairs.getKey(); //URI
	        String value = (String)pairs.getValue(); //preffix name
	        model.setNsPrefix(value, key);
	    }
		return model;
	}
	
	
	/**
	 * Exports the LinkedUSDLModel to an RDF file.  
	 * @param   path   The path where the final file will be stored
	 * @param   format	the RDFFormat to use 
	 * @throws InvalidLinkedUSDLModelException 
	 * @throws IOException 
	 */
	public void writeModelToFile(String path, String format) throws InvalidLinkedUSDLModelException, IOException
	{
		Model model = this.WriteToModel();
		LinkedUSDLValidator.validateModel(model);
		this.write(model, path, format);
	}
	
	private void write(Model model, String path, String format) throws IOException {
		File outputFile = new File(path);
		if (!outputFile.exists()) {
        	outputFile.createNewFile();        	 
        }

		FileOutputStream out = new FileOutputStream(outputFile);
		model.write(out, format);
		out.close();
	}
	
	private Map<String, String> processPrefixes(Model model){
		Map<String, String> result = new HashMap<String, String>();
		
		//adicionar o baseURI
		result.put(this.baseURI, "");
		
		for(Prefixes p : Prefixes.values()){
			result.put(p.getPrefix(), p.getName());
		}
		
		
		Iterator<Entry<String,String>> it = model.getNsPrefixMap().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
	        String name = (String)pairs.getKey();
	        String uri = (String)pairs.getValue();
	        if(!result.containsKey(uri)){
	        	result.put(uri, name);
	        }
	    }
		
		return result;
	}
	
	@Override
	public String toString() {
		String result = "LINKED USDL MODEL: \n" +
				"SERVICES:\n";
		for(Service service : this.services){
			result = result + service.toString() + "\n"; 
		}
		result = result + "\nOFFERINGS:\n";
		for(Offering offering : this.offerings){
			result = result + offering.toString() + "\n"; 
		}
		
		return result;
	}


	
}