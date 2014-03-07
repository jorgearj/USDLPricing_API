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
		
		String queryString = ReaderQueries.readAllOfferings(variableName);
		Query query = QueryFactory.create(queryString);
        QueryExecution exec = QueryExecutionFactory.create(query, model);
		
		ResultSet results = exec.execSelect();

		while(results.hasNext()){
			QuerySolution row = results.next();
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