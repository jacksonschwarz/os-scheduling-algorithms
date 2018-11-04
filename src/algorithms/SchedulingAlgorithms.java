package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

/*
 * Wants to enter all 4 values. 
 * No tables. Wants avg WT and avg TAT
 * Need to print Gantt charts
 * Needs both versions of RR
 * Wants to be able to do one algorithm at a time, or multiple
 */

class Pair {
	private int pid, time;

	public Pair(int pid, int time) {
		this.pid = pid;
		this.time = time;
	}

	public int getPid() {
		return this.pid;
	}

	public int getTime() {
		return this.time;
	}

}

class Process {
	private int pid, bt, at, p, rt, wt = 0, tat = 0;
	private boolean completed = false, hasArrived = false;

	public Process(int pid, int bt, int at, int p) {
		this.pid = pid;
		this.bt = bt;
		this.at = at;
		this.p = p;
		this.rt = bt;
	}

	public Process(int pid, int bt, int at) {
		this(pid, bt, at, -1);
	}
	
	public Process(int pid, int bt) {
		this(pid, bt, 0, -1);
	}
	
	public Process(int pid) {
		this(pid, 0, 0, -1);
	}

	public void toggleArrive() {
		this.hasArrived = !this.hasArrived;
	}

	public void toggleComplete() {
		this.completed = !this.completed;
	}

	public boolean hasArrived() {
		return this.hasArrived;
	}

	public boolean isComplete() {
		return this.completed;
	}

	public int getBT() {
		return this.bt;
	}

	public int getPID() {
		return this.pid;
	}

	public void setBT(int bt) {
		this.bt = bt;
	}

	public void setAT(int at) {
		this.at = at;
	}

	public int getAT() {
		return this.at;
	}

	public int getP() {
		return this.p;
	}

	public int getRT() {
		return this.rt;
	}

	public int getWT() {
		return this.wt;
	}

	public void setWT(int wt) {
		this.wt = wt;
	}

	public int getTAT() {
		return this.tat;
	}

	public void setTAT(int tat) {
		this.tat = tat;
	}

	public void progressProcess() {
		this.rt--;
	}

	public static Comparator<Process> pidSort = new Comparator<Process>() {
		public int compare(Process one, Process two) {
			return one.getPID() - two.getPID();
		}
	};

	public static Comparator<Process> arrivalTimeSort = new Comparator<Process>() {

		public int compare(Process one, Process two) {
			int arr1 = one.getAT();
			int arr2 = two.getAT();
			if (arr1 != arr2) {
				return arr1 - arr2;
			} else {
				return pidSort.compare(one, two);
			}
		}
	};

	public static Comparator<Process> burstTimeSort = new Comparator<Process>() {

		public int compare(Process one, Process two) {
			int burst1 = one.getBT();
			int burst2 = two.getBT();
			if (burst1 != burst2) {
				return burst1 - burst2;
			} else {
				return arrivalTimeSort.compare(one, two);
			}
		}
	};

	public String toString() {
		return "PID: " + this.pid + "\tBurst Time: " + this.bt + "\tArrival Time: " + this.at + "\tWait Time: "
				+ this.wt + "\tTurn Around Time: " + this.tat;
	}
}

public class SchedulingAlgorithms {

	public static void displayProcesses(ArrayList<Process> processes) {
		double avgWT = 0, avgTAT = 0;
		
		Collections.sort(processes, Process.pidSort);
		
		for (Process p : processes) {
			System.out.println(p);
			avgWT += p.getWT();
			avgTAT += p.getTAT();
		}
		
		System.out.printf("Average WT: %.2f\nAverage TAT: %.2f\n", avgWT/processes.size(), avgTAT/processes.size());
		
	}

	public static int sumBT(ArrayList<Process> processes) {
		int sum = 0;
		for (int i = 0; i < processes.size(); i++) {
			sum += processes.get(i).getBT();
		}
		return sum;
	}

	public static boolean allComplete(ArrayList<Process> processes) {
		for (int i = 0; i < processes.size(); i++) {
			if (!processes.get(i).isComplete()) {
				return false;
			}
		}
		return true;
	}

	public static ArrayList<Process> getArrivedProcesses(ArrayList<Process> processes, int t) {
		ArrayList<Process> arrivedProcesses = new ArrayList<Process>();
		for (int i = 0; i < processes.size(); i++) {
			if (t == processes.get(i).getAT()) {
				arrivedProcesses.add(processes.get(i));
			}
		}
		return arrivedProcesses;
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		ArrayList<Process> processes = new ArrayList<Process>();

		System.out.println("Enter each processes' PID, separated by a space: ");
		String[] pids = scan.nextLine().split("\\s");
		for (int i = 0; i < pids.length; i++) {
			processes.add(new Process(Integer.parseInt(pids[i])));
		}

		System.out.println("Enter each processes' burst time, separated by a space: ");

		String[] burstTimes = scan.nextLine().split("\\s");
		if (burstTimes.length != pids.length) {
			System.out.println("Each process must have an arrival time.");
			System.exit(0);
		}
		for (int i = 0; i < pids.length; i++) {
			processes.get(i).setBT(Integer.parseInt(burstTimes[i]));
		}

		System.out.println("Will you be entering arrival times? 'Y' if yes, 'N' if no.");

		if (scan.nextLine().equals("Y")) {
			System.out.println("Enter each processes' arrival time, separated by a space: ");
			String[] arrivalTimes = scan.nextLine().split("\\s");
			if (arrivalTimes.length != pids.length) {
				System.out.println("Each process must have an arrival time.");
				System.exit(0);
			}
			for (int i = 0; i < pids.length; i++) {
				processes.get(i).setAT(Integer.parseInt(arrivalTimes[i]));
			}
		}

		System.out.println("What sorting algorithm would you like to use (FCFS, SJF, SRT, Priority, or RR)? ");
		String algo = scan.nextLine();
		switch (algo) {
		case "FCFS":
			FCFS(processes);
			break;
		case "SJF":
			SJF(processes);
			break;
		case "SRT":
			SRT(processes);
			break;
		case "Priority":
			Priority(processes);
			break;
		case "RR":
			RR(processes);
			break;
		default:
			System.out.println("No correct algorithm selected. Exiting system.");
			System.exit(0);
		}
		displayProcesses(processes);
	}

	private static void FCFS(ArrayList<Process> processes) {
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		Collections.sort(processes, Process.arrivalTimeSort);
		int totalSum = 0;
		for (int i = 0; i < processes.size(); i++) {
			Process curr = processes.get(i);
			pairs.add(new Pair(curr.getPID(), totalSum));
			curr.setWT(totalSum);
			totalSum += curr.getBT();
			curr.setTAT(totalSum);
		}
		printGantt(pairs); // prints the Gantt chart for this sorting algorithm
	}

	private static void SJF(ArrayList<Process> processes) {
		Collections.sort(processes, Process.arrivalTimeSort); // sorts by arrival time to get the first process
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		Process firstProcess = processes.get(0);
		pairs.add(new Pair(firstProcess.getPID(), firstProcess.getAT()));
		firstProcess.setWT(0); // first process doesn't wait
		firstProcess.setTAT(processes.get(0).getBT()); // TAT is just WT+BT, or in this case just BT since WT is 0
		firstProcess.toggleComplete(); // this process is now complete
		int loopStart = firstProcess.getAT() + firstProcess.getBT(); // this is the time that the next process starts at
		Collections.sort(processes, Process.burstTimeSort); // now we care about burst time, so it is sorted that way
		while (!allComplete(processes)) { // loops until all processes have been completed
			for (int i = 0; i < processes.size(); i++) { // loops through all processes
				Process p = processes.get(i);
				if (p.getAT() <= loopStart && !p.isComplete()) { // if the process has arrived and is not complete,
					pairs.add(new Pair(p.getPID(), loopStart)); // we'll use it
					p.setWT(loopStart - p.getAT()); // WT is the time is was started - arrival time
					p.setTAT(p.getBT() + p.getWT()); // TAT = BT+WT
					p.toggleComplete(); // toggles to true
					loopStart += p.getBT(); // next process starts at this processes start time + this processes BT
					break; // breaks out of the for loop so that it will start back at the 0 index process
				}
				if (i == processes.size() - 1) {// if no processes have arrived, then the loopStart will increment (idle
												// CPU time)
					loopStart++;
				}
			}
		}
		printGantt(pairs);
	}

	private static void SRT(ArrayList<Process> processes) {
		// shortest remaining time goes first

	}

	private static void Priority(ArrayList<Process> processes) {
		// processes go based on prioirty
	}

	private static void RR(ArrayList<Process> processes) {
		// processes go based on the Quantum Q. Can be either fixed or vairable quantum
	}

	/*
	 * Prints the Gantt chart for the arraylist of pairs
	 */
	private static void printGantt(ArrayList<Pair> pairs) {
		Iterator<Pair> pairIt = pairs.iterator();
		String first = "", second = "";

		while (pairIt.hasNext()) {
			Pair cur = pairIt.next();
			first += "| " + cur.getPid() + " |";
			second += "  " + cur.getTime() + "  ";

			if (pairIt.hasNext()) {
				first += " ====== ";
				second += "        ";
			}
		}
		System.out.println(first);
		System.out.println(second);
	}
}
