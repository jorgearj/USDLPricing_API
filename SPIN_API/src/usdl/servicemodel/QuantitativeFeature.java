package usdl.servicemodel;

public class QuantitativeFeature extends Feature {
	private QuantitativeValue value;

	public QuantitativeFeature() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuantitativeValue getValue() {
		return value;
	}

	public void setValue(QuantitativeValue value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "QuantitativeFeature [value=" + value + "]";
	}
	
	
}