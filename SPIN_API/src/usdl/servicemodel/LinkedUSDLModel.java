package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

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
		/*String queryString =
		        " PREFIX core: <"+ Prefixes.USDL_CORE.getPrefix()+">" +
				" PREFIX pf: <http://jena.hpl.hp.com/ARQ/property#> " +
		        " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + 
				" PREFIX gr: <http://purl.org/goodrelations/v1#> " +
				" PREFIX xsd:     <http://www.w3.org/2001/XMLSchema#> "+
				" PREFIX CloudTaxonomy: <"+ Prefixes.CLOUD.getPrefix()+">"+
		        " SELECT REDUCED ?offering " +
				" WHERE { " +
					" ?offering rdf:type core:ServiceOffering . " +
				" } " ;
		*/
		String queryString = ReaderQueries.readAllOfferings(variableName);
		System.out.println("QUERY:\n"+queryString);		
		Query query = QueryFactory.create(queryString);
        QueryExecution exec = QueryExecutionFactory.create(query, model);
		
		ResultSet results = exec.execSelect();

		while(results.hasNext()){
			QuerySolution row = results.next();
			System.out.println("FOUND");
			Offering offering = Offering.readFromModel(row.getResource(variableName), model);
			offeringsList.add(offering);
		}		
		
		exec.close();
		
		return offeringsList;
	}
	
	private List<Service> readAllServices(Model model){
		List<Service> servicesList = new ArrayList<>();
		
		return servicesList;
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