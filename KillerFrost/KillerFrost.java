import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class KillerFrost {

	private static String filename = "./data.txt";

	private static HashMap readIce() {
		//ArrayList<Integer> corpus = new ArrayList<Integer>();
		HashMap<Integer,Integer> ice = new HashMap<Integer,Integer>();
		try {
			File f = new File(filename);
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				if (sc.hasNextInt()) {
					int i = sc.nextInt();
					int j = sc.nextInt();
					ice.put(i, j);
				} else {
					sc.next();
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found.");
		}
		//return corpus;
		return ice;
	}
	public static void main(String[] args) {
		HashMap<Integer,Integer> ice = readIce();
		Set<Entry<Integer, Integer>> set = ice.entrySet();
		Iterator<Entry<Integer, Integer>> i = set.iterator();
		while (i.hasNext()) {
			Map.Entry<Integer,Integer> curr = (Map.Entry<Integer,Integer>)i.next();
			System.out.print(curr.getKey() + ": ");
			System.out.println(curr.getValue());
		}

	}

}
