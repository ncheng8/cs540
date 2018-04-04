
import java.util.*;

class State {
	int[] board;
	State parentPt;
	int depth;

	public State(int[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
		this.parentPt = null;
		this.depth = 0;
	}

	public State[] getSuccessors() {
		State[] successors = new State[4];
		// TO DO: get all four successors and return them in sorted order
		int center = 0;
		// locate the empty tile
		for (int i = 0; i < 9; i++) {
			if (this.board[i] == 0) {
				center = i;
				break;
			}
			if (i == 9) {
				System.exit(0);
			}
		}
		// identify the swappable tiles around empty tile
		int x = center % 3;
		int y = center / 3;

		int xPlus = x + 1;
		int xMinus = x - 1;
		int yPlus = y + 1;
		int yMinus = y - 1;

		// logic for edge wrapping
		if (xPlus > 2)
			xPlus = 0;
		if (yPlus > 2)
			yPlus = 0;
		if (xMinus < 0)
			xMinus = 2;
		if (yMinus < 0)
			yMinus = 2;

		// xPlus,y; x,yPlus; xMinus,y; x,yMinus;
		int[] dPad = new int[4];
		dPad[0] = xPlus + 3 * y;
		dPad[1] = x + 3 * yPlus;
		dPad[2] = xMinus + 3 * y;
		dPad[3] = x + 3 * yMinus;

		int temp;
		// make all four moves and put those states into successors array
		for (int j = 0; j < 4; j++) {
			State t = new State(this.board);
			temp = t.board[dPad[j]];
			t.board[center] = temp;
			t.board[dPad[j]] = 0;
			successors[j] = t;
			t.parentPt = this;
		}
		// sort successors lowest to highest
		for (int k = 0; k < 3; k++) {
			for (int l = 0; l < 3 - k; l++) {
				if (successors[l].getBoardInt() > successors[l + 1].getBoardInt()) {
					State tmp = successors[l];
					successors[l] = successors[l + 1];
					successors[l + 1] = tmp;
				}
			}

		}
		return successors;
	}

	public void printState(int option) {

		// TO DO: print a torus State based on option (flag)
		// for 1XX, 2XX, and 3XX
		if (option == 1 || option == 2) {
			System.out.print(this.getBoard());
			if (option == 2) {
				System.out.print(" parent ");
				if (this.parentPt == null) {
					System.out.print("0 0 0 0 0 0 0 0 0");
				} else {
					System.out.print(this.parentPt.getBoard());
				}
			}
			System.out.println("");
		}

		// for 3XX
		if (option == 3) {
			List<State> goalPath = new ArrayList<>();
			State currState = this;
			while (currState != null) {
				goalPath.add(currState);
				currState = currState.parentPt;
			}
			for (int i = goalPath.size() - 1; i >= 0; i--) {
				System.out.println(goalPath.get(i).getBoard());
			}
		}
	}

	public String getBoard() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			builder.append(this.board[i]).append(" ");
		}
		return builder.toString().trim();
	}

	// simply returns the state as a comparable int
	public int getBoardInt() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			builder.append(this.board[i]);
		}
		return Integer.parseInt(builder.toString());
	}

	public boolean isGoalState() {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] != (i + 1) % 9)
				return false;
		}
		return true;
	}

	public boolean equals(State src) {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] != src.board[i])
				return false;
		}
		return true;
	}
}

public class Torus {

	public static void main(String args[]) {
		if (args.length < 10) {
			System.out.println("Invalid Input");
			return;
		}
		int flag = Integer.valueOf(args[0]);
		int[] board = new int[9];
		for (int i = 0; i < 9; i++) {
			board[i] = Integer.valueOf(args[i + 1]);
		}
		int option = flag / 100;
		int cutoff = flag % 100;
		if (option == 1) {
			State init = new State(board);
			State[] successors = init.getSuccessors();
			for (State successor : successors) {
				successor.printState(option);
			}
		} else if (option != 5) {
			State init = new State(board);
			Stack<State> stack = new Stack<>();
			List<State> prefix = new ArrayList<>();

			stack.push(init);
			prefix.add(init);
			init.depth = 0;
			int op4 = 0;
			while (!stack.isEmpty()) {
				State curr = stack.pop();
				// check prefix list, truncate
				for (int j = 0; j < prefix.size(); j++) {
					if (curr.parentPt == null) {
						prefix.add(curr);
						break;
					}
					if (prefix.get(j).equals(curr.parentPt)) {
						prefix.subList(j + 1, prefix.size()).clear();
						prefix.add(curr);
					}
				}
				// print states depending on option; 3 deviates from other options 1-4
				if (option == 3) {
					curr.printState(2);
				} else if (option < 4) {
					curr.printState(1);
				}
				// goal check
				if (curr.isGoalState()) {
					System.out.println("Goal state located");
					return;
				}
				if (curr.depth == op4 && option == 4) {
					System.out.println(curr.getBoard());
					op4++;
				}
				// expand successors by pushing them onto stack and prefix
				if (curr.depth < cutoff) {
					State[] succ = curr.getSuccessors();
					int currDepth = curr.depth + 1;
					for (int i = 0; i < 4; i++) {
						boolean inPrefix = false;
						for (int j = 0; j < prefix.size(); j++) {
							if (prefix.get(j).equals(succ[i])) {
								inPrefix = true;
							}
						}
						if (!inPrefix) {
							succ[i].depth = currDepth;
							stack.push(succ[i]);
						}
					}
				}
			}
		} else {
			State init = new State(board);
			Stack<State> stack = new Stack<>();
			List<State> prefix = new ArrayList<>();

			int goalChecked = 0;
			int maxStackSize = Integer.MIN_VALUE;
			int cut = 0;
			while (true) {
				prefix.clear();
				stack.push(init);
				prefix.add(init);
				init.depth = 0;
				while (!stack.isEmpty()) {
					if (stack.size() > maxStackSize) {
						maxStackSize = stack.size();
					}
					State curr = stack.pop();
					// check prefix list, truncate
					for (int j = 0; j < prefix.size(); j++) {
						if (curr.parentPt == null) {
							prefix.add(curr);
							break;
						}
						if (prefix.get(j).equals(curr.parentPt)) {
							prefix.subList(j + 1, prefix.size()).clear();
							prefix.add(curr);
						}
					}
					// goal check and print the goal path
					goalChecked++;
					if (curr.isGoalState()) {
						// System.out.println("Goal state located");
						curr.printState(3);
						System.out.println("Goal-check " + goalChecked);
						System.out.println("Max-stack-size " + maxStackSize);
						return;
					}
					// expand successors by pushing them onto stack and prefix
					if (curr.depth < cut) {
						State[] succ = curr.getSuccessors();
						int currDepth = curr.depth + 1;
						for (int i = 0; i < 4; i++) {
							boolean inPrefix = false;
							// check the prefix for repeat states
							for (int j = 0; j < prefix.size(); j++) {
								if (prefix.get(j).equals(succ[i])) {
									inPrefix = true;
								}
							}
							if (!inPrefix) {
								succ[i].depth = currDepth;
								stack.push(succ[i]);
							}
						}
					}
				}
				// increase depth for iterative deepening
				cut++;
			}
		}
	}
}
