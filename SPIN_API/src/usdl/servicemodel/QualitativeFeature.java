package usdl.servicemodel;

public class QualitativeFeature extends Feature {
	private QualitativeValue value;
	public QualitativeFeature() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String toString() {
		return super.toString() +", value= "+ value + "]";
	}


	public QualitativeValue getValue() {
		return value;
	}


	public void setValue(QualitativeValue value) {
		this.value = value;
	}

	
	
}