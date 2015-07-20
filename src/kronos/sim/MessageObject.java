package kronos.sim;

import kronos.sim.source.SimSourceType;

public class MessageObject {
	
	public String text;
	public SimSourceType type;
	
	public MessageObject (String text, SimSourceType type){
		this.text = text;
		this.type = type;
	}

}
