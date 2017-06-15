import jade.content.Concept;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Agent that can play the prisoner's dilemma with other PlayerAgents.
 * Will keep track of the total years of prison it has.
 * 
 * @author Mathijs van Bremen
 * @see ControllerAgent
 */
public class PlayerAgent extends BaseAgent implements GameVocabulary {	
	/**
	 * Amount of years to add when both agents cooperate
	 */
	private static final int CC_YEARS = 1;
	/**
	 * Amount of years to add when both agents defect
	 */
	private static final int DD_YEARS = 3;
	/**
	 * Amount of years to add when we cooperate but opponent defects
	 */
	private static final int CD_YEARS = 5;
	/**
	 * Amount of years to add when we defect but opponent cooperates
	 */
	private static final int DC_YEARS = 0;
	/**
	 * The total years of prison
	 */
	private long yearsOfPrison = 0;
	
	/**
	 * The Strategy passed via the arguments on setup
	 */
	private Strategy strategy;

	/**
	 * Will get the {@link Strategy} from the arguments
	 */
	@Override
	protected void onSetup() {
		// Get the arguments passed to this Agent
		final Object[] args = getArguments();
		if (args != null) {
			// Get the strategy from the arguments and save it
			strategy = new Strategy(this, (Strategy.StrategyType) args[0]);
		}
	}
	
	/**
	 * Returns a new Behaviour for this PlayerAgent. This will be a 
	 * CyclicBehaviour that will simply wait for messages and let the 
	 * {@link Strategy} class handle it.
	 * 
	 * @return Returns a {@link Behaviour}
	 * @see CyclicBehaviour
	 */
	@Override
	protected Behaviour getBehaviour() {
		return new CyclicBehaviour(this) {
			/**
			 * Called when this Behaviour will be ran.
			 */
			@Override
			public void action() {
				// Block until we receive a message
				final ACLMessage msg = blockingReceive();
				
				// Let the Strategy handle the message
				strategy.handleMsg(msg);				
			}
		};
	}
	
	/**
	 * Will update the years of prison depending on the Action we sent and the Action 
	 * we received. These actions will be of {@link CooperateAction} and {@link DefectAction}.
	 * 
	 * @param sentAction action we sent to the opponent 
	 * @param receivedAction action we received from the opponent
	 */
	protected void updateYearsOfPrison(Concept sentAction, Concept receivedAction) {
		// Check if the sent action is cooperate or defect
		if (sentAction instanceof CooperateAction) {
			// Check if the received action is cooperate or defect
			if (receivedAction instanceof CooperateAction) {
				// Both cooperate, so add CC_YEARS to total
				yearsOfPrison += CC_YEARS;		
			} else {
				// We cooperated but opponent didn't, add CD_YEARS to total
				yearsOfPrison += CD_YEARS;				
			}
		} else {
			// Check if the received action is cooperate or defect
			if (receivedAction instanceof CooperateAction) {
				// We defected but opponent didn't, add DC_YEARS to total
				yearsOfPrison += DC_YEARS;				
			} else {
				// Both defect, so add DD_YEARS to total
				yearsOfPrison += DD_YEARS;				
			}			
		}
		
		System.out.println(this.getName() + " | " + yearsOfPrison);
	}
}
