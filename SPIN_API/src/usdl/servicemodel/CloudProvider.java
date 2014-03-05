package usdl.servicemodel;

import java.util.List;

public class CloudProvider {
	private String name;
	private String comment;
	private List<Service> providedServices;

	
	public CloudProvider() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public List<Service> getProvidedServices() {
		return providedServices;
	}


	public void setProvidedServices(List<Service> providedServices) {
		this.providedServices = providedServices;
	}


	@Override
	public String toString() {
		return "CloudProvider [name=" + name + ", comment=" + comment
				+ ", providedServices=" + providedServices + "]";
	}

	
}