/**
 * Static field used to set up the ontology.
 * 
 * @author Mathijs van Bremen
 * @see GameOntology
 */
public interface GameVocabulary {
	/**
	 * Field for the OfferAction schema.
	 */
	public static final String OFFER = "Offer";
	/**
	 * Field for the offer value for the OfferAction schema.
	 */
	public static final String OFFER_FIELD = "Offer";
	
	/**
	 * Field for the ResponseAction schema.
	 */
	public static final String RESPONSE = "Response";
	/**
	 * Field for the utility value for the ResponseAction schema.
	 */
	public static final String UTILITY_FIELD = "Utility";
	/**
	 * Field for the offer value for the ResponseAction schema.
	 */
	public static final String RESPONSE_OFFER_FIELD = "Offer";
	
	/**
	 * Field for the StartAction schema.
	 */
	public static final String START = "Start";
	/**
	 * Field for the Opponent name value for the StartAction schema.
	 */
	public static final String OPPONENT_NAME = "Opponent-name";
}
