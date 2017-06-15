import java.util.HashMap;
import java.util.Map;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

/**
 * Class that will handle the messages for performing the prisoner's 
 * dilemma. 
 * 
 * @author Mathijs van Bremen
 * @see PlayerAgent
 */
public class Strategy {
	/**
	 * Types of Strategies supported
	 */
	public enum StrategyType {
		/**
		 * Will pick defect or cooperate on random
		 */
		RANDOM,
		/**
		 * Will always defect
		 */
		DEFECT,
		/**
		 * Will always cooperate
		 */
		COOPERATE,
		/**
		 * Will cooperate the first time and then follow the opponent's 
		 * last strategy
		 */
		TIT_FOR_TAT,
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
	 * {@link Map} containing the name of an opponent's agent and the 
	 * last strategy they used on us. Used when {@link StrategyType#TIT_FOR_TAT} 
	 * is our strategy.
	 */
	private final Map<String, StrategyType> opponentStrategyMap = new HashMap<>();
	/**
	 * The {@link PlayerAgent} this Strategy belongs to
	 */
	private final PlayerAgent agent;
	/**
	 * boolean indicating whether or not the last message was a {@link StartAction}
	 */
	private boolean lastWasStart = false;
	/**
	 * The previous action we sent to the opponent
	 */
	private Concept prevSentAgentAction;
	
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
			
			// Name of the sender
			final String senderName = msg.getSender().getName();
			// Name of opponent we have to send message to
			String opponentName = null;
			
			// Check which action this is
			if (action instanceof StartAction) {				
				/* We need to initiate the game */				
				// Cast the action to the StartAction
				final StartAction startAction = (StartAction) action;
				// Define the opponent name
				opponentName = startAction.getOpponentName();
				
				// Set boolean to true so we know that we sent the initial message
				lastWasStart = true;				
			} else if (action instanceof CooperateAction) {				
				/* Opponent cooperated */
				// Check if the last action was start
				if (lastWasStart) {
					// If we're doing tit for tat, then put strategy in map
					if (strategyType == StrategyType.TIT_FOR_TAT) {
						opponentStrategyMap.put(senderName, StrategyType.COOPERATE);
					}
					
					//  Reset boolean back to false
					lastWasStart = false;
				} else {
					// We received a message, so depending on our strategy we respond	
					// Define the opponentName so the message will be sent
					opponentName = senderName;
				}
				
				// Update score
				agent.updateYearsOfPrison(prevSentAgentAction, action);	
			} else if (action instanceof DefectAction) {				
				/* Opponent defected */	
				// Check if the last action was start
				if (lastWasStart) {
					// If we're doing tit for tat, then put strategy in map
					if (strategyType == StrategyType.TIT_FOR_TAT) {
						opponentStrategyMap.put(senderName, StrategyType.DEFECT);
					}
					
					// Reset boolean back to false
					lastWasStart = false;
				} else {
					// We received a message, so depending on our strategy we respond.
					// Define the opponentName so the message will be sent
					opponentName = senderName;
				}
				
				// Update score
				agent.updateYearsOfPrison(prevSentAgentAction, action);	
			}	
			
			// Logging
			System.out.println(agent.getName() + " | " + action.getClass().getName() 
					+ " | " + lastWasStart + " | " + strategyType
					+ " | " + opponentName);
			
			// Check if opponentName is defined, meaning we have to send a message
			if (opponentName != null) {
				// Check which StrategyType we're in
				switch (strategyType) {
					case RANDOM: {
						// Send random to opponent
						sendRandom(opponentName);
						
						break;
					}
					
					case DEFECT: {
						// Send defect to opponent
						sendDefect(opponentName);
						
						break;
					}
					
					case COOPERATE: {
						// Send cooperate to opponent
						sendCooperate(opponentName);
						
						break;
					}
					
					case TIT_FOR_TAT: {
						// Send via tit for tat strategy to opponent
						sendTitForTat(opponentName);
						
						// Now that we've sent the message, we need to update the 
						// last strategy in the map
						if (action instanceof CooperateAction) {
							opponentStrategyMap.put(senderName, StrategyType.COOPERATE);														
						} else if (action instanceof DefectAction) {
							opponentStrategyMap.put(senderName, StrategyType.DEFECT);							
						}
						
						break;
					}
				
					default: 
						// Do nothing
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Will send a random strategy to the given opponent
	 * 
	 * @param opponentName name of the opponent's agent
	 */
	private void sendRandom(String opponentName) {
		// Use the Math class to randomly send defect or cooperate
		if (Math.random() >= 0.5) {
			sendDefect(opponentName);
		} else {
			sendCooperate(opponentName);			
		}		
	}
	
	/**
	 * Will send a defect strategy to the given opponent
	 * 
	 * @param opponentName name of the opponent's agent
	 */
	private void sendDefect(String opponentName) {
		// Send a new message with the DefectAction
		sendNewACL(opponentName, new DefectAction());		
	}
	
	/**
	 * Will send a cooperate strategy to the given opponent
	 * 
	 * @param opponentName name of the opponent's agent
	 */
	private void sendCooperate(String opponentName) {
		// Send a new message with the CooperateAction
		sendNewACL(opponentName, new CooperateAction());		
	}
	
	/**
	 * Will send a defect or cooperate depending on the tit for tat 
	 * strategy
	 * 
	 * @param opponentName name of the opponent's agent
	 * @see StrategyType#TIT_FOR_TAT
	 */
	private void sendTitForTat(String opponentName) {		
		// Get the StrategyType from the map and check if not found
		StrategyType strategyType = opponentStrategyMap.get(opponentName);
		if (strategyType == null) {
			// Not found, so put the UNKNOWN one in
			strategyType = StrategyType.UNKNOWN;
			opponentStrategyMap.put(opponentName, strategyType);
		}
		
		switch (strategyType) {
			case COOPERATE: {
				// Opponent cooperated last time, so send the CooperateAction
				sendCooperate(opponentName);
				
				break;
			}
			
			case DEFECT: {
				// Opponent defected last time, so send the DefectAction
				sendDefect(opponentName);
				
				break;
			}
		
			case RANDOM: 		// Should not happen, fall-through
			case TIT_FOR_TAT: 	// -
			case UNKNOWN:		// fall-through
			default: {
				// Simply send the CooperateAction as the first action
				sendCooperate(opponentName);
				
				break;
			}
		}
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
		
		// Store the action as the prevSentAction
		prevSentAgentAction = action;
	}
}
