package validations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;
import usdl.servicemodel.Offering;
import exceptions.InvalidLinkedUSDLModelException;

public class DuplicateResourcesTest {
	
	@Test(expected = InvalidLinkedUSDLModelException.class)
	public void duplicateResource() throws InvalidLinkedUSDLModelException, IOException {
		List<Offering> list = new ArrayList<Offering>();
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		Offering offering = new Offering("Offering 1");
		list.add(offering);
		Offering offering2 = new Offering("Offering 1");
		list.add(offering2);
		model.setOfferings(list);
		model.writeModelToFile("./DebuggingFiles/duplicateResourceTest.ttl", "TTL");
		
	} 
	
	@Test
	public void noDuplicateResource() throws InvalidLinkedUSDLModelException, IOException {
		List<Offering> list = new ArrayList<Offering>();
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		Offering offering = new Offering("Offering 1");
		list.add(offering);
		Offering offering2 = new Offering("Offering 2");
		list.add(offering2);
		model.setOfferings(list);
		model.writeModelToFile("./DebuggingFiles/noDuplicateResourceTest.ttl", "TTL");
		
	}

}
