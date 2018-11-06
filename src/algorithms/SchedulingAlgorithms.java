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
		this.rt = bt;
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

	public int getPriority() {
		return p;
	}

	public void setPriority(int aPriority) {
		this.p = aPriority;
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
	public static Comparator<Process> remainingTimeSort = new Comparator<Process>() {
		public int compare(Process one, Process two) {
			int rTime1 = one.getRT();
			int rTime2 = two.getRT();

			if (rTime1 != rTime2) {
				return rTime1 - rTime2;
			} else {
				return burstTimeSort.compare(one, two);
			}
		}
	};
	public static Comparator<Process> prioritySort = new Comparator<Process>() {
		public int compare(Process one, Process two) {
			int p1 = one.getP();
			int p2 = two.getP();
			if (p1 != p2) {
				return p1 - p2;
			} else {
				return remainingTimeSort.compare(one, two);
			}
		}
	};

	public String toString() {
		return "PID: " + this.pid + "\tBurst Time: " + this.bt + "\tArrival Time: " + this.at + "\tWait Time: "
				+ this.wt + "\tTurn Around Time: " + this.tat + "\tRemaining Time: " + this.rt;
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

		System.out.printf("Average WT: %.2f\nAverage TAT: %.2f\n", avgWT / processes.size(), avgTAT / processes.size());

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

		if (scan.nextLine().equalsIgnoreCase("Y")) {
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
			System.out.println("Enter the priority of each process, separated by a space");
			String[] priorities = scan.nextLine().split("\\s");
			if (priorities.length != pids.length) {
				System.out.println("Please enter the same number of priorities as there are processes");
				System.exit(0);
			} else {
				for (int i = 0; i < pids.length; i++) {
					processes.get(i).setPriority(Integer.parseInt(priorities[i]));
				}
			}
			Priority(processes);

			break;
		case "RR":
			System.out.println("Is the quantum fixed or variable? (f/v)");
			String isFixed = scan.nextLine();
			if (isFixed.equalsIgnoreCase("f")) {
				System.out.println("Enter the quantum");
				int quantum = scan.nextInt();
				RRFixed(processes, quantum);
			} else if (isFixed.equalsIgnoreCase("v")) {
				System.out.println("Enter the quantum");
				int quantum=scan.nextInt();
				RRVariable(processes, quantum);
			} else {
				System.out.println("Please enter either \"f\" for a fixed quantum or \"v\" for a variable quantum");
				System.exit(0);
			}
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

	private static ArrayList<Process> checkArrivedProcesses(int t, ArrayList<Process> processes) {
		ArrayList<Process> arrived = new ArrayList<Process>();
		for (Process p : processes) {
			if (t >= p.getAT()) {
				arrived.add(p);
			}
		}
		return arrived;
	}

	private static void updateWaitTimes(ArrayList<Process> processes, Process currentProcess) {
		for(int i = 0; i < processes.size(); i++) {
			if(processes.get(i) != currentProcess && !processes.get(i).isComplete()) {
				processes.get(i).setWT(processes.get(i).getWT()+1);
			}
		}
	}
	private static void SRT(ArrayList<Process> processes) {
		// shortest remaining time goes first

		/*
		 * 1- Traverse until all process gets completely executed. a) Find process with
		 * minimum remaining time at every single time lap. b) Reduce its time by 1. c)
		 * Check if its remaining time becomes 0 d) Increment the counter of process
		 * completion. e) Completion time of current process = current_time +1; e)
		 * Calculate waiting time for each completed process. wt[i]= Completion time -
		 * arrival_time-burst_time f)Increment time lap by one. 2- Find turnaround time
		 * (waiting_time+burst_time).
		 */
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		ArrayList<Process> arrived = new ArrayList<Process>();
		int totalTime = 0;
		// the process that is being worked on at any given iteration.
		Process currentProcess = null, oldProcess = null;

		// traverse until all processes are completed executed
		while (!allComplete(processes)) {
			// make sure that only arrived processes are being worked on.
			arrived = checkArrivedProcesses(totalTime, processes);
			// System.out.println(totalTime);
			// sort by remaining time each time
			Collections.sort(arrived, Process.remainingTimeSort);

			oldProcess = currentProcess;
			// get the one with the shortest remaining time
			if (arrived.size() > 0) {
				for(int i = 0; i < arrived.size(); i++) {
					Process p = arrived.get(i);
					if(!p.isComplete()) {
						currentProcess = p;
						break;
					}
				}
			} else {
				totalTime++;
				continue;
			}
			if (oldProcess != currentProcess) {
				pairs.add(new Pair(currentProcess.getPID(), totalTime));
			}
			updateWaitTimes(arrived, currentProcess);
			// System.out.println(currentProcess);
			// decrement the process with the shortest remaining time
			currentProcess.progressProcess();
			// when the process is completed, remove the process from the "arrived" and
			// "processes" list
			if (currentProcess.getRT() == 0) {
				currentProcess.toggleComplete();
				currentProcess.setTAT(currentProcess.getWT()+currentProcess.getBT());
				arrived.remove(currentProcess);
				//processes.remove(currentProcess);
			}
			
			// increment the total time
			totalTime++;
		}
		printGantt(pairs);
	}
	private static void Priority(ArrayList<Process> processes) {
		// processes go based on prioirty

		// Very similar to SRT, but sort by priority instead of remaining time.
		ArrayList<Process> arrived = new ArrayList<Process>();
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		int totalTime = 0;
		Process currentProcess=null, oldProcess=null;

		while (!allComplete(processes)) {
			arrived = checkArrivedProcesses(totalTime, processes);
			System.out.println(totalTime);
			Collections.sort(arrived, Process.prioritySort);
			
			oldProcess = currentProcess;
			// get the one with the shortest remaining time
			if (arrived.size() > 0) {
				for(int i = 0; i < arrived.size(); i++) {
					Process p = arrived.get(i);
					if(!p.isComplete()) {
						currentProcess = p;
						break;
					}
				}
			} else {
				totalTime++;
				continue;
			}
			if (oldProcess != currentProcess) {
				pairs.add(new Pair(currentProcess.getPID(), totalTime));
			}
			updateWaitTimes(arrived, currentProcess);
			//System.out.println(currentProcess);
			currentProcess.progressProcess();
			if (currentProcess.getRT() == 0) {
				currentProcess.toggleComplete();
				currentProcess.setTAT(currentProcess.getWT()+currentProcess.getBT());
				arrived.remove(currentProcess);
			}
			totalTime++;
		}
		printGantt(pairs);
	}
	

	/**
	 * RR with a variable quantum
	 * @param processes
	 */
	private static void RRVariable(ArrayList<Process> processes, int Q) {
		// processes go based on the Quantum Q. Can be either fixed or vaable quantum
	}
	/**
	 * RR with a fixed quantum
	 */
	private static void RRFixed(ArrayList<Process> processes, int Q) {
		//first, sort the processes by arrival time
		Collections.sort(processes, Process.arrivalTimeSort);
		
		int totalTime=0;
		int processCounter=0;
		
		Process currentProcess=null, oldProcess=null;
		ArrayList<Process> arrived=new ArrayList<Process>();
		
		ArrayList<Pair> pairs=new ArrayList<Pair>();
		
		while(!allComplete(processes)) {
			arrived=checkArrivedProcesses(totalTime, processes);
			System.out.println(totalTime);
			if(arrived.size() > 0) {
				currentProcess=arrived.get(processCounter);

				if(totalTime %  Q == 0) {
					System.out.println("Current process: ");
					System.out.println(currentProcess);
					if(processCounter < arrived.size()-1) {
						processCounter++;
					}
					else {
						processCounter=0;
					}
					
				}
				currentProcess.progressProcess();
				if(currentProcess.getRT() == 0) {
					currentProcess.toggleComplete();
					arrived.remove(currentProcess);
				}
				totalTime++;

			}
			else {
				totalTime++;
				continue;
			}

		}
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
