package usdl.servicemodel;

import java.util.List;

public class Offering {
	private String name;
	private List<Service> includes;
	private PricePlan pricePlan;
	private String comment;
	
	public Offering() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Service> getIncludes() {
		return includes;
	}

	public void setIncludes(List<Service> includes) {
		this.includes = includes;
	}

	public PricePlan getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(PricePlan pricePlan) {
		this.pricePlan = pricePlan;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Offering [name=" + name + ", includes=" + includes
				+ ", pricePlan=" + pricePlan + ", comment=" + comment + "]";
	}
	
	
}