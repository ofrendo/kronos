package kronos.sim;

import java.util.ArrayList;

import kronos.model.ERPData;
import kronos.model.Product;

public class ProductHandler {

	ArrayList<Product> products;
	private static ProductHandler productHandler;
	
	public static ProductHandler getProductHandler(){
		if (productHandler != null) {
			productHandler = new ProductHandler();
		}
		
		return productHandler;
	}
	
	public ProductHandler () {
		products = new ArrayList<Product>();
	}
	
	public void createNewProduct (ERPData erp) {
		products.add(new Product(erp));
	}
	
	public void fireEvent() throws Exception{
		//maybe TO DO: real Exception
		if (products.size() == 0) {
			throw new Exception("No Procuts available for new Events");
		}
		
		
		
		
	}
	
	
}
