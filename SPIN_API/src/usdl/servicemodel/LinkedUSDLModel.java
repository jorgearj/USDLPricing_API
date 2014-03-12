package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;

import usdl.constants.enums.Prefixes;
import usdl.queries.ReaderQueries;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public class LinkedUSDLModel {
	private List<Service> services;
	private List<Offering> offerings;
	
	public LinkedUSDLModel() {
		super();
		this.services = new ArrayList<Service>();
		this.offerings = new ArrayList<Offering>();
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

	public void readModel(Model model){
		System.out.println("READING FROM MODEL");
		
		this.offerings = this.readAllOfferings(model);
		this.services = this.readAllServices(model);
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
	
	public Model createModelFromOfferings()
	{
		// Initialize system functions and templates
		SPINModuleRegistry.get().init();

		// Create main model
		Model model = JenaUtil.createDefaultModel();
		JenaUtil.initNamespaces(model.getGraph());
		setPrefixes(model);
		
		for(Offering of : this.offerings)
			of.writeToModel(model);
		
		return model;
	}
	
	private void setPrefixes(Model model){
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
		model.setNsPrefix("",   Prefixes.BASE.getPrefix() );
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