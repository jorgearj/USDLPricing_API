package validations;

import java.io.IOException;

import org.junit.Test;

import usdl.servicemodel.LinkedUSDLModel;
import usdl.servicemodel.LinkedUSDLModelFactory;
import usdl.servicemodel.Offering;
import usdl.servicemodel.PriceComponent;
import usdl.servicemodel.PricePlan;
import usdl.servicemodel.PriceSpec;
import usdl.servicemodel.Service;
import exceptions.InvalidLinkedUSDLModelException;
import exceptions.ReadModelException;

public class ValidateModelTest {
	
	@Test//(expected = InvalidLinkedUSDLModelException.class)
	public void noOfferings() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		model.validateModel();
	} 
	
	@Test//(expected = InvalidLinkedUSDLModelException.class)
	public void nullOffering() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		
		model.addOffering(null);
		
		model.validateModel();
	} 
	
	@Test//(expected = InvalidLinkedUSDLModelException.class)
	public void offeringWithEmptyName() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		
		Offering offering = new Offering("");
		model.addOffering(offering);
		
		model.validateModel();
	} 
	
	@Test//(expected = InvalidLinkedUSDLModelException.class)
	public void offeringWithNullName() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		
		Offering offering = new Offering("");
		offering.setName(null);
		model.addOffering(offering);
		
		model.validateModel();
	} 
	
	@Test//(expected = InvalidLinkedUSDLModelException.class)
	public void offeringWithoutService() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		Offering offering = new Offering("Offering1");
		
		model.addOffering(offering);
		
		model.validateModel();
	} 
	
	@Test//(expected = InvalidLinkedUSDLModelException.class)
	public void offeringWithoutPricePlan() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		Offering offering = new Offering("Offering1");
		Service service = new Service();
		service.setName("Service1");
		offering.addService(service);
		
		model.addOffering(offering);
		
		model.validateModel();
	} 
	
	@Test
	public void pricePlanWithoutComponent() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		Offering offering = new Offering("Offering1");
		Service service = new Service();
		service.setName("Service1");
		offering.addService(service);
		PricePlan plan = new PricePlan();
		plan.setName("PricePlan1");
		offering.setPricePlan(plan);
		
		model.addOffering(offering);
		
		model.validateModel();
	}
	
	@Test
	public void componentWithoutPrice() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		Offering offering = new Offering("Offering1");
		Service service = new Service();
		service.setName("Service1");
		offering.addService(service);
		PricePlan plan = new PricePlan();
		plan.setName("PricePlan1");
		PriceComponent comp = new PriceComponent("Component1");
		plan.addPriceComponent(comp);
		offering.setPricePlan(plan);
		
		model.addOffering(offering);
		
		model.validateModel();
	}
	
	@Test
	public void validOffering() throws InvalidLinkedUSDLModelException, IOException, ReadModelException {
		
		LinkedUSDLModel model = LinkedUSDLModelFactory.createEmptyModel();
		Offering offering = new Offering("Offering1");
		Service service = new Service();
		service.setName("Service1");
		offering.addService(service);
		PricePlan plan = new PricePlan();
		plan.setName("PricePlan1");
		PriceComponent priceComp = new PriceComponent("PriceComponent1");
		priceComp.setPrice(new PriceSpec("Price for Price Component 1"));
		plan.addPriceComponent(priceComp);
		offering.setPricePlan(plan);
		
		model.addOffering(offering);
		
		model.validateModel();
	} 

}
