import java.util.*;

public class GameController {
	private int numPlayers, firstPos, lastPos;
	private ArrayList<Player> playerList;
	public GameView view;
	
	public GameController(int numPlayers){
		this.numPlayers = numPlayers;
		firstPos = 0;
		lastPos = 0;
		view = new GameView();
		playerList = new ArrayList<Player>();
		instantiatePlayers();
	}
	
	private void instantiatePlayers(){
		for(int i = 0; i < numPlayers; i ++){
			Player newPlayer = new Player(i);
			playerList.add(newPlayer);
			newPlayer = null;
		}
	}
	
	public void runGame(){
		int turnCounter = 1;
		
		//repeats all logic inside loop for multiple turns and increments turn counter for display purposes
		while(firstPos < 50){
			Player currentPlayer, topPlayer, lowestPlayer;
			int move = 0;
			topPlayer = new Player(-1);
			lowestPlayer = new Player(-1);
			
			view.display("Start of turn " + turnCounter);
			//display all players location at start of turn
			for(int i = 0; i < numPlayers; i ++){
				currentPlayer = playerList.get(i);
				view.display("Player " + (i+1) + " is located at " + currentPlayer.getCurrentPos());	
			}
			view.display("The current highest position is " + firstPos + " and the lowest is " + lastPos + "\n");
			
			//loops through all players to do their moves (this logic is for 1 turn only)
			for(int i = 0; i < numPlayers; i ++){
				currentPlayer = playerList.get(i);
				
				//if the duration of the players move type is 0, recalculate a new move type and a new move duration for them
				if(currentPlayer.getMoveDuration() == 0){
					currentPlayer.setMoveType(MoveFactory.calculateMoveType());
					currentPlayer.setMoveDuration(MoveFactory.calculateMoveDuration());
				}
				
				//display output and calculate the players move based on several factors
				view.display("Player " + (i+1) + " is playing");
				move = MoveFactory.calculateMoveDistance(currentPlayer.getMoveType(), currentPlayer.getCurrentPos(), firstPos, lastPos);
				view.display("This player has a move type of " + currentPlayer.getMoveType() + " and a remaining duration of " + currentPlayer.getMoveDuration());
				view.display("This player rolled a " + MoveFactory.getDieThrow());
				view.display("This player will then move " + move + " from their position at " + currentPlayer.getCurrentPos());
				
				
				//Set the player's new position and lower their move duration by 1
				currentPlayer.setCurrentPos((move + currentPlayer.getCurrentPos()));
				currentPlayer.setMoveDuration(currentPlayer.getMoveDuration() - 1);

				//update the first and last position if the move will increase/decrease that value
				if(currentPlayer.getCurrentPos() > firstPos && move >= 0){
					firstPos = currentPlayer.getCurrentPos();
					topPlayer = currentPlayer;
				}
				
				if(currentPlayer.getCurrentPos() < lastPos && move <= 0){
					lastPos = currentPlayer.getCurrentPos();
					lowestPlayer = currentPlayer;
				}
			
			
				view.display("This player's new position is " + currentPlayer.getCurrentPos());
				view.display("The current highest position is " + firstPos + " and the lowest is " + lastPos + "\n");
				
				//checks if a winner occurs, then breaks the for loop if it did
				if(firstPos >= 50){
					System.out.print("Winner Player " + (i+1) + " with " + currentPlayer.getCurrentPos());
					break;
				}
				
				currentPlayer = null;
				move = 0;
			}
			turnCounter ++;
		}
	}
}
