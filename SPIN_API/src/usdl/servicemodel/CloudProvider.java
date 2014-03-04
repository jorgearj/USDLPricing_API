package usdl.servicemodel;

import java.util.ArrayList;

public class CloudProvider {
	private ArrayList<Service> services;
	public CloudProvider(){
		
	}
	public ArrayList<Service> getServices() {
		return services;
	}
	public void setServices(ArrayList<Service> services) {
		this.services = services;
	}
	
	
}