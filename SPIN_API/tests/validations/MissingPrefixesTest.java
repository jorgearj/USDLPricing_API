package validations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;
import usdl.servicemodel.Offering;
import usdl.servicemodel.validations.LinkedUSDLValidator;
import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;

public class MissingPrefixesTest {
	
	@Test(expected = InvalidLinkedUSDLModelException.class)
	public void missingCore() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createFromModel("./DebuggingFiles/MissingPrefixesTests/missingCoreInput.ttl");
	} 
	
	@Test(expected = InvalidLinkedUSDLModelException.class)
	public void missingPrice() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createFromModel("./DebuggingFiles/MissingPrefixesTests/missingPriceInput.ttl");
	} 
	
	@Test
	public void noPrefixMissing() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		LinkedUSDLModel model = LinkedUSDLModelFactory.createFromModel("./DebuggingFiles/MissingPrefixesTests/noPrefixMissingInput.ttl");
	}

}
