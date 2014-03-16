package usdl.servicemodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import exceptions.ReadModelException;

public class LinkedUSDLModel {
	private List<Service> services;
	private List<Offering> offerings;
	


	protected LinkedUSDLModel() {
		super();
		this.services = new ArrayList<Service>();
		this.offerings = new ArrayList<Offering>();
		
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

	/**
	 * Imports an RDF model and maps it to java objects populating the LinkedUSDLModel structure  
	 * @param   model   The model to read from
	 */
	public void readModel(Model model){
		System.out.println("READING FROM MODEL");
		
		this.offerings = this.readAllOfferings(model);
//		this.services = this.readAllServices(model);
	}
	
	
	private List<Offering> readAllOfferings(Model model){
		List<Offering> offeringsList = new ArrayList<>();
		String variableName = "offering";
		
		String queryString = ReaderQueries.readAllOfferings(variableName);
		System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
        QueryExecution exec = QueryExecutionFactory.create(query, model);
		
		ResultSet results = exec.execSelect();

		while(results.hasNext()){
			QuerySolution row = results.next();
			Offering offering = Offering.readFromModel(row.getResource(variableName), model);
			//System.out.println(offering.toString());
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
	public Model WriteToModel(String baseURI)
	{
		// Create main model
		Model model = JenaUtil.createDefaultModel();
		//JenaUtil.initNamespaces(model.getGraph());
		setPrefixes(model,baseURI);
		
		for(Offering of : this.offerings)
			of.writeToModel(model);
		
		return model;
	}
	
	private void setPrefixes(Model model,String baseURI){
		model.setNsPrefix("usdl",  Prefixes.USDL_CORE.getPrefix());
		model.setNsPrefix("rdf",   Prefixes.RDF.getPrefix());
		model.setNsPrefix("owl",   Prefixes.OWL.getPrefix());
		model.setNsPrefix("dc",    Prefixes.DC.getPrefix() );
		model.setNsPrefix("xsd",   Prefixes.XSD.getPrefix());
		model.setNsPrefix("vann",  Prefixes.VANN.getPrefix());
		model.setNsPrefix("foaf",  Prefixes.FOAF.getPrefix());
		model.setNsPrefix("rdfs",  Prefixes.RDFS.getPrefix());
		model.setNsPrefix("gr",    Prefixes.GR.getPrefix()  );
		model.setNsPrefix("skos",  Prefixes.SKOS.getPrefix());
		model.setNsPrefix("org",   Prefixes.ORG.getPrefix() );
		model.setNsPrefix("price", Prefixes.USDL_PRICE.getPrefix() );
		model.setNsPrefix("legal", Prefixes.USDL_LEGAL.getPrefix() );
		model.setNsPrefix("cloud", Prefixes.CLOUD.getPrefix());
		model.setNsPrefix("sp", Prefixes.SP.getPrefix());
		model.setNsPrefix("spl", Prefixes.SPL.getPrefix());
		model.setNsPrefix("spin", Prefixes.SPIN.getPrefix());
		model.setNsPrefix("",   baseURI + "#" );
	}
	
	
	/**
	 * Exports the LinkedUSDLModel to an RDF file.  
	 * @param   path   The path where the final file will be stored
	 * @param   baseURI   The string representing the baseURI to use in the resulting file. defaults to null.
	 * @param   format	the RDFFormat to use 
	 * @throws InvalidLinkedUSDLModelException 
	 * @throws IOException 
	 */
	public void writeModelToFile(String path, String baseURI, String format) throws InvalidLinkedUSDLModelException, IOException
	{
		Model model = this.WriteToModel(baseURI);
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