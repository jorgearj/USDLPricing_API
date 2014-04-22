package validations;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import usdl.constants.properties.PricingAPIProperties;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.FileManager;

import exceptions.InvalidLinkedUSDLModelException;

public class ValidateRDFSchemaTest {
	
	@Test//(expected = InvalidLinkedUSDLModelException.class)
	public void invalidModelTest() throws InvalidLinkedUSDLModelException, IOException {
		Model model = ModelFactory.createDefaultModel();
		Model pricingSchema = ModelFactory.createDefaultModel();
		
		InputStream in;
		InputStream schema;
		try {
			in = FileManager.get().open("./DebuggingFiles/Output.ttl");
			schema = FileManager.get().open(PricingAPIProperties.usdlPriceSchema);
			model.read(in, "", "TTL");
			pricingSchema.read(schema, "", "TTL");
		} catch (Exception e) {
			e.printStackTrace();
		}

		InfModel infmodel = ModelFactory.createRDFSModel(pricingSchema, model);
		ValidityReport validity = infmodel.validate();
		System.out.println("VALIDATE: "+validity.isValid());
	}

}
