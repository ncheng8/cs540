
abstract class Move {
	protected int dieThrow, currentPos, firstPos, lastPos;
	
	public Move(int currentPos, int firstPos, int lastPos){
		this.currentPos = currentPos;
		this.firstPos = firstPos;
		this.lastPos = lastPos;
		dieThrow = (int) (Math.random() * 6) + 1;
	}
	
	public abstract int calcDistance();
	
	public int getDieThrow(){
		return dieThrow;
	}
}

class MoveType1 extends Move{
	public MoveType1(int currentPos, int firstPos, int lastPos){
		super(currentPos, firstPos, lastPos);
	}

	public int calcDistance() {
		
		if(dieThrow >= 3){
			return (int)(dieThrow + (( firstPos - currentPos ) / 2));
		}
		else{
			return (((int)(dieThrow + (( firstPos - currentPos ) / 2))) * (-1));
		}
	}
}

class MoveType2 extends Move{
	public MoveType2(int currentPos, int firstPos, int lastPos){
		super(currentPos, firstPos, lastPos);
	}

	public int calcDistance() {

		if((dieThrow % 2) == 0){
			return (3 * dieThrow);
		}
		else{
			return dieThrow;
		}
	}
}

class MoveType3 extends Move{
	public MoveType3(int currentPos, int firstPos, int lastPos){
		super(currentPos, firstPos, lastPos);
	}

	public int calcDistance() {

		if(dieThrow <= 2){
			return (int)(dieThrow + (( currentPos - lastPos ) / 2));
		}
		else{
			return (((int)(dieThrow + (( currentPos - lastPos ) / 2))) * (-1));
		}
	}
}
