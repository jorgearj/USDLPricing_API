package usdl.servicemodel;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

public class LinkedUSDLModel {
	private List<Service> services;
	private List<Offering> offerings;
	private Model model;

	
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
		
	}

	@Override
	public String toString() {
		return "LinkedUSDLModel [services=" + services + ", offerings="
				+ offerings + "]";
	}


	
}