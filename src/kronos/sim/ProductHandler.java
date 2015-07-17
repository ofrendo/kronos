package kronos.sim;

import java.util.ArrayList;

import kronos.model.ERPData;
import kronos.model.Product;

public class ProductHandler {

	ArrayList<Product> products;
	private static ProductHandler productHandler;
	private CEventProcessor cEventProcessor;

	public static ProductHandler getProductHandler(){
		if (productHandler != null) {
			productHandler = new ProductHandler();
		}
		
		return productHandler;
	}
	
	private ProductHandler () {
		products = new ArrayList<Product>();
		cEventProcessor = new CEventProcessor();
		cEventProcessor.init();
	}
	
	public void createNewProduct (ERPData erp) {
		products.add(new Product(erp));
	}
	
	public void fireEvent() throws Exception{
		//maybe TO DO: real Exception
		if (products.size() == 0) {
			throw new Exception("No Procuts available for new Events");
		}
		// processing event
		
		// sending to espertech for further processing
		
	}
	
	
}
