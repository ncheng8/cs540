
public class Player {
	private int moveType, moveDuration, currentPos, identifier;

	public Player(int identifier){
		moveType = MoveFactory.calculateMoveType();
		moveDuration = MoveFactory.calculateMoveDuration();
		currentPos = 0;
		this.identifier = identifier;
	}

	public int getMoveDuration() {
		return moveDuration;
	}

	public void setMoveDuration(int moveDuration) {
		this.moveDuration = moveDuration;
	}

	public int getMoveType() {
		return moveType;
	}

	public void setMoveType(int moveType) {
		this.moveType = moveType;
	}

	public int getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}
	
	public int getIdentifier(){
		return identifier;
	}
	
	public boolean equals(Player p){
		if(p.getIdentifier() == identifier) return true;
		else return false;
	}
	
}
