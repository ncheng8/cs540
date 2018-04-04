import java.util.*;

import javax.swing.JFrame;

class State {
	char[] board;

	public State(char[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
	}

	public int getScore() {

		// TO DO: return game theoretic value of the board
		int dark = 0;
		int light = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i] == '1') {
				dark++;
			} else if (board[i] == '2') {
				light++;
			}
		}
		if (dark > light) {
			return 1;
		} else if (light > dark) {
			return -1;
		} else {
			return 0;
		}
	}

	public boolean isTerminal() {

		// TO DO: determine if the board is a terminal node or not and return boolean
		boolean isTerminal;
		if (getSuccessors('1') == null && getSuccessors('2') == null) {
			isTerminal = true;
			// System.out.println(getScore());
		} else {
			isTerminal = false;
			// System.out.println("non-terminal");
		}
		return isTerminal;
	}

	public boolean isOnBoard(int x, int y) {
		return x >= 0 && x <= 3 && y >= 0 && y <= 3;
	}

	public State[] getSuccessors(char player) {

		// TO DO: get all successors and return them in proper order
		int numSuccessors = 0;
		char enemy;
		if (player == '1') {
			enemy = '2';
		} else {
			enemy = '1';
		}
		// array of x and y values around the block to check for valid moves
		int[][] directions = new int[][] { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 },
				{ -1, 1 } };
		State[] successors = new State[14], successors2 = null;
		// search board for empty spaces, check if it is a valid move after
		for (int i = 0; i < this.board.length; i++) {
			State tempstate = new State(this.board);
			if (tempstate.board[i] == '0') {
				// try to place chip here
				// System.out.println(tempstate.getBoard());
				tempstate.board[i] = player;
				// System.out.println(tempstate.getBoard());
				int x = i % 4;
				int y = i / 4;
				int numFlip = 0;
				int[] tilesToFlip = new int[6];
				// search in every direction around the empty block for blocks to flip
				for (int j = 0; j < 8; j++) {
					// numFlip = 0;
					int nx = x + directions[j][0]; // first step in the direction
					int ny = y + directions[j][1]; // first step in the direction
					int temp = nx + (4 * ny);
					if (isOnBoard(nx, ny)) {
						if (tempstate.board[temp] == enemy) {
							// An enemy player's piece is next to our piece
							nx += directions[j][0];
							ny += directions[j][1];
							if (!isOnBoard(nx, ny)) {
								continue;
							}
							int slide = nx + (4 * ny);
							while (tempstate.board[slide] == enemy) {
								nx += directions[j][0];
								ny += directions[j][1];
								slide = nx + (4 * ny);
								if (!isOnBoard(nx, ny)) { // break out of while loop, continue for loop
									break;
								}
							}
							if (!isOnBoard(nx, ny)) {
								continue;
							}
							slide = nx + (4 * ny);

							if (tempstate.board[slide] == player) {
								while (true) {
									nx -= directions[j][0];
									ny -= directions[j][1];
									if (nx == x && ny == y) {
										break;
									}
									tilesToFlip[numFlip] = nx + (4 * ny);
									numFlip++;
								}
							}

						}
					}
				}
				// now create a successor state if found
				if (numFlip > 0) {
					for (int f = 0; f < numFlip; f++) {
						tempstate.board[tilesToFlip[f]] = player;
					}
					successors[numSuccessors] = tempstate;
					numSuccessors++;
				}

			}
		}
		if (numSuccessors == 0) {
			return null;
		}
		successors2 = new State[numSuccessors];
		for (int i = 0; i < numSuccessors; i++) {
			successors2[i] = successors[i];
		}
		return successors2;
	}

	public void printState(int option, char player) {

		// TO DO: print a State based on option (flag)
		char enemy;
		if (player == '1') {
			enemy = '2';
		} else {
			enemy = '1';
		}
		if (option == 1) {
			State[] successors = getSuccessors(player);
			// check both players to see if a turn should be skipped
			if (successors == null) {
				successors = getSuccessors(enemy);
				if (successors == null) {
					System.out.println("No successors");
					return;
				}
			}
			// print every successor
			for (State s : successors) {
				System.out.println(s.getBoard());
			}
		} else if (option == 2) {
			// print terminal node's score, or "non-terminal" if it is not
			if (this.isTerminal() == false) {
				System.out.println("non-terminal");
			} else {
				System.out.println(getScore());
			}
		} else if (option == 3) {
			// simply run minmax on the state
			int t = Minimax.run(this, player);
			System.out.println(t);
			System.out.println(Minimax.count);
		} else if (option == 4) {
			// dont print anything if the state is terminal
			if (this.isTerminal()) {
				return;
			} else {
				State[] successors = getSuccessors(player);
				// print the board if the current player has no moves
				if (successors == null) {
					System.out.println(this.getBoard());
				}
				int optimal;
				if (player == '1') {
					optimal = 1;
				} else {
					optimal = -1;
				}
				// search through every successor and call minmax on them
				// if a successor attains the optimal value, print it and stop
				for (State s : successors) {
					int p = Minimax.run(s, player);
					if (p == optimal) {
						System.out.println(s.getBoard());
						return;
					}
				}
				// no successor attains the optimal value, so print the board
				System.out.println(this.getBoard());
				return;
			}
		} else if (option == 5) {
			int t = Minimax.run_with_pruning(this, player);
			System.out.println(t);
			System.out.println(Minimax.count);
		}else if (option == 6) {
			// dont print anything if the state is terminal
			if (this.isTerminal()) {
				return;
			} else {
				State[] successors = getSuccessors(player);
				// print the board if the current player has no moves
				if (successors == null) {
					System.out.println(this.getBoard());
				}
				int optimal;
				if (player == '1') {
					optimal = 1;
				} else {
					optimal = -1;
				}
				// search through every successor and call minmax on them
				// if a successor attains the optimal value, print it and stop
				for (State s : successors) {
					int p = Minimax.run_with_pruning(s, player);
					if (p == optimal) {
						System.out.println(s.getBoard());
						return;
					}
				}
				// no successor attains the optimal value, so print the board
				System.out.println(this.getBoard());
				return;
			}
		}

	}

	public String getBoard() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			builder.append(this.board[i]);
		}
		return builder.toString().trim();
	}

	public boolean equals(State src) {
		for (int i = 0; i < 16; i++) {
			if (this.board[i] != src.board[i])
				return false;
		}
		return true;
	}
}

class Minimax {
	public static int count = 0;

	private static int max_value(State curr_state) {

		// TO DO: implement Max-Value of the Minimax algorithm
		count++;
		if (curr_state.isTerminal()) {
			return curr_state.getScore();
		}
		State[] states = curr_state.getSuccessors('1');
		if (states == null) {
			return min_value(curr_state);
		}
		int a = -999;
		for (State s : states) {
			int k = min_value(s);
			if (k > a)
				a = k;
		}
		return a;
	}

	private static int min_value(State curr_state) {

		// TO DO: implement Min-Value of the Minimax algorithm
		count++;
		if (curr_state.isTerminal()) {
			return curr_state.getScore();
		}
		State[] states = curr_state.getSuccessors('2');
		if (states == null) {
			return max_value(curr_state);
		}
		int b = 999;
		for (State s : curr_state.getSuccessors('2')) {
			int k = max_value(s);
			if (k < b)
				b = k;
		}
		return b;
	}

	private static int max_value_with_pruning(State curr_state, int alpha, int beta) {

		// TO DO: implement Max-Value of the alpha-beta pruning algorithm
		count++;
		int a = alpha;
		int b = beta;
		if (curr_state.isTerminal()) {
			return curr_state.getScore();
		}
		State[] states = curr_state.getSuccessors('1');
		if (states == null) {
			return min_value_with_pruning(curr_state, a, b);
		}

		for (State s : states) {
			int k = min_value_with_pruning(s, a, b);
			if (k > a)
				a = k;
			if (a > b)
				return b;
		}
		return a;
	}

	private static int min_value_with_pruning(State curr_state, int alpha, int beta) {

		// TO DO: implement Min-Value of the alpha-beta pruning algorithm
		count++;
		int a = alpha;
		int b = beta;
		if (curr_state.isTerminal()) {
			return curr_state.getScore();
		}
		State[] states = curr_state.getSuccessors('2');
		if (states == null) {
			return max_value_with_pruning(curr_state, a, b);
		}
		for (State s : curr_state.getSuccessors('2')) {
			int k = max_value_with_pruning(s, a, b);
			if (k < b)
				b = k;
			if (a > b)
				return a;
		}
		return b;
	}

	public static int run(State curr_state, char player) {

		// TO DO: run the Minimax algorithm and return the game theoretic value
		if (player == '1') {
			return max_value(curr_state);
		} else {
			return min_value(curr_state);

		}
	}

	public static int run_with_pruning(State curr_state, char player) {

		// TO DO: run the alpha-beta pruning algorithm and return the game theoretic
		// value
		if (player == '1') {
			return max_value_with_pruning(curr_state, -999, 999);
		} else {
			return min_value_with_pruning(curr_state, -999, 999);
		}

	}
}

public class Reversi {
	public static void main(String args[]) {
		if (args.length != 3) {
			System.out.println("Invalid Number of Input Arguments");
			return;
		}
		int flag = Integer.valueOf(args[0]);
		char[] board = new char[16];
		for (int i = 0; i < 16; i++) {
			board[i] = args[2].charAt(i);
		}
		int option = flag / 100;
		char player = args[1].charAt(0);
		if ((player != '1' && player != '2') || args[1].length() != 1) {
			System.out.println("Invalid Player Input");
			return;
		}
		State init = new State(board);
		init.printState(option, player);
	}
}
