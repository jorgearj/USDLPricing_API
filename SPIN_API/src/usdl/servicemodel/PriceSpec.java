package usdl.servicemodel;

import java.util.Date;

public class PriceSpec {
	private String name;
	private String currency;
	private double value;
	private double maxValue;
	private double minValue;
	private boolean addedTaxIncluded;
	private Date validFrom;
	private Date validThrough;
	private String comment;
	
	public PriceSpec() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public boolean isAddedTaxIncluded() {
		return addedTaxIncluded;
	}

	public void setAddedTaxIncluded(boolean addedTaxIncluded) {
		this.addedTaxIncluded = addedTaxIncluded;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidThrough() {
		return validThrough;
	}

	public void setValidThrough(Date validThrough) {
		this.validThrough = validThrough;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "PriceSpec [name=" + name + ", currency=" + currency
				+ ", value=" + value + ", maxValue=" + maxValue + ", minValue="
				+ minValue + ", addedTaxIncluded=" + addedTaxIncluded
				+ ", validFrom=" + validFrom + ", validThrough=" + validThrough
				+ ", comment=" + comment + "]";
	}
	
	
}