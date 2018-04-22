import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class Ice {

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
	
	private static double getMSENormalize(HashMap<Integer,Integer> ice, int n, double b0, double b1) {
		double MSE = 0;
		
		double xMean = 0;
		double SD = 0;
		// calculate xMean for normalizing
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			xMean += (double) e.getKey();
		}
		xMean = xMean / (double) n;
		// calculate Standard Deviation of x for normalizing
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {			
			SD += Math.pow(e.getKey() - xMean,2);
		}
		SD = SD / (n-1);
		SD = Math.sqrt(SD);
		
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			double newX = ((double) e.getKey() - xMean) / SD;
			MSE += Math.pow(b0 + (b1 * newX) - (double)e.getValue(), 2);
		}
		MSE = MSE / n;
		return MSE;
	}
	
	private static double[] getMSEGradient(HashMap<Integer,Integer> ice, int n, double b0, double b1) {
		double MSE0 = 0;
		double MSE1 = 0;
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			MSE0 += b0 + (b1 * (double)e.getKey()) - (double)e.getValue();
		}
		MSE0 *= 2.0;
		MSE0 = MSE0 / (double) n;
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			MSE1 += (b0 + (b1 * (double)e.getKey()) - (double)e.getValue()) * (double) e.getKey();
		}
		MSE1 *= 2.0;
		MSE1 = MSE1 / (double) n;
		double[] gradient = {MSE0, MSE1};
		return gradient;
	}
	
	private static double[] getMSEGradientNormalize(HashMap<Integer,Integer> ice, int n, double b0, double b1) {
		double MSE0 = 0;
		double MSE1 = 0;
		
		double xMean = 0;
		double SD = 0;
		// calculate xMean for normalizing
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			xMean += (double) e.getKey();
		}
		xMean = xMean / (double) n;
		// calculate Standard Deviation of x for normalizing
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			SD += Math.pow(e.getKey() - xMean,2);
		}
		SD = SD / (n-1);
		SD = Math.sqrt(SD);
		
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			double newX = ((double) e.getKey() - xMean) / SD;
			MSE0 += b0 + (b1 * newX) - (double)e.getValue();
		}
		MSE0 *= 2.0;
		MSE0 = MSE0 / (double) n;
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			double newX = ((double) e.getKey() - xMean) / SD;
			MSE1 += (b0 + (b1 * newX) - (double)e.getValue()) * newX;
		}
		MSE1 *= 2.0;
		MSE1 = MSE1 / (double) n;
		double[] gradient = {MSE0, MSE1};
		return gradient;
	}
	
	private static double[] getStochasticGradient(HashMap<Integer,Integer> ice, int n, double b0, double b1) {
		// add a little spice
		Random rng = new Random();
		int key = rng.nextInt(161) + 1855;
		
		double MSE0 = 0;
		double MSE1 = 0;
		
		double xMean = 0;
		double SD = 0;
		// calculate xMean for normalizing
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			xMean += (double) e.getKey();
		}
		xMean = xMean / (double) n;
		// calculate Standard Deviation of x for normalizing
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			SD += Math.pow(e.getKey() - xMean,2);
		}
		SD = SD / (n-1);
		SD = Math.sqrt(SD);
		
		
		int value = ice.get(key);
		double newX = ((double)key - xMean) / SD;
		/*for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			
			MSE0 += b0 + (b1 * newX) - (double)e.getValue();
		}*/
		MSE0 = 2.0 * (b0 + (b1 * newX) - value);
		//MSE0 = MSE0 / (double) n;
		MSE1 = MSE0 * newX;
		//MSE1 *= 2.0;
		//MSE1 = MSE1 / (double) n;
		double[] gradient = {MSE0, MSE1};
		return gradient;
	}
	
	private static double[] getDirectMSE(HashMap<Integer,Integer> ice, int n)  {
		double b1_top = 0;
		double xMean = 0;
		double yMean = 0;
		// calculate xMean and yMean to be used in other calculations
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			xMean += (double) e.getKey();
			yMean += (double) e.getValue();
		}
		xMean = xMean / (double) n;
		yMean = yMean / (double) n;
				
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			b1_top += ((double) e.getKey() - xMean) * ((double) e.getValue() - yMean);
		}
		double b1_bottom = 0;
		for (Map.Entry<Integer,Integer> e : ice.entrySet()) {
			b1_bottom += Math.pow((double) e.getKey() - xMean,2);
		}
		double b1 = b1_top / b1_bottom;
		double b0 = yMean - (b1 * xMean);
		double MSE = getMSE(ice, n, b0, b1);
		
		double[] direct = {b0, b1, MSE};
		return direct;
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
		if (args.length < 1) {
			System.out.println("Please enter arguments");
			return;
		}
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
			double b0 = Double.valueOf(args[1]);
			double b1 = Double.valueOf(args[2]);
			System.out.println(String.format("%.2f", getMSE(ice, n, b0, b1)));
		} else if (flag == 400) {
			double b0 = Double.valueOf(args[1]);
			double b1 = Double.valueOf(args[2]);
			double[] gradient = getMSEGradient(ice, n, b0, b1);
			System.out.println(String.format("%.2f", gradient[0]));
			System.out.println(String.format("%.2f", gradient[1]));
		} else if (flag == 500) {
			double step = Double.valueOf(args[1]);
			int t = Integer.valueOf(args[2]);
			double b0 = 0;
			double b1 = 0;
			for (int i = 1; i <= t; i++) {
				double[] gradient = getMSEGradient(ice, n, b0, b1);
				b0 = b0 - (step * gradient[0]);
				b1 = b1 - (step * gradient[1]);
				System.out.print(i + " ");
				System.out.print(String.format("%.2f", b0) + " ");
				System.out.print(String.format("%.2f", b1) + " ");
				System.out.println(String.format("%.2f", getMSE(ice, n, b0, b1)));
			}
		} else if (flag == 600) {
			double[] direct = getDirectMSE(ice, n);
			System.out.print(String.format("%.2f", direct[0]) +  " ");
			System.out.print(String.format("%.2f", direct[1]) + " ");
			System.out.println(String.format("%.2f", direct[2]));
		} else if (flag == 700) {
			double year = Double.valueOf(args[1]);
			double[] direct = getDirectMSE(ice,n);
			System.out.println(String.format("%.2f", direct[0] + (direct[1] * year)));
		} else if (flag == 800) {
			double step = Double.valueOf(args[1]);
			int t = Integer.valueOf(args[2]);
			double b0 = 0;
			double b1 = 0;
			for (int i = 1; i <= t; i++) {
				double[] gradient = getMSEGradientNormalize(ice, n, b0, b1);
				b0 = b0 - (step * gradient[0]);
				b1 = b1 - (step * gradient[1]);
				System.out.print(i + " ");
				System.out.print(String.format("%.2f", b0) + " ");
				System.out.print(String.format("%.2f", b1) + " ");
				System.out.println(String.format("%.2f", getMSENormalize(ice, n, b0, b1)));
			}
		} else if (flag == 900) {
			double step = Double.valueOf(args[1]);
			int t = Integer.valueOf(args[2]);
			double b0 = 0;
			double b1 = 0;
			for (int i = 1; i <= t; i++) {
				double[] gradient = getStochasticGradient(ice, n, b0, b1);
				b0 = b0 - (step * gradient[0]);
				b1 = b1 - (step * gradient[1]);
				System.out.print(i + " ");
				System.out.print(String.format("%.2f", b0) + " ");
				System.out.print(String.format("%.2f", b1) + " ");
				System.out.println(String.format("%.2f", getMSENormalize(ice, n, b0, b1)));
			}
		}

	}

}
