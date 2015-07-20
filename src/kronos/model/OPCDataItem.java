package kronos.model;


import javax.xml.bind.annotation.XmlRootElement;

import kronos.fsm.Triggers;

/**
 * Represents an OPCDataItem arriving via JMS. Can be created via marshalling
 * or used to generate a JSOn string.
 */
@XmlRootElement(name = "OpcDataItem")
public class OPCDataItem extends SimData {

    private Object value;

    private String status;

    private String itemName;

    private long timestamp;

    /**
     * Default empty constructor needed for JAXB marshalling / unmarshalling
     */
    public OPCDataItem() {

    }    
    
    public OPCDataItem(Object v, String name) {
        this.value = v;
        this.itemName = name;
        this.timestamp = System.currentTimeMillis();
        this.status = "GOOD";
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	@Override
	public Triggers getTrigger() {
		for (Triggers t : Triggers.values()) {
			if (t.eventName.equals(this.itemName + " " + value)) {
				return t;
			}
			else if (this.itemName.contains(t.eventName)) {
				return t;
			}
		}
		return null;
	}
}
