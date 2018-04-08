
import java.util.*;
import java.io.*;

public class Chatbot {
	private static String filename = "./WARC201709_wid.txt";

	private static ArrayList<Integer> readCorpus() {
		ArrayList<Integer> corpus = new ArrayList<Integer>();
		try {
			File f = new File(filename);
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				if (sc.hasNextInt()) {
					int i = sc.nextInt();
					corpus.add(i);
				} else {
					sc.next();
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found.");
		}
		return corpus;
	}

	static public void main(String[] args) {
		ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);

		if (flag == 100) {
			int w = Integer.valueOf(args[1]);
			int count = 0;

			for (Integer word : corpus) {
				if (word == w) {
					count++;
				}
			}
			double prob = count / (double) corpus.size();
			prob = (double) Math.round(prob * 10000000d) / 10000000d;
			System.out.println(count);
			System.out.println(String.format("%.7f", prob));

		} else if (flag == 200) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			double r = n1 * 1.0 / n2;
			double currPos = 0;
			// list of all segments; 4700, one for each word
			ArrayList<Segment> allIntervals = new ArrayList<Segment>();
			// instantiate all the segments, put a word in each one
			// this way, the list is ordered based on word
			for (int i = 0; i <= 4700; i++) {
				int[] t = { i };
				allIntervals.add(new Segment(1, t));
			}
			// go through corpus and update the word counts as we go
			for (Integer t : corpus)
				allIntervals.get(t).addCount();
			// calculate the l and r values for each segment based on their counts 
			// and the size of the corpus
			for (Segment s : allIntervals) {
				s.setLHS(currPos);
				currPos += s.getCount() / (double) corpus.size();
				s.setRHS(currPos);
			}
			// search through the segment list and find the "randomly chosen" word
			for (Segment s : allIntervals) {
				if (r <= s.getRHS() && r > s.getLHS()) {
					System.out.println(s.wordIndex()[0]);
					System.out.println(String.format("%.7f", s.getLHS()));
					System.out.println(String.format("%.7f", s.getRHS()));
				}
			}
			//try binary search
			int index = binarySearch(allIntervals, r);
			Segment s = allIntervals.get(index);
			System.out.println(s.wordIndex()[0]);
			System.out.println(String.format("%.7f", s.getLHS()));
			System.out.println(String.format("%.7f", s.getRHS()));
		} else if (flag == 300) {
			int h = Integer.valueOf(args[1]);
			int w = Integer.valueOf(args[2]);
			int count = 0;
			ArrayList<Integer> words_after_h = new ArrayList<Integer>();
			// TODO

			// output
			System.out.println(count);
			System.out.println(words_after_h.size());
			System.out.println(String.format("%.7f", count / (double) words_after_h.size()));
		} else if (flag == 400) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			int h = Integer.valueOf(args[3]);
			// TODO

		} else if (flag == 500) {
			int h1 = Integer.valueOf(args[1]);
			int h2 = Integer.valueOf(args[2]);
			int w = Integer.valueOf(args[3]);
			int count = 0;
			ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
			// TODO

			// output
			System.out.println(count);
			System.out.println(words_after_h1h2.size());
			if (words_after_h1h2.size() == 0)
				System.out.println("undefined");
			else
				System.out.println(String.format("%.7f", count / (double) words_after_h1h2.size()));
		} else if (flag == 600) {
			int n1 = Integer.valueOf(args[1]);
			int n2 = Integer.valueOf(args[2]);
			int h1 = Integer.valueOf(args[3]);
			int h2 = Integer.valueOf(args[4]);
			// TODO
		} else if (flag == 700) {
			int seed = Integer.valueOf(args[1]);
			int t = Integer.valueOf(args[2]);
			int h1 = 0, h2 = 0;

			Random rng = new Random();
			if (seed != -1)
				rng.setSeed(seed);

			if (t == 0) {
				// TODO Generate first word using r
				double r = rng.nextDouble();
				System.out.println(h1);
				if (h1 == 9 || h1 == 10 || h1 == 12) {
					return;
				}

				// TODO Generate second word using r
				r = rng.nextDouble();
				System.out.println(h2);
			} else if (t == 1) {
				h1 = Integer.valueOf(args[3]);
				// TODO Generate second word using r
				double r = rng.nextDouble();
				System.out.println(h2);
			} else if (t == 2) {
				h1 = Integer.valueOf(args[3]);
				h2 = Integer.valueOf(args[4]);
			}

			while (h2 != 9 && h2 != 10 && h2 != 12) {
				double r = rng.nextDouble();
				int w = 0;
				// TODO Generate new word using h1,h2
				System.out.println(w);
				h1 = h2;
				h2 = w;
			}
		}

		return;
	}

	public static int binarySearch(ArrayList<Segment> list, double x) {
		// out of bounds check
		if (x < 0 || x >= list.size()) {
			return -1;
		}
		int l = 0;
		int r = list.size() - 1;
		int mid;
		while (l <= r) {
			mid = (l + r) / 2;
			if (x <= list.get(mid).getLHS()) { // search the left half
				r = mid - 1;
			} else if (x > list.get(mid).getRHS()) { // search the right half
				l = mid + 1;
			} else { // return if the middle value is the one we're looking for
				return mid;
			}
		}
		return -1;
	}
}

class Segment {
	private int type;
	private int[] index;
	private int word;
	private int count = 0;
	private double lhs;
	private double rhs;

	public Segment(int type, int[] index, double lhs, double rhs) {
		if (type == 1) {
			this.index = new int[1];
			this.index[0] = index[0];

		} else if (type == 2) {
			this.index = new int[2];
			this.index[0] = index[0];
			this.index[1] = index[1];
		} else {
			this.index = new int[3];
			this.index[0] = index[0];
			this.index[1] = index[1];
			this.index[2] = index[2];
		}
		this.type = type;
		this.lhs = lhs;
		this.rhs = rhs;

	}

	public Segment(int word) {
		if (type == 1) {
			this.index = new int[1];
			this.index[0] = index[0];

		} else if (type == 2) {
			this.index = new int[2];
			this.index[0] = index[0];
			this.index[1] = index[1];
		} else {
			this.index = new int[3];
			this.index[0] = index[0];
			this.index[1] = index[1];
			this.index[2] = index[2];
		}
		this.type = type;
		/*
		 * this.lhs = lhs; this.rhs = rhs;
		 */

	}

	public double getLHS() {
		return lhs;
	}

	public double getRHS() {
		return rhs;
	}

	public int[] wordIndex() {
		return index;
	}

	public void addCount() {
		this.count++;
	}

	public void setLHS(double x) {
		this.lhs = x;
	}

	public void setRHS(double x) {
		this.rhs = x;
	}

	public int getCount() {
		return count;
	}
}
