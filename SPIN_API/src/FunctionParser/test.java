package FunctionParser;

public class test {

	public static void main(String[] args) {

		MathExp2SPARQL parser = new MathExp2SPARQL("windows = LINUX", null, null);
		System.out.println(parser.getSPARQLQuery());
	}
}
