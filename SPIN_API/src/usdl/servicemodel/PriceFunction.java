package usdl.servicemodel;


import java.util.List;



public class PriceFunction {
	private String name;
	private String stringFunction;
	private List<Usage> usageVariables;
	private List<Provider> providerVariables;
	private List<Constraint> constraints;
	private String comment;
	
	public PriceFunction() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStringFunction() {
		return stringFunction;
	}
	public void setStringFunction(String stringFunction) {
		this.stringFunction = stringFunction;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "PriceFunction [name=" + name + ", stringFunction="
				+ stringFunction + ", Usage variables=" + usageVariables
				+ ", Provider variables=" + providerVariables
				+ ", constraints=" + constraints + ", comment=" + comment + "]";
	}
	public List<Usage> getUsageVariables() {
		return usageVariables;
	}
	public void setUsageVariables(List<Usage> variables) {
		usageVariables = variables;
	}
	public List<Provider> getProviderVariables() {
		return providerVariables;
	}
	public void setProviderVariables(List<Provider> variables) {
		providerVariables = variables;
	}
	
	// New Functions
}