package usdl.servicemodel;


import java.util.List;



public class PriceFunction {
	private String name;
	private String stringFunction;
	private List<Usage> UsageVariables;
	private List<Provider> ProviderVariables;
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
				+ stringFunction + ", Usage variables=" + UsageVariables
				+ ", Provider variables=" + ProviderVariables
				+ ", constraints=" + constraints + ", comment=" + comment + "]";
	}
	public List<Usage> getUsageVariables() {
		return UsageVariables;
	}
	public void setUsageVariables(List<Usage> usageVariables) {
		UsageVariables = usageVariables;
	}
	public List<Provider> getProviderVariables() {
		return ProviderVariables;
	}
	public void setProviderVariables(List<Provider> providerVariables) {
		ProviderVariables = providerVariables;
	}
	
	
}