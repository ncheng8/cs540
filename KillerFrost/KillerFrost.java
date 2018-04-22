import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class KillerFrost {

	private static String filename = "./data.txt";

	private static HashMap<Integer,Integer> readIce() {
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
		return ice;
	}
	
	private static double[] getStats(HashMap<Integer,Integer> ice) {
		double[] stats = new double[3];
		double n = ice.size();
		double mean = 0;
		double SD = 0;
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			mean += e.getValue();
		}
		mean = mean / n;
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			SD += Math.pow(e.getValue() - mean,2);
		}
		SD = SD / (n-1);
		SD = Math.sqrt(SD);
		stats[0] = n;
		stats[1] = mean;
		stats[2] = SD;
		return stats;
	}
	
	private static double getMSE(HashMap<Integer,Integer> ice, int n, double b0, double b1) {
		double MSE = 0;
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			MSE += Math.pow(b0 + (b1 * (double)e.getKey()) - (double)e.getValue(), 2);
		}
		MSE = MSE / n;
		return MSE;
	}
	
	private static void printData(HashMap<Integer,Integer> ice) {
		Set<Entry<Integer, Integer>> set = ice.entrySet();
		Iterator<Entry<Integer, Integer>> i = set.iterator();
		while (i.hasNext()) {
			Map.Entry<Integer,Integer> curr = (Map.Entry<Integer,Integer>)i.next();
			System.out.print(curr.getKey() + " ");
			System.out.println(curr.getValue());			
		}
	}
	
	
	public static void main(String[] args) {
		// read in data set
		HashMap<Integer,Integer> ice = readIce();
		// compute statistics for the data
		double[] stats = getStats(ice);
		int n = (int) stats[0];
		double mean = stats[1];
		double SD = stats[2];
		// don't forget the command line arguments
		int flag = Integer.valueOf(args[0]);		
		if (flag == 100) {
			printData(ice);
		} else if (flag == 200) {
			System.out.println(n);
			System.out.println(String.format("%.2f", mean));
			System.out.println(String.format("%.2f", SD));			
		} else if (flag == 300) {
			double b1 = Double.valueOf(args[1]);
			double b2 = Double.valueOf(args[2]);
			System.out.println(String.format("%.2f", getMSE(ice, n, b1, b2)));
		}

	}

}
