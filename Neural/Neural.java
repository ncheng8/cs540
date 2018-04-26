import java.util.*;
public class Neural {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Please enter arguments");
			return;
		}
		int flag = Integer.valueOf(args[0]);
		double [] w = new double[10];
		for (int i = 1; i <= 9; i++) {
			w[i] = Double.valueOf(args[i]);
		}
		double x1 = Double.valueOf(args[10]);
		double x2 = Double.valueOf(args[11]);
		
		double uA = w[1] +  (x1 * w[2]) +  (x2 * w[3]);
		double uB = w[4] +  (x1 * w[5]) +  (x2 * w[6]);
		
		double vA = Math.max(uA, 0);
		double vB = Math.max(uB, 0);
		
		double uC = w[7] + (vA * w[8]) + (vB * w[9]);
		double vC = 1 / (1 + Math.exp(-uC));
		
		if (flag == 100) {			
			System.out.print(String.format("%.5f", uA) + " ");
			System.out.print(String.format("%.5f", vA) + " ");
			System.out.print(String.format("%.5f", uB) + " ");
			System.out.print(String.format("%.5f", vB) + " ");
			System.out.print(String.format("%.5f", uC) + " ");
			System.out.print(String.format("%.5f", vC) + " ");
		} else if (flag == 200) {
			double y = Double.valueOf(args[12]);
			double E = 0.5 * Math.pow(vC - y, 2);
			double dEvC = vC - y;
			double dEuC = dEvC * vC * (1-vC);
			
			System.out.print(String.format("%.5f", E) + " ");
			System.out.print(String.format("%.5f", dEvC) + " ");
			System.out.print(String.format("%.5f", dEuC) + " ");
		} else if (flag == 300) {
			double y = Double.valueOf(args[12]);
			double E = 0.5 * Math.pow(vC - y, 2);
			double dEvC = vC - y;
			double dEuC = dEvC * vC * (1-vC);
			
			double dEvA = w[8] * dEuC;
			double dEvB = w[9] * dEuC;
			
			double dEuA = 0;
			double dEuB = 0;
			
			if (uA >= 0) 
				dEuA = dEvA;
			if (uB >= 0) 
				dEuB = dEvB;
			
			System.out.print(String.format("%.5f", dEvA) + " ");
			System.out.print(String.format("%.5f", dEuA) + " ");
			System.out.print(String.format("%.5f", dEvB) + " ");
			System.out.print(String.format("%.5f", dEuB) + " ");
		}

	}

}
