import jade.content.Concept;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Agent that can play the game as stated in the {@link ControllerAgent}.
 * Will keep track of the total utility it has.
 * 
 * @author Mathijs van Bremen
 * @see ControllerAgent
 */
public class PlayerAgent extends BaseAgent implements GameVocabulary {		
	/**
	 * The Strategy passed via the arguments on setup
	 */
	private Strategy strategy;
	
	/**
	 * Total amount of utility this PlayerAgent has won so far
	 */
	private long utility = 0;

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
	 * Updates the {@link #utility} with the given amount.
	 * 
	 * @param utility the amount of Utility to add
	 */
	protected void updateUtility(int utility) {
		this.utility += utility;
		
		System.out.println(this.getName() + " | " + this.utility);
	}
}
