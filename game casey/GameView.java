import java.util.*;

public class GameView {
	private Scanner sc;
	String input;

	
	GameView(){
		sc = new Scanner(System.in);
	}
	
	public String getInput(String prompt) {
		System.out.println(prompt);
		input = sc.nextLine();
		return input;
	}

	public void display(String msg) {
		System.out.println(msg);		
	}
}
