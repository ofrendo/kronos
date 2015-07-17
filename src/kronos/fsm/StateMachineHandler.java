package kronos.fsm;

import com.github.oxo42.stateless4j.*;

public class StateMachineHandler {
	// 
	private StateMachineConfig<PartStates, Triggers> fsmc ; 
	
	public StateMachineHandler(){
		configure();
	}
	
	public StateMachine<PartStates, Triggers> newStateMachine(){
		StateMachine<PartStates, Triggers> stateMachine = new StateMachine<>(PartStates.INIT, fsmc);
		return stateMachine;
	}

	private void configure() {
		fsmc = new StateMachineConfig<PartStates, Triggers>(); 
		
		fsmc.configure(PartStates.INIT)
		.permit(Triggers.LIGHTBARRIER_1_INTERRUPT, PartStates.LIGHTBARRIER_1); 
		
		fsmc.configure(PartStates.LIGHTBARRIER_1)
		.permit(Triggers.LIGHTBARRIER_1_CONNECT, PartStates.BETWEEN_L1_L2);
		
		fsmc.configure(PartStates.BETWEEN_L1_L2)
		.permit(Triggers.LIGHTBARRIER_2_INTERRUPT, PartStates.LIGHTBARRIER_2);
		
		fsmc.configure(PartStates.LIGHTBARRIER_2)
		.permit(Triggers.LIGHTBARRIER_2_CONNECT, PartStates.BETWEEN_L2_L3);
		
		fsmc.configure(PartStates.BETWEEN_L2_L3)
		.permit(Triggers.LIGHTBARRIER_2_INTERRUPT, PartStates.MILLING_STATION);
		
		fsmc.configure(PartStates.MILLING_STATION)
		.permit(Triggers.MILLING_STATION, PartStates.MILLING_STATION)
		.permit(Triggers.LIGHTBARRIER_3_CONNECT, PartStates.BETWEEN_L3_L4);
		
		fsmc.configure(PartStates.BETWEEN_L3_L4)
		.permit(Triggers.LIGHTBARRIER_3_INTERRUPT, PartStates.DRILLINGSTATION);
		
		fsmc.configure(PartStates.DRILLINGSTATION)
		.permit(Triggers.DRILLINGSTATION, PartStates.DRILLINGSTATION)
		.permit(Triggers.LIGHTBARRIER_4_INTERRUPT, PartStates.BETWEEN_L4_L5);
		
		fsmc.configure(PartStates.BETWEEN_L4_L5)
		.permit(Triggers.LIGHTBARRIER_5_INTERRUPT, PartStates.LIGHTBARRIER_5);
		
		fsmc.configure(PartStates.LIGHTBARRIER_5)
		.permit(Triggers.LIGHTBARRIER_5_CONNECT, PartStates.END_OF_PRODUCTION);
		
		fsmc.configure(PartStates.END_OF_PRODUCTION)
		.permit(Triggers.SPECTAL_ANALYSIS, PartStates.FINISH);
		
		fsmc.configure(PartStates.FINISH)
		.permit(Triggers.SPECTAL_ANALYSIS, PartStates.FINISH);
		
		
	}
}
