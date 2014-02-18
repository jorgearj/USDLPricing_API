/**
 * @author Daniel Guedes Barrigas - danielgbarrigas@hotmail.com / danielgbarrigas@gmail.com
 * 
 * 
 *  A simple code to create a SPIN function with 3 variables, 2 Constants and 1 Usage, using the TopBraid Composer API and then call it to get the dynamic result
 *  Used formula = C + C + U
 *                               2 + 2 + X
 *  It also lists the functions variables, it's usage variables and requests to the user to insert a value for each usage variable. (In this scenario only one value is requested since there's only one 
 *  usage variable defined on the function)
 */

package API;


import java.util.ArrayList;
import java.util.Scanner;

import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.arq.ARQFactory;
import org.topbraid.spin.model.Query;
import org.topbraid.spin.model.Function;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;
import org.topbraid.spin.vocabulary.ARG;
import org.topbraid.spin.vocabulary.SPIN;
import org.topbraid.spin.vocabulary.SPL;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileUtils;

import org.topbraid.spin.model.Select;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;





public class BaseExample {
	public final static String USDL = "http://www.linked-usdl.org/ns/usdl-core#";
	public final static String RDFNS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public final static String OWL = "http://www.w3.org/2002/07/owl#";
	public final static String DC = "http://purl.org/dc/elements/1.1/";
	public final static String XSD = "http://www.w3.org/2001/XMLSchema#";
	public final static String VANN = "http://purl.org/vocab/vann/";
	public final static String FOAF = "http://xmlns.com/foaf/0.1/";
	public final static String USDK = "http://www.linked-usdl.org/ns/usdl#";
	public final static String RDFSNS = "http://www.w3.org/2000/01/rdf-schema#";
	public final static String GR = "http://purl.org/goodrelations/v1#";
	public final static String SKOS = "http://www.w3.org/2004/02/skos/core#";
	public final static String ORG = "http://www.w3.org/ns/org#";
	public final static String PRICE = "http://www.linked-usdl.org/ns/usdl-price#";
	public final static String LEGAL = "http://www.linked-usdl.org/ns/usdl-legal#";
	public final static String CLOUD = "http://rdfs.genssiz.org/CloudTaxonomy#";
	public final static String SPINNS = "http://spinrdf.org/spin#";
	public final static String BASE = "http://rdfs.genssiz.org/test#";
	
	//prefixes for the query's
	public final static	String prefixes =" PREFIX core: <"+ USDL+">\n " +
	        " PREFIX price: <"+ PRICE+">\n " +		
			" PREFIX pf: <http://jena.hpl.hp.com/ARQ/property#>\n " +
	        " PREFIX rdf: <"+RDFNS+">\n " +
			" PREFIX rdfs: <"+RDFSNS+">\n " + 
			" PREFIX gr: <"+GR+">\n " +
			" PREFIX xsd: <"+XSD+">\n "+
			" PREFIX spin: <"+SPINNS + ">\n "+
			" PREFIX foaf: <"+FOAF+">\n" +
			" PREFIX cloud: <"+ CLOUD+">\n";
	
	// Query of the function
	private static final String QUERY =
		"SELECT ?result\n" +
		"WHERE {\n" +
		"    :variable1 price:hasValue ?v .\n" +
		"    ?v gr:hasValue ?value1 . \n " +
		"    :variable2 price:hasValue ?v2 . \n " +
		"    ?v2 gr:hasValue ?value2 . \n" +
		"    :variable3 price:hasValue ?v3 . \n" +
		"    ?v3 gr:hasValue ?value3 . \n" +
		"    BIND( (?value1 + ?value2 + ?value3) AS ?result  ) .\n" +
		"}";

	public static void main(String[] args) {
		// Initialize system functions and templates
		SPINModuleRegistry.get().init();

		// Create main model
		Model model = JenaUtil.createDefaultModel();
		JenaUtil.initNamespaces(model.getGraph());
		
		//set model prefixes
		model.setNsPrefix("usdl",  USDL);
		model.setNsPrefix("rdf",   RDFNS);
		model.setNsPrefix("owl",   OWL);
		model.setNsPrefix("dc",    DC );
		model.setNsPrefix("xsd",   XSD);
		model.setNsPrefix("vann",  VANN);
		model.setNsPrefix("foaf",  FOAF);
		model.setNsPrefix("rdfs",  RDFSNS);
		model.setNsPrefix("gr",    GR  );
		model.setNsPrefix("skos",  SKOS);
		model.setNsPrefix("org",   ORG );
		model.setNsPrefix("price", PRICE );
		model.setNsPrefix("legal", LEGAL );
		model.setNsPrefix("cloud", CLOUD);
		model.setNsPrefix("spin", SPINNS);
		model.setNsPrefix("",   BASE );
		
		// Create function and its variables
		//Function func = createFunction(model);
		createFunction(model);

		String q = prefixes + "SELECT ?v \n" +
		                   "WHERE{ \n" + 
				           "?v a spin:Function"+ ".\n} "; //fetch the name of the function, in this case there is only one so we just fetch the name of any function on the model 
		
		//query the model
		com.hp.hpl.jena.query.Query arq2 = ARQFactory.get().createQuery(model,q);
		QueryExecution qexe = ARQFactory.get().createQueryExecution(arq2, model);		
		ResultSet rs = qexe.execSelect();

		Resource func = null;
		System.out.println("\n\n");
		while(rs.hasNext()){//for each found function
			QuerySolution row = rs.next();
			func = row.getResource("v");
			System.out.println("Function Name: "+func.getLocalName());
			
			String q2 = prefixes + "SELECT ?var \n" +
	                "WHERE{ \n" + 
			           "?v foaf:name "+"\""+ func.getLocalName()+"\"" +".\n " +
	                   "?v price:hasVariable ?var .\n}"; //fetch the variables of the function
			
			com.hp.hpl.jena.query.Query arq3 = ARQFactory.get().createQuery(model,q2);
			//System.out.println(arq3.toString());
			QueryExecution qexeb = ARQFactory.get().createQueryExecution(arq3, model);
			
			ResultSet rsb = qexeb.execSelect();
			System.out.println("Variables:");
			while(rsb.hasNext())//for each variable of the function, print their name
			{
				QuerySolution rowb = rsb.next();
				Resource var = rowb.getResource("var");
				System.out.println(var.getLocalName());
			}
			System.out.println("-------------");
		}	
		
		String q3 = prefixes + "SELECT ?usagevar \n" +
                "WHERE{ \n" + 
		           "?v foaf:name "+"\""+ func.getLocalName()+"\"" +".\n " +
                   "?v price:hasVariable ?usagevar .\n" +
		           "?usagevar a price:Usage .\n}"; //fetch the usage variables of the function
		
		com.hp.hpl.jena.query.Query arq4 = ARQFactory.get().createQuery(model,q3);
		//System.out.println(arq4.toString());
		QueryExecution qexed = ARQFactory.get().createQueryExecution(arq4, model);
		
		ResultSet rsd = qexed.execSelect();
		Property price_hasValue = model.createProperty(PRICE+"hasValue");
		ArrayList<Resource> usagevars = new ArrayList<Resource>();
		while(rsd.hasNext())//for each usage variable of the function
		{
			QuerySolution rowc = rsd.next();
			Resource usageVar = rowc.getResource("usagevar");
			
			if(!usageVar.hasProperty(price_hasValue))//if it doesn't have a defined value
			{
				usagevars.add(usageVar);//add the variable to an arraylist since we can't alter the size of the resourceset while iterating over its content
			}
		}
		Scanner in = new Scanner(System.in);
		System.out.println("There's "+usagevars.size()+" variable(s) that need to be defined!");
		int i=3;
		for(Resource r : usagevars)//define a value for each usage variable without a value associated with it
		{
			System.out.println("Please, intert the value for the variable:   "+r.getLocalName());
			
			int num = in.nextInt();
			r.addProperty(model.createProperty(PRICE+"hasValue"),
			model.createResource(GR+"QuantitativeValue"+i).addProperty(model.createProperty(GR+"hasUnitOfMeasurement"),model.createTypedLiteral("MON",XSDDatatype.XSDstring)).
			addProperty(model.createProperty(GR+"hasValue"),model.createTypedLiteral(num,XSDDatatype.XSDint)));
			i++;
		}

		//EXECUTING THE FUNCTION
		Function ff = SPINModuleRegistry.get().getFunction(func.getURI(), model);//get the function from the model
		com.hp.hpl.jena.query.Query narq = ARQFactory.get().createQuery((Select)ff.getBody());//transform the spin objects that define the function into a SPARQL query
		QueryExecution qexecc = ARQFactory.get().createQueryExecution(narq, model);
		ResultSet rsc = qexecc.execSelect();
		System.out.println("Result for the formula (c+c+u):  " + rsc.nextSolution().getLiteral("result").toString());//final result is store in the ?result variable of the query
		
		System.out.println("\n************************************\n");
		System.out.println("Do you want to print the full model? (Y/N)");
		String s = in.next();
		if(s.equalsIgnoreCase("Y"))
		{
			System.out.println("-------- Full Model - TTL----------\n");
			model.write(System.out,FileUtils.langTurtle);//output the full model into the console
			System.out.println("------------------------------------\n");
		}
	}
private static Function createFunction(Model model) {
		
		// Create a function
		com.hp.hpl.jena.query.Query arqQuery = ARQFactory.get().createQuery(model, QUERY);
		Query spinQuery = new ARQ2SPIN(model).createQuery(arqQuery, null);
		Function function = model.createResource(BASE + "MyFunction", SPIN.Function).as(Function.class);
		function.addProperty(SPIN.body, spinQuery);
		
		//create a variable
		Resource variable = model.createResource(BASE + "variable1").
				addProperty(model.createProperty(RDFNS+"type"),model.createResource(PRICE + "Constant")).
				addProperty(model.createProperty(PRICE+"hasValue"),
						model.createResource(GR+"QuantitativeValue1").addProperty(model.createProperty(GR+"hasUnitOfMeasurement"),model.createTypedLiteral("MON",XSDDatatype.XSDstring)).
						addProperty(model.createProperty(GR+"hasValue"),model.createTypedLiteral(2,XSDDatatype.XSDint)));
		
		//create a second variable
		Resource variable2 = model.createResource(BASE + "variable2").
				addProperty(model.createProperty(RDFNS+"type"),model.createResource(PRICE + "Constant")).
				addProperty(model.createProperty(PRICE+"hasValue"),
						model.createResource(GR+"QuantitativeValue2").addProperty(model.createProperty(GR+"hasUnitOfMeasurement"),model.createTypedLiteral("MON",XSDDatatype.XSDstring)).
						addProperty(model.createProperty(GR+"hasValue"),model.createTypedLiteral(2,XSDDatatype.XSDint)));
		
		
		Resource variable3 = model.createResource(BASE + "variable3").
				addProperty(model.createProperty(RDFNS+"type"),model.createResource(PRICE + "Usage"));
				
				
		function.addProperty(model.createProperty(FOAF + "name"),"MyFunction");//use foaf:name to set a name for the function
		function.addProperty(model.createProperty(PRICE + "hasVariable"), variable);
		function.addProperty(model.createProperty(PRICE + "hasVariable"), variable2);
		function.addProperty(model.createProperty(PRICE+ "hasVariable"),variable3);
		
		return function;
	}
}
