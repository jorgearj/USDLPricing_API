package usdl.servicemodel;

public class QuantitativeFeature extends Feature {
	private QuantitativeValue value;

	public QuantitativeFeature() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return super.toString() +", value= "+ value + "]";
	}

	public QuantitativeValue getValue() {
		return value;
	}

	public void setValue(QuantitativeValue value) {
		this.value = value;
	}

}