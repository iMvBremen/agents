import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Perry extends Agent {

	public static final String META = "meta";
	public static final String GAME = "game";

	public static final String CONGRATULATIONS = "gefeliciteerd";

	public static final int META_STATE_FREE = 0;
	public static final int META_STATE_PROPOSAL_SENT = 1;
	public static final int META_STATE_IN_GAME = 2;

	private int metaState = META_STATE_FREE;
	private AID proposingTo = null;

	private List<AID> aids;

	private FifteenStack stack;

	/*
	 * @brief initializes the agent id given through arguments.
	 */
	private void initAids() {
		aids = new ArrayList<AID>();
		Object[] args = getArguments();
		if (args == null) {
			System.err.println("No arguments given");
			return;
		}
		AID me = getAID();
		for (Object arg : args) {
			AID aid = getAID(arg.toString());
			if (aid.equals(me)) {
				continue;
			}
			aids.add(aid);
		}
	}

	/*
	 * @brief agent inform all other agents by sending a message with his agent id.
	 */
	private void informAll() {
		if (aids.size() != 0) {
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.setLanguage(META);
			message.setContent(this.getAID().toString());
			for (AID aid : aids) {
				message.addReceiver(aid);
			}
			logMessage(message);
			send(message);
		}
	}

	private void changeState(int newState) {
		// System.out.println(getAID().getLocalName() + " " + this.metaState + "
		// > " + newState);
		this.metaState = newState;
	}

	/*
	 * @brief agent sends a propose message to another agent.
	 */
	private void sendProposal() {
		try {
			Thread.sleep((int) (Math.random() * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (aids.size() == 0) {
			System.err.println("No agents");
			return;
		}
		int i = (int) Math.floor(Math.random() * aids.size());
		proposingTo = aids.get(i);
		changeState(META_STATE_PROPOSAL_SENT);
		sendTo(proposingTo, META, ACLMessage.PROPOSE);
	}

	/*
	 * @brief agent creates a new game and makes a move.
	 */
	private void startGame() {
		stack = new FifteenStack();
		changeState(META_STATE_IN_GAME);
		makeAMove();
	}

	/*
	 * @brief agent makes a move by randomly choosing a stack and an amount.
	 */
	private void makeAMove() {
		int s = (int) (Math.random() * 3) + 1;
		while (stack.look(s) <= 0) {
			s = (int) (Math.random() * 3) + 1;
		}
		int amount = (int) (Math.random() * (stack.look(s) - 1)) + 1;
		stack.take(s, amount);
	}

	/*
	 * @brief handles game messages and sends a reply.
	 * @param ACLMessage message, the received message.
	 */
	private void handleGame(ACLMessage message) {
		AID replyTo = message.getSender();
		stack = FifteenStack.fromString(message.getContent());
		makeAMove();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (stack.gameOver()) {
			sendTo(replyTo, META, ACLMessage.INFORM, CONGRATULATIONS);
			System.out.println();
			changeState(META_STATE_FREE);
		} else {
			sendTo(replyTo, GAME, ACLMessage.INFORM, stack.toString());
		}
	}

	/*
	 * @brief handles meta messages.
	 * @param ACLMessage message, the received message.
	 */
	private void handleMeta(ACLMessage message) {
		AID replyTo = message.getSender();
		switch (message.getPerformative()) {
		case ACLMessage.INFORM:
			if (message.getContent().equals(CONGRATULATIONS)) {
				changeState(META_STATE_FREE);
			}
			break;
		case ACLMessage.PROPOSE:
			if (metaState == META_STATE_FREE) {
				changeState(META_STATE_IN_GAME);
				sendTo(replyTo, META, ACLMessage.ACCEPT_PROPOSAL);
			} else {
				sendTo(replyTo, META, ACLMessage.REFUSE);
			}
			break;
		case ACLMessage.REFUSE:
			if (metaState != META_STATE_IN_GAME) {
				changeState(META_STATE_FREE);
			}
			break;
		case ACLMessage.ACCEPT_PROPOSAL:
			if (metaState != META_STATE_IN_GAME) {
				startGame();
				sendTo(replyTo, GAME, ACLMessage.INFORM, stack.toString());
			}
			break;
		}
	}

	/*
	 * @brief agent send a message to another agent.
	 * @param AID to, receiver of the message.
	 * @param String language, type of message (meta/game).
	 * @param int performative, type of performative.
	 */
	private void sendTo(AID to, String language, int performative) {
		ACLMessage reply = new ACLMessage(performative);
		reply.setLanguage(language);
		reply.addReceiver(to);
		logMessage(reply);
		send(reply);
	}

	/*
	 * @brief agent send a message to another agent.
	 * @param AID to, receiver of the message.
	 * @param String language, type of message (meta/game).
	 * @param int performative, type of performative.
	 * @param String content, content of the message.
	 */
	private void sendTo(AID to, String language, int performative, String content) {
		ACLMessage reply = new ACLMessage(performative);
		reply.setLanguage(language);
		reply.addReceiver(to);
		reply.setContent(content);
		logMessage(reply);
		send(reply);
	}

	/*
	 * @brief logs the messages, used for debug.
	 * @param ACLMessage message, the received message.
	 */
	private void logMessage(ACLMessage message) {
		String from = this.getAID().getLocalName();
		String to = "";
		for (Iterator<AID> i = message.getAllReceiver(); i.hasNext();) {
			AID aid = i.next();
			to += aid.getLocalName();
		}
		String performative = "";
		switch (message.getPerformative()) {

		case ACLMessage.INFORM:
			performative = "INFORM";
			break;
		case ACLMessage.PROPOSE:
			performative = "PROPOSE";
			break;
		case ACLMessage.REFUSE:
			performative = "REFUSE";
			break;
		case ACLMessage.ACCEPT_PROPOSAL:
			performative = "ACCEPT_PROPOSAL";
			break;
		}
		String content = message.getContent();
		if (content == null) {
			content = "";
		}
		System.out.printf("SND %8s > %8s: %s %10s %s\n", from, to, message.getLanguage(), performative, content);
	}

	protected void setup() {
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				initAids();
				informAll();
				sendProposal();
			}
		});

		addBehaviour(new CyclicBehaviour() {
			public void action() {
				ACLMessage message = receive();
				if (message != null) {
					if (message.getLanguage().equals("meta")) {
						handleMeta(message);
					} else if (message.getLanguage().equals("game")) {
						handleGame(message);
					}
				}
				if (metaState == META_STATE_PROPOSAL_SENT && Math.random() > 0.5) {
					sendTo(proposingTo, META, ACLMessage.PROPOSE);
				}
				block(1000);
			}
		});

		addBehaviour(new TickerBehaviour(this, 1000) {
			@Override
			protected void onTick() {
				if (metaState == META_STATE_FREE) {
					sendProposal();
				}
			}
		});
	}
}
