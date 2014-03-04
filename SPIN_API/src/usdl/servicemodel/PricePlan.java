package usdl.servicemodel;

import java.util.List;

import usdl.servicemodel.PriceSpec;

public class PricePlan {
	private String name;
	private PriceSpec priceCap;
	private PriceSpec priceFloor;
	private List<PriceComponent> priceComponents;
	private String comment;
	//private List<Offering> offerings;//needed?
	public PricePlan() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PriceSpec getPriceCap() {
		return priceCap;
	}
	public void setPriceCap(PriceSpec priceCap) {
		this.priceCap = priceCap;
	}
	public PriceSpec getPriceFloor() {
		return priceFloor;
	}
	public void setPriceFloor(PriceSpec priceFloor) {
		this.priceFloor = priceFloor;
	}
	public List<PriceComponent> getPriceComponents() {
		return priceComponents;
	}
	public void setPriceComponents(List<PriceComponent> priceComponents) {
		this.priceComponents = priceComponents;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "PricePlan [name=" + name + ", priceCap=" + priceCap
				+ ", priceFloor=" + priceFloor + ", priceComponents="
				+ priceComponents + ", comment=" + comment + "]";
	}

	
}