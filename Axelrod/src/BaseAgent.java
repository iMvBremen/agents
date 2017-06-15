import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

/**
 * Base Agent to make the setup a bit easier for new agents within this 
 * project. Will register SLCodec and the GameOntology after which it will 
 * add a Behaviour defined by the {@link #onSetup()} method extending class 
 * will define.
 * 
 * @author Mathijs van Bremen
 */
public abstract class BaseAgent extends Agent {
	/**
	 * Language we will be talking in.
	 */
	private final Codec codec = new SLCodec();
	/**
	 * Ontology we will be using. 
	 */
	private final Ontology ontology = GameOntology.getInstance();
	
	/**
	 * Initialization of the Agent.
	 */
	protected void setup() {
		// Register the language and ontology
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		// Add the Behaviour for this Agent
		addBehaviour(getBehaviour());
		
		// Call the onSetup so the extending class can set its things up
		onSetup();
	}
	
	/**
	 * Will be called on setup for extending class to set its things up.
	 */
	protected abstract void onSetup();
	
	/**
	 * Extending class will return its Behaviour that will be added on setup.
	 * 
	 * @return {@link Behaviour} that this Agent should run
	 */
	protected abstract Behaviour getBehaviour();
	
	/**
	 * Will return the {@link Codec} used by this Agent.
	 * 
	 * @return the {@link Codec} used
	 */
	public Codec getCodec() {
		return codec;
	}
	
	/**
	 * Will return the {@link Ontology} used by this Agent.
	 * 
	 * @return the {@link Ontology} used
	 */
	public Ontology getOntology() {
		return ontology;
	}
}
