package usdl.servicemodel;

import java.util.List;

public class LinkedUSDLModel {
	private List<Service> services;
	private List<Offering> offerings;

	
	public LinkedUSDLModel() {
		super();
		// TODO Auto-generated constructor stub
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


	@Override
	public String toString() {
		return "LinkedUSDLModel [services=" + services + ", offerings="
				+ offerings + "]";
	}


	
}