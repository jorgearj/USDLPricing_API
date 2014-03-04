package usdl.servicemodel;

public class QualitativeFeature extends Feature {
	private QualitativeValue value;

	
	public QualitativeFeature() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QualitativeValue getValue() {
		return value;
	}

	public void setValue(QualitativeValue value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "QualitativeFeature [value=" + value + "]";
	}
	
	
}