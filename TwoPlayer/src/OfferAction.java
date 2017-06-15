import jade.content.AgentAction;

/**
 * Action passed as {@link ACLMessage} content for the receiver 
 * when {@link PlayerAgent} is offering a value to his opponent.
 * 
 * @author Mathijs van Bremen
 */
public class OfferAction implements AgentAction {
	/**
	 * The offer (between 1-10)
	 */
	private int offer;
	
	/**
	 * Public constructor required by JADE
	 */
	public OfferAction() {
		this.offer = 0;
	}
	
	/**
	 * Sets the offer
	 * 
	 * @param offer offer going to the opponent's agent
	 */
	public void setOffer(int offer) {
		this.offer = offer;
	}
	
	/**
	 * Returns the set offer
	 * 
	 * @return offer of the agent
	 */
	public int getOffer() {
		return offer;
	}
}
