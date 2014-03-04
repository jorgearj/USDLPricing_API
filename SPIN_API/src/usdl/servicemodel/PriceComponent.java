package usdl.servicemodel;


import java.util.List;

import usdl.servicemodel.PriceSpec;

public class PriceComponent {
	private String name;
	private boolean isDeduction = false;
	private PriceSpec componentCap;
	private PriceSpec componentFloor;
	private PriceSpec price;
	private PriceFunction priceFunction;
	private List<QuantitativeValue> metrics;
	private String comment;
	
	public PriceComponent() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeduction() {
		return isDeduction;
	}

	public void setDeduction(boolean isDeduction) {
		this.isDeduction = isDeduction;
	}

	public PriceSpec getComponentCap() {
		return componentCap;
	}

	public void setComponentCap(PriceSpec componentCap) {
		this.componentCap = componentCap;
	}

	public PriceSpec getComponentFloor() {
		return componentFloor;
	}

	public void setComponentFloor(PriceSpec componentFloor) {
		this.componentFloor = componentFloor;
	}

	public PriceSpec getPrice() {
		return price;
	}

	public void setPrice(PriceSpec price) {
		this.price = price;
	}

	public PriceFunction getPriceFunction() {
		return priceFunction;
	}

	public void setPriceFunction(PriceFunction priceFunction) {
		this.priceFunction = priceFunction;
	}

	public List<QuantitativeValue> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<QuantitativeValue> metrics) {
		this.metrics = metrics;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "PriceComponent [name=" + name + ", isDeduction=" + isDeduction
				+ ", componentCap=" + componentCap + ", componentFloor="
				+ componentFloor + ", price=" + price + ", priceFunction="
				+ priceFunction + ", metrics=" + metrics + ", comment="
				+ comment + "]";
	}

	
}