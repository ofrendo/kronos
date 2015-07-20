package kronos.fsm;

public enum Triggers {
	LIGHTBARRIER_1_INTERRUPT("Lichtschranke 1 false"), //something is between the light barrier
	LIGHTBARRIER_1_CONNECT("Lichtschranke 1 true"),   //the object moved on and the light barrier is connected
	LIGHTBARRIER_2_INTERRUPT("Lichtschranke 2 false"),
	LIGHTBARRIER_2_CONNECT("Lichtschranke 2 true"),
	LIGHTBARRIER_3_INTERRUPT("Lichtschranke 3 false"),
	MILLING_STATION("Milling"),
	LIGHTBARRIER_3_CONNECT("Lichtschranke 3 true"),
	LIGHTBARRIER_4_INTERRUPT("Lichtschranke 4 false"),
	DRILLING_STATION("Drilling"),
	LIGHTBARRIER_4_CONNECT("Lichtschranke 4 true"),
	LIGHTBARRIER_5_INTERRUPT("Lichtschranke 5 false"),
	LIGHTBARRIER_5_CONNECT("Lichtschranke 5 true"),
	SPECTAL_ANALYSIS("SPECTRAL_ANALYSIS"); //Maybe change this?
	
	public String eventName;
	Triggers(String eventName) {
		this.eventName = eventName;
	}
	
	
}