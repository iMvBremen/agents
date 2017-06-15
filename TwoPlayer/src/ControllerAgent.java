import java.util.ArrayList;
import java.util.List;

import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/*
Random@192.168.192.27:1099/JADE | 291
Defect@192.168.192.27:1099/JADE | 240
Cooperate@192.168.192.27:1099/JADE | 465
Tit-for-tat@192.168.192.27:1099/JADE | 319
===================================================
Random@192.168.192.27:1099/JADE | 318
Defect@192.168.192.27:1099/JADE | 228
Cooperate@192.168.192.27:1099/JADE | 460
Tit-for-tat@192.168.192.27:1099/JADE | 333
===================================================
Random@192.168.192.27:1099/JADE | 329
Defect@192.168.192.27:1099/JADE | 204
Cooperate@192.168.192.27:1099/JADE | 449
Tit-for-tat@192.168.192.27:1099/JADE | 333
===================================================
Random@192.168.192.27:1099/JADE | 341
Defect@192.168.192.27:1099/JADE | 207
Cooperate@192.168.192.27:1099/JADE | 444
Tit-for-tat@192.168.192.27:1099/JADE | 336
===================================================
Random@192.168.192.27:1099/JADE | 336
Defect@192.168.192.27:1099/JADE | 213
Cooperate@192.168.192.27:1099/JADE | 449
Tit-for-tat@192.168.192.27:1099/JADE | 333

June 12th 2017 22:51
 */

/**
 * This Agent will start up the whole simulation in which two players will
 * play a repeated game against each other. The game goes as follows:
 * 
 * Both Agents choose a number from 1-10. The Agent with the lowest offer wins
 * and adds up the utility to its total. If both Agents offered the same value, 
 * then a winner will be chosen at random and the winner will get the utility added.
 *
 * @author Mathijs van Bremen
 * @see PlayerAgent, Strategy
 */
public class ControllerAgent extends BaseAgent {
	/**
	 * Amount of times players will play against each other divided by two 
	 * (as every round will have two games: A->B and B->A).
	 */
	private static final int AMOUNT_OF_ROUNDS = 25;
	/**
	 * List containing a couple {@link AgentController} objects for each 
	 * {@link PlayerAgent} objects set up in the {@link #onSetup()} method
	 */
	private final List<AgentController> players = new ArrayList<>();
	
	/**
	 * Method will add the players to the list.
	 */
	@Override
	protected void onSetup() {
		addPlayer("Player1", Strategy.StrategyType.RANDOM);
		addPlayer("Player2", Strategy.StrategyType.RANDOM);
	}
	
	/**
	 * Returns a new Behaviour for this ControllerAgent. This will be a 
	 * OneShotBehaviour that will run the game for us.
	 * 
	 * @return Returns a {@link Behaviour}
	 * @see OneShotBehaviour
	 */
	@Override
	protected Behaviour getBehaviour() {
		return new OneShotBehaviour(this) {
			/**
			 * Called when this Behaviour will be ran.
			 */
			@Override
			public void action() {		
				// Small delay to make sure all other Agents are running				
				this.myAgent.doWait(1000);				
				
				// Iterate for as long as we have rounds left
				for (int round = 0; round < AMOUNT_OF_ROUNDS; round++) {
					// Iterate over all the players
					for (int index = 0; index < players.size(); index++) {
						// Get current player
						final AgentController player = players.get(index);
						
						// Iterate over all the players playerA can play against
						for (int opponentIndex = index+1; opponentIndex < players.size(); opponentIndex++) {
							// Get the opponent
							final AgentController opponent = players.get(opponentIndex);
							// Send the StartAction from the player to the opponent
							sendStartAction(player, opponent);	
							
							// Small delay to fix state out of sync issue 
							this.myAgent.doWait(50);
							System.out.println("===================================================");
							
							// Now send StartAction from opponent to the player (other way around)
							sendStartAction(opponent, player);	
							
							// Small delay to fix state out of sync issue 
							this.myAgent.doWait(50);
							System.out.println("===================================================");
						}
					}					
				}
			}
		};
	}
	
	/**
	 * Will create and start a new {@link PlayerAgent} for given name and 
	 * {@link Strategy.StrategyType}
	 * 
	 * @param name the name of the PlayerAgent
	 * @param strategyType the strategy type that the PlayerAgent should use
	 */
	private void addPlayer(String name, Strategy.StrategyType strategyType) {
		// AgentContainer to add agents to
		final AgentContainer agentContainer = getContainerController();
		// Object array so we can pass the StrategyType as an argument to the Agent
		final Object[] args = new Object[1];
		args[0] = strategyType;
		
		try {
			// Create a new PlayerAgent, add it to the list and start it
			final AgentController agent = agentContainer.createNewAgent(name, PlayerAgent.class.getName(), args);
			players.add(agent);
			agent.start();	
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Will send the {@link StartAction} that will initiate 
	 * a game between sender and receiver.
	 * 
	 * @param sender the {@link AgentController} receiving the {@link StartAction}
	 * @param receiver the {@link AgentController} that sender will have a game with
	 */
	private void sendStartAction(AgentController sender, AgentController receiver) {
		// Create a new ACLMessage with correct ontology and language
		final ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setOntology(ControllerAgent.this.getOntology().getName());
		msg.setLanguage(ControllerAgent.this.getCodec().getName());
		try {
			// Add the StartAction to the receiver's AID
			final AID receiverAID = new AID(sender.getName(), AID.ISGUID);
			final StartAction startAction = new StartAction();
			startAction.setOpponentName(receiver.getName());
			getContentManager().fillContent(msg, new Action(receiverAID, startAction));
			// Add the receiver to the message
			msg.addReceiver(receiverAID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Send the message
		send(msg);	
	}
}
