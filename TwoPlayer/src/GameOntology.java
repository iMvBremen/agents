import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;

/**
 * This class will set up the Ontology for this project.
 * 
 * @author Mathijs van Bremen
 */
public class GameOntology extends Ontology implements GameVocabulary {
	/**
	 * Name of this ontology.
	 */
	private static final String ONTOLOGY_NAME = "Prisoner-ontology";
	/**
	 * Singleton of this ontology class.
	 */
	private static Ontology instance = null;

	/**
	 * Private constructor as this class is a singleton. 
	 * Will set up the ontology.
	 */
	private GameOntology() {
		// This ontology extends the BasicOntology
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		
		try {
			// Add the Offer action
			AgentActionSchema aas = new AgentActionSchema(OFFER);
			add(aas, OfferAction.class);
			// Add the offer value slot to the Offer action schema
			aas.add(OFFER_FIELD, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
			
			// Add the Response action
			aas = new AgentActionSchema(RESPONSE);
			add(aas, ResponseAction.class);
			// Add the utility field slot to the Response action schema
			aas.add(UTILITY_FIELD, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
			// Add the offer field slot to the Response action schema
			aas.add(RESPONSE_OFFER_FIELD, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
			
			// Add the Start action
			aas = new AgentActionSchema(START);
			add(aas, StartAction.class);
			// Add the Opponent name slot to the Start action schema
			aas.add(OPPONENT_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the GameOntology instance if not yet created and returns it
	 * 
	 * @return this GameOntology
	 */
	public static Ontology getInstance() {
		// Define a new PrisonerOntology if not yet defined
		if (instance == null) {
			instance = new GameOntology();
		}
		
		// Return the instance
		return instance;
	}

}
