
public class FifteenStack {
	private int firstStack = 3;
	private int secondStack = 5;
	private int thirdStack = 7;

	/*
	 * @brief default constructor.
	 */
	FifteenStack() {

	}

	/*
	 * @brief constructor to initializes the stack.
	 * @param int firstStack, amount of the first stack. 
	 * @param int secondStack, amount of the second stack. 
	 * @param int thirdStack, amount of the third stack. 
	 */
	FifteenStack(int firstStack, int secondStack, int thirdStack) {
		this.firstStack = firstStack;
		this.secondStack = secondStack;
		this.thirdStack = thirdStack;
	}

	/*
	 * @brief looks at the amount left on a stack.
	 * @param int stack, the stack that the agent wants to look at.
	 */
	public int look(int stack) {
		switch (stack) {
		case 1:
			return firstStack;
		case 2:
			return secondStack;
		case 3:
			return thirdStack;
		default:
			return 0;
		}
	}

	/*
	 * @brief takes an amount of the stack.
	 * @param int stack, tje stack the agent wants to take from.
	 * @param int amount, the amount the agent wants to take.
	 */
	public void take(int stack, int amount) {
		switch (stack) {
		case 1:
			firstStack -= amount;
			break;
		case 2:
			secondStack -= amount;
			break;
		case 3:
			thirdStack -= amount;
			break;
		default:
			break;
		}
	}

	/*
	 * @brief checks if all stacks are empty.
	 * @return boolean, whether the game is over or not.
	 */
	public boolean gameOver() {
		if (firstStack <= 0 && secondStack <= 0 && thirdStack <= 0) {
			return true;
		}
		return false;
	}

	/*
	 * @brief shows current state of the game.
	 */
	public String toString() {
		return firstStack + " " + secondStack + " " + thirdStack;
	}

	/*
	 * @brief creates a new FifteenStack based on the provided String.
	 * @param String s, String that contains the amount of the stacks.
	 */
	public static FifteenStack fromString(String s) {
		String[] parts = s.split(" ");
		return new FifteenStack(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}

}
