import jade.content.AgentAction;

/**
 * Action passed as {@link ACLMessage} content for the receiver 
 * when {@link PlayerAgent} is responding to an {@link OfferAction} 
 * and returning the utility and offer for his opponent.
 * 
 * @author Mathijs van Bremen
 */
public class ResponseAction implements AgentAction {
	/**
	 * The utility (between 1-10) that we get
	 */
	private int utility;
	/**
	 * The offer (between 1-10) that we get from the other Agent
	 */
	private int offer;
	
	/**
	 * Public constructor required by JADE
	 */
	public ResponseAction() {
		this.utility = 0;
		this.offer = 0;
	}
	
	/**
	 * Sets the utility
	 * 
	 * @param utility the utility the agent gets
	 */
	public void setUtility(int utility) {
		this.utility = utility;
	}
	
	/**
	 * Returns the set utility
	 * 
	 * @return utility the agent gets
	 */
	public int getUtility() {
		return utility;
	}
	
	/**
	 * Sets the offer
	 * 
	 * @param offer the offer the agent gets
	 */
	public void setOffer(int offer) {
		this.offer = offer;
	}
	
	/**
	 * Returns the set offer
	 * 
	 * @return offer the agent gets
	 */
	public int getOffer() {
		return offer;
	}
}
