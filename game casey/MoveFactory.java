
public class MoveFactory {
	private static int dieThrow;
	
	public static int calculateMoveDistance(int moveType, int currentPos, int firstPos, int lastPos){
		if(moveType == 1){
			Move m = new MoveType1(currentPos, firstPos, lastPos);
			dieThrow = m.getDieThrow();
			return m.calcDistance();
		}
		else if(moveType == 2){
			Move m = new MoveType2(currentPos, firstPos, lastPos);
			dieThrow = m.getDieThrow();
			return m.calcDistance();
		}
		else if(moveType == 3){
			Move m = new MoveType3(currentPos, firstPos, lastPos);
			dieThrow = m.getDieThrow();
			return m.calcDistance();
		}
		else return 0;

	}
	
	public static int calculateMoveDuration(){
		return (int) (Math.random() * 4) + 2;
	}
	
	public static int calculateMoveType(){
		return (int) (Math.random() * 3) + 1;
	}
	
	public static int getDieThrow(){
		return dieThrow;
	}
}

