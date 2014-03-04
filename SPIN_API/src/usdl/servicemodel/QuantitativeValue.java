package usdl.servicemodel;

public class QuantitativeValue extends Value {
	private double value;
	private double minValue;
	private double maxValue;
	private String unitOfMeasurement;
	
	public QuantitativeValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	@Override
	public String toString() {
		return "QuantitativeValue [value=" + value + ", minValue=" + minValue
				+ ", maxValue=" + maxValue + ", unitOfMeasurement="
				+ unitOfMeasurement + "]";
	}
	
	
}