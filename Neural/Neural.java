import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class Neural {
	private static String filename1 = "./hw2_midterm_A_train.txt";
	private static String filename2 = "./hw2_midterm_A_eval.txt";
	
	public static double getPartial(double vJ, double y) {
		double dEvJ = vJ - y;
		double dEuC = dEvJ * vJ * (1-vJ);
		return dEuC;
	}
	
	public static List<Student> getStudents() {
		List<Student> students = new ArrayList<Student>();
		try {
			File f = new File(filename1);
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				if (sc.hasNextDouble()) {
					double x1 = sc.nextDouble();
					double x2 = sc.nextDouble();
					int y = sc.nextInt();
					students.add(new Student(x1,x2,y));
				} else {
					sc.next();
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found.");
		}
		return students;
	}
	
	public static List<Student> getEval() {
		List<Student> students = new ArrayList<Student>();
		try {
			File f = new File(filename2);
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				if (sc.hasNextDouble()) {
					double x1 = sc.nextDouble();
					double x2 = sc.nextDouble();
					int y = sc.nextInt();
					students.add(new Student(x1,x2,y));
				} else {
					sc.next();
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found.");
		}
		return students;
	}
	
	public static double[] getEpoch(double[] wP, double x1, double x2, int y, double n) {
		double[] w = wP;
		
		double uA = w[1] +  (x1 * w[2]) +  (x2 * w[3]);
		double uB = w[4] +  (x1 * w[5]) +  (x2 * w[6]);
		
		double vA = Math.max(uA, 0);
		double vB = Math.max(uB, 0);
		
		double uC = w[7] + (vA * w[8]) + (vB * w[9]);
		double vC = 1 / (1 + Math.exp(-uC));
		
		double[] pwd = new double[10];
		
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
		
		pwd[1] = dEuA;
		pwd[2] = dEuA * x1;
		pwd[3] = dEuA * x2;
		pwd[4] = dEuB;
		pwd[5] = dEuB * x1;
		pwd[6] = dEuB * x2;
		pwd[7] = dEuC;
		pwd[8] = dEuC * vA;
		pwd[9] = dEuC * vB;
		for (int i = 1; i < 10; i++) {
			w[i] = w[i] - (n * pwd[i]);
		}
		double eN = getSetError(w, getEval());
		
		w[0] = eN;
		return w;
	}
	
	public static double getSetError(double[] w, List<Student> s) {
		double rat = 0;
		for (int i = 0; i < 25; i++) {
			double uA = w[1] +  (s.get(i).getx1() * w[2]) +  (s.get(i).getx2() * w[3]);
			double uB = w[4] +  (s.get(i).getx1() * w[5]) +  (s.get(i).getx2() * w[6]);
			
			double vA = Math.max(uA, 0);
			double vB = Math.max(uB, 0);
			
			double uC = w[7] + (vA * w[8]) + (vB * w[9]);
			double vC = 1 / (1 + Math.exp(-uC));
			rat += 0.5 * Math.pow((vC - s.get(i).getY()),2);
		}
		return rat;
	}
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
		// dumb mistake, probably wont fix
		double x2 = 0;
		if (args.length > 11) {
			x2 = Double.valueOf(args[11]);
		}
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
			//double E = 0.5 * Math.pow(vC - y, 2);
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
		} else if (flag == 400) {
			double y = Double.valueOf(args[12]);
			double[] pwd = new double[10];
			
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
			
			pwd[1] = dEuA;
			pwd[2] = dEuA * x1;
			pwd[3] = dEuA * x2;
			pwd[4] = dEuB;
			pwd[5] = dEuB * x1;
			pwd[6] = dEuB * x2;
			pwd[7] = dEuC;
			pwd[8] = dEuC * vA;
			pwd[9] = dEuC * vB;
			
			for (int i = 1; i < 10; i++) {
				System.out.print(String.format("%.5f", pwd[i]) + " ");
			}
			
		} else if (flag == 500) {
			double y = Double.valueOf(args[12]);
			double n = Double.valueOf(args[13]);
			for (int i = 1; i < 10; i++) {
				System.out.print(String.format("%.5f",w[i]) + " ");
			}
			System.out.println("");
			double E = 0.5 * Math.pow(vC - y, 2);
			System.out.println(String.format("%.5f", E));
			double[] pwd = new double[10];
			
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
			
			pwd[1] = dEuA;
			pwd[2] = dEuA * x1;
			pwd[3] = dEuA * x2;
			pwd[4] = dEuB;
			pwd[5] = dEuB * x1;
			pwd[6] = dEuB * x2;
			pwd[7] = dEuC;
			pwd[8] = dEuC * vA;
			pwd[9] = dEuC * vB;
			for (int i = 1; i < 10; i++) {
				w[i] = w[i] - (n * pwd[i]);
			}
			uA = w[1] +  (x1 * w[2]) +  (x2 * w[3]);
			uB = w[4] +  (x1 * w[5]) +  (x2 * w[6]);
			
			vA = Math.max(uA, 0);
			vB = Math.max(uB, 0);
			
			uC = w[7] + (vA * w[8]) + (vB * w[9]);
			vC = 1 / (1 + Math.exp(-uC));
			
			for (int i = 1; i < 10; i++) {
				System.out.print(String.format("%.5f",w[i]) + " ");
			}
			System.out.println("");
			double eN = 0.5 * Math.pow(vC - y, 2);
			System.out.println(String.format("%.5f", eN));			
		} else if (flag == 600) {
			double n = Double.valueOf(args[10]);
			List<Student> students = getStudents();
			List<Student> eval = getEval();
			double[] response;
			for (Student s : students) {
				System.out.print(String.format("%.5f",s.getx1()) + " ");
				System.out.print(String.format("%.5f",s.getx2()) + " ");
				System.out.print(String.format("%.5f",(double)s.getY()) + "\n");
				response = getEpoch(w,s.getx1(),s.getx2(),s.getY(),n);
				for (int i = 1; i < 10; i++) {
					System.out.print(String.format("%.5f", response[i]) + " ");
				}
				System.out.println("");
				System.out.println(String.format("%.5f", response[0]));
			}
		}

	}

}

class Student {
	private double x1;
	private double x2;
	private int y;
	
	public Student(double x1, double x2, int y) {
		this.x1 = x1;
		this.x2 = x2;
		this.y = y;
	}
	
	public double getx1() {
		return x1;
	}
	
	public double getx2() {
		return x2;
	}
	
	public int getY() {
		return y;
	}
}

