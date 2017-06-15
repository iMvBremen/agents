# Axelrod tournament
The first assignment was about creating a simulation for the Axelrod tournament. This tournament was held by Robert Axelrod in 1980 in which computers played games against each other using various strategies for the prisoner's dilemma. The winning strategy of the tournament was Tit for Tat. More information on the actual tournament can be found [here](https://cs.stanford.edu/people/eroberts/courses/soco/projects/1998-99/game-theory/axelrod.html).

The strategies used for this tournament are:
  - Always defect
  - Always cooperate
  - Random - randomly defect or cooperate
  - Tit for Tat - cooperate at first, then pick whatever opponent did last time

# Realisation 
To realise this project we made two agents: a ControllerAgent and a PlayerAgent. The ControllerAgent is the main entry point for the program which boots up the rest. It adds four PlayerAgents to the system, one of each of the strategies mentioned earlier. It will then have its Behaviour called once which will let all the PlayerAgents play a game against each other for a set amount of times.

When the PlayerAgent receives a StartAction from the ControllerAgent, it will, depending on its StrategyType, send a CooperateAction or a DefectAction to its opponent. The opponent will respond to this message with a DefectAction or a CooperateAction of its own depending on its StrategyType. After this the game is over and both PlayerAgents will get the right amount of utility assigned.

When the ControllerAgent is done sending out all the StartActions to the PlayerAgents, the utilities of all the PlayerAgents will tell us more about the strategies.

# Tests 
We have performed the test a total of 5 times. The test consists of all PlayerAgents playing against each other 50 times. After each round the players get their utility (years of prison). The lower this utility, the better the strategy works. Before we go on to the results, we need to define the utilities used for this test:

| | A defect | A cooperate |
| ------ | ------ | ------ |
| **B defect** | 3, 3 | 0, 5 |
| **B cooperate** | 5, 0 | 1, 1 |

What this means is that when both cooperate, both will get a utility of 1. If B cooperates and A defects, then B gets a utility of 0 and A gets a utility of 5. This is the same the other way around. If both defect, then both will get a utility of 3.

#### Results
| | 1 | 2 | 3 | 4 | 5 | | AVERAGE  |
| ------ | ------ | ------  | ------ | ------ | ------ | ------ | ------ |
| **Random**  | 291 | 318  | 329 | 341 | 336 | | 323 |
| **Defect** | 240 | 228 | 204 | 207 | 213 | | 218.4 |
| **Cooperate** | 465 | 460 | 449 | 444 | 449 | | 453.4 |
| **Tit for Tat** | 319 | 333 | 333 | 336 | 333 | | 330.8 |

As we can see in the table above Tit for Tat, unlike the actual axelrod tournament, doesn't have the lowest utility. The results above show that always defecting is the best choice in case the players play against each other the same amount of times. Always cooperating is the worst strategy here. Random and Tit for Tat give roughly the same utility.  

# Running the code
To run the code, use the following command:
```sh
$ java jade.Boot -gui Controller:ControllerAgent
```
This will start up the ControllerAgent in JADE and show you the GUI as well. Make sure you've followed all the steps for the installation of JADE (see website right [here](http://jade.tilab.com/)).
