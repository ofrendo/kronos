package kronos.fsm;

/**
 * Collection of states that a product can be in.
 * @author D059373
 *
 */
public enum PartStates {
		INIT,
		LIGHTBARRIER_1,
		BETWEEN_L1_L2,
		LIGHTBARRIER_2,
		BETWEEN_L2_L3,
		MILLING_STATION,
		BETWEEN_L3_L4,
		DRILLING_STATION,
		BETWEEN_L4_L5,
		LIGHTBARRIER_5,
		END_OF_PRODUCTION,
		SPECTRAL_ANALYSIS,
		FINISH
}
