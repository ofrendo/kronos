package kronos.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import kronos.fsm.Triggers;

/**
* This class represents an ERP data item arriving via JMS 
* as XML message. 
* @author julian.reichwald@dhbw-mannheim.de
*
*/
@XmlRootElement
public class ERPData extends SimData {

    private String OrderNumber;

    private int CustomerNumber;

    private int MaterialNumber;

    private Date TimeStamp;

    public ERPData() {

    }

	public String getOrderNumber() {
		return OrderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}

	public int getCustomerNumber() {
		return CustomerNumber;
	}

	public void setCustomerNumber(int customerNumber) {
		CustomerNumber = customerNumber;
	}

	public int getMaterialNumber() {
		return MaterialNumber;
	}

	public void setMaterialNumber(int materialNumber) {
		MaterialNumber = materialNumber;
	}

	public Date getTimeStamp() {
		return TimeStamp;
	}
	
	public long getTime(){
		return TimeStamp.getTime();
	}

	public void setTimeStamp(Date timeStamp) {
		TimeStamp = timeStamp;
	}

	@Override
	public Triggers getTrigger() {
		return null;
	}
    
    
}
