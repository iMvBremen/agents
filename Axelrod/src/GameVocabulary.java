/**
 * Static field used to set up the ontology.
 * 
 * @author Mathijs van Bremen
 * @see GameOntology
 */
public interface GameVocabulary {
	/**
	 * Field for the DefectAction schema.
	 */
	public static final String DEFECT = "Defect";
	
	/**
	 * Field for the CooperateAction schema.
	 */
	public static final String COOPERATE = "Cooperate";
	
	/**
	 * Field for the StartAction schema.
	 */
	public static final String START = "Start";
	/**
	 * Field for the Opponent name value for the StartAction schema.
	 */
	public static final String OPPONENT_NAME = "Opponent-name";
}
