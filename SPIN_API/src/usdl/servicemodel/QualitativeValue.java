package usdl.servicemodel;

public class QualitativeValue extends Value {
	public QualitativeFeature hasValue;

	public QualitativeValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QualitativeFeature getHasValue() {
		return hasValue;
	}

	public void setHasValue(QualitativeFeature hasValue) {
		this.hasValue = hasValue;
	}

	@Override
	public String toString() {
		return "QualitativeValue [hasValue=" + hasValue + "]";
	}
	
	
}