package Examples;

import java.io.IOException;

import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;
import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;

public class APITest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("STARTING API");
		try {
			LinkedUSDLModel model = LinkedUSDLModelFactory.createFromModel("./ServiceExamples/");
//			LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
			System.out.println(model.toString());
			model.writeModelToFile("./DebuggingFiles/test.ttl", "TTL");
		} catch (InvalidLinkedUSDLModelException | IOException
				| ReadModelException e) {
			e.printStackTrace();
		}
	}

}
