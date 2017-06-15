import java.util.Random;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

/**
 * Class that will handle the messages for performing the game 
 * as stated in the {@link ControllerAgent}.
 * 
 * @author Mathijs van Bremen
 * @see PlayerAgent, ControllerAgent
 */
public class Strategy {
	/**
	 * Types of Strategies supported
	 */
	public enum StrategyType {
		/**
		 * Will pick random value between {@link Strategy#MIN_OFFER} 
		 * and {@link Strategy#MAX_OFFER}
		 */
		RANDOM,
		/**
		 * Extra strategy in case opponent's strategy is not known yet
		 */
		UNKNOWN
	}
	/**
	 * Our {@link StrategyType}, passed when creating this class
	 */
	private final StrategyType strategyType;
	/**
	 * The {@link PlayerAgent} this Strategy belongs to
	 */
	private final PlayerAgent agent;
	
	/**
	 * Random class to get random values for the {@link StrategyType#RANDOM} 
	 * StrategyType
	 */
	private final Random random = new Random();
	/**
	 * Minimum offer we can give
	 */
	private static final int MIN_OFFER = 1;
	/**
	 * Maximum offer we can give
	 */
	private static final int MAX_OFFER = 10;
	
	/**
	 * Constructor that will simply set given parameters
	 * 
	 * @param agent the {@link PlayerAgent} this Strategy belongs to
	 * @param strategyType the StrategyType we should follow
	 */
	public Strategy(PlayerAgent agent, StrategyType strategyType) {
		this.agent = agent;
		this.strategyType = strategyType;
	}
	
	/**
	 * Should be called when the agent receives an {@link ACLMessage}.
	 * Will then handle the message and react accordingly. 
	 * 
	 * @param msg the {@link ACLMessage} the agent received
	 */
	public void handleMsg(ACLMessage msg) {				
		try {
			// Get the action from the content
			final ContentElement content = agent.getContentManager().extractContent(msg);
			final Concept action = ((Action) content).getAction();
			
			// Check which action this is
			if (action instanceof StartAction) {				
				/* We need to initiate the game */				
				// Cast the action to the StartAction
				final StartAction startAction = (StartAction) action;
				
				// Create a new OfferAction and set the offer
				final OfferAction offerAction = new OfferAction();
				offerAction.setOffer(getOffer());
				
				// Send the OfferAction to the opponent
				sendNewACL(startAction.getOpponentName(), offerAction);
			} else if (action instanceof OfferAction) {
				// Cast the action to the OfferAction
				final OfferAction offerAction = (OfferAction) action;
				
				// Create a new ResponseAction and set the offer
				final ResponseAction responseAction = new ResponseAction();
				responseAction.setOffer(getOffer());
				// Check if we won or lost
				if ( ((offerAction.getOffer() == responseAction.getOffer()) && getRandomWin()) 
						|| (offerAction.getOffer() > responseAction.getOffer()) ) {
					// Offers were equal and we randomly won or opponent's offer was 
					// higher than ours. Set the utility to 0 for the opponent and update 
					// ours with the offer we set
					responseAction.setUtility(0);
					agent.updateUtility(responseAction.getOffer());
				} else {
					// Opponent won, so set utility to what opponent offered
					responseAction.setUtility(offerAction.getOffer());					
				}
				
				// Send the ResponseAction to the opponent
				sendNewACL(msg.getSender().getName(), responseAction);
			} else if (action instanceof ResponseAction) {
				// Cast the action to the ResponseAciton
				final ResponseAction responseAction = (ResponseAction) action;
				// Update the Agent's utility
				agent.updateUtility(responseAction.getUtility());							
			}
			
			// Logging
//			System.out.println(agent.getName() + " | " + action.getClass().getName() 
//					+ " | " + strategyType);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Returns an offer depending on the current {@link StrategyType}. This 
	 * offer will be within the {@link #MIN_OFFER} and {@link #MAX_OFFER}.
	 * 
	 * @return an offer depending on current {@link StrategyType}
	 */
	private int getOffer() {
		// Check which StrategyType we're in
		switch (strategyType) {
			case RANDOM: {
				// Return a random value between min and max
				return random.nextInt(MAX_OFFER - MIN_OFFER + 1) + MIN_OFFER;
			}
		
			default:
				// Do nothing
				break;
		}
		
		// Unknown strategy, so just return the max
		return MAX_OFFER;
	}
	
	/**
	 * Returns random true or false. True meaning we win and false meaning we lose
	 * 
	 * @return random true or false
	 */
	private boolean getRandomWin() {
		return random.nextBoolean();
	}
	
	/**
	 * Will send an {@link ACLMessage} to the given opponent's name. 
	 * The message will contain the given AgentAction as content.
	 * 
	 * @param opponentName name of the opponent's agent
	 * @param action either {@link CooperateAction} or {@link DefectAction}
	 */
	private void sendNewACL(String opponentName, AgentAction action) {
		// Create a new ACLMessage and set the ontology and language
		final ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(agent.getOntology().getName());
		msg.setLanguage(agent.getCodec().getName());
		// Create a new AID, add the given AgentAction and add it to the message
		final AID receiver = new AID(opponentName, AID.ISGUID);
		try {
			agent.getContentManager().fillContent(msg, new Action(receiver, action));
		} catch (Exception e) {
			e.printStackTrace();
		}
		msg.addReceiver(receiver);
		
		// Send the message
		agent.send(msg);
	}
}
