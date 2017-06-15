# Two player game
The second assignment was about creating a simulation in which two players repeatedly play against each other. The game is as follows: 
- Both agents pick a number from 1-10
- Agent with lowest score wins and adds up the utility to its total
- If both agents picked the same number, then a random winner will be chosen

# Realisation 
To realise this project we made two agents: a ControllerAgent and a PlayerAgent. The ControllerAgent is the main entry point for the program which boots up the rest. It adds two PlayerAgents to the system that will both have the same strategy. It will then have its Behaviour called once which will let all the PlayerAgents play a game against each other for a set amount of times.

When the PlayerAgent receives a StartAction from the ControllerAgent, it will send an OfferAction to its opponent with a random offer. The opponent will respond to this message with a ResponseAction in which the offer and utility are set for the receiver of this message. After this the game is over and both PlayerAgents will get the right amount of utility assigned.

When the ControllerAgent is done sending out all the StartActions to the PlayerAgents, the simulation is over.

# Tests 
We have performed the test a total of 5 times. The test consists of all PlayerAgents playing against each other 50 times. After each round the players get their utility.

#### Results
| | 1 | 2 | 3 | 4 | 5 | | AVERAGE  |
| ------ | ------ | ------  | ------ | ------ | ------ | ------ | ------ |
| **Player 1**  | 77 | 111  | 73 | 82 | 94 | | 87.4 |
| **Player 2** | 75 | 81 | 111 | 85 | 107 | | 91.8 |

As we can see in the table above the random values are really close to each other. All we can say about that is that the [java.util.Random](https://docs.oracle.com/javase/8/docs/api/java/util/Random.html) class did its job.

This assignment was more meant to come up with a system to make the simulation work, making it not really interesting after having done exactly that in the first assignment.

# Running the code
To run the code, use the following command:
```sh
$ java jade.Boot -gui Controller:ControllerAgent
```
This will start up the ControllerAgent in JADE and show you the GUI as well. Make sure you've followed all the steps for the installation of JADE (see website right [here](http://jade.tilab.com/)).