import jade.content.AgentAction;

/**
 * Action passed as {@link ACLMessage} content for the receiver 
 * when {@link ControllerAgent} wants {@link PlayerAgent} to start 
 * a game with a certain opponent.
 * 
 * @author Mathijs van Bremen
 */
public class StartAction implements AgentAction {
	/**
	 * Name of the opponent we need to send a message to.
	 */
	private String opponentName;
	
	/**
	 * Public constructor required by JADE
	 */
	public StartAction() {
		this.opponentName = "";
	}
	
	/**
	 * Sets the opponent's name
	 * 
	 * @param opponentName name of the opponent's agent
	 */
	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}
	
	/**
	 * Returns the set opponent's name
	 * 
	 * @return name of the opponent's agent
	 */
	public String getOpponentName() {
		return opponentName;
	}
}
