package usdl.servicemodel;
//http://www.heppnetz.de/ontologies/goodrelations/v1.html#QualitativeValue
public class QualitativeValue extends Value {
	public String hasLabel;

	public QualitativeValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getHasValue() {
		return hasLabel;
	}

	public void setHasLabel(String label) {
		this.hasLabel = label;
	}

	@Override
	public String toString() {
		return "QualitativeValue [hasLabel=" + hasLabel + "]";
	}
	
	
}