package kronos.fsm;

import stateless4j.PartStates;
import stateless4j.StateMachineConfig;
import stateless4j.Triggers;

import com.github.oxo42.stateless4j.*;

public class StateMachineHandler {
	// 
	private StateMachineConfig<PartStates, Triggers> fsmc ; 
	private StateMachine<PartStates, Triggers> fsm  ;
	
	public StateMachineHandler(){
		configure();
	}

	private void configure() {
		// TODO Auto-generated method stub
		fsmc = new StateMachineConfig<PartStates, Triggers>(); 
		
		fsmc.configure(PartStates.INIT)
		.permit(Triggers.LIGHTBARRIER_1_INTERRUPT, PartStates.LIGHTBARRIER_1)
		.ignore(null)
		.onEntry(this::setStartTime); 
		
		fsmc.configure(PartStates.LIGHTBARRIER_1)
		.permit(Triggers.LIGHTBARRIER_1_CONNECT, PartStates.BETWEEN_L1_L2);
		
		fsmc.configure(PartStates.BETWEEN_L1_L2)
		.permit(Triggers.LIGHTBARRIER_2_INTERRUPT, PartStates.LIGHTBARRIER_2);
		
		
		fsmc.configure(PartStates.MILLING)
		.permit(Triggers.MILLING_OFF, PartStates.INIT)
		.ignore(Triggers.DRILLING_ON)
		.ignore(Triggers.DRILLING_OFF)
		.ignore(Triggers.MILLING_ON)
		.onEntry(this::increaseMillingCounter);
		
	}
	
	public void setStartTime() {
		
	}
}
