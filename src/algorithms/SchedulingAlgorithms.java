package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class Process {
	public final int TIME_UNIT=1;
	public final int MAX_TIME=100;
	private int pid, bt, at, p, rt;
	private static int processCount = 0;

	private boolean hasArrived;
	public Process(int bt, int at, int p) {
		this.pid = ++processCount;
		this.bt = bt;
		this.at = at;
		this.p = p;
		
		this.hasArrived=false;
    
		this.rt=bt;
	}

	public Process(int bt, int at) {
		this(bt, at, -1);
	}

	public Process(int bt) {
		this(bt, 0, -1);
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

	public int getRT(){
  	return this.rt;
	}
	public boolean hasArrived() {
		return this.hasArrived;
	}
	public void toggleArrival() {
		this.hasArrived=!this.hasArrived;
	}
  
  public void progressProcess(){
    this.rt--; 
  }
	public static Comparator<Process> arrivalTimeSort = new Comparator<Process>() {
		
		public int compare(Process one, Process two) {
			int arr1 = one.getAT();
			int arr2 = two.getAT();
			return arr1-arr2;
		}
	};
}

public class SchedulingAlgorithms {

	/*
	 * Creates a class to represent processes that have Burst Time (BT), Arrival
	 * time (AT), and Priority (P)
	 */

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		ArrayList<Process> processes = new ArrayList<Process>();
		System.out.println("Enter each processes' burst time, separated by a space: ");

		String[] burstTimes = scan.nextLine().split("\\s");
		for (int i = 0; i < burstTimes.length; i++) {
			processes.add(new Process(Integer.parseInt(burstTimes[i])));
		}

		System.out.println("Will you be entering arrival times? 'Y' if yes, 'N' if no.");

		if (scan.nextLine().equals("Y")) {
			System.out.println("Enter each processes' arrival time, separated by a space: ");
			String[] arrivalTimes = scan.nextLine().split("\\s");
			if (arrivalTimes.length != burstTimes.length) {
				System.out.println("Each process must have an arrival time.");
				System.exit(0);
			}
			for (int i = 0; i < burstTimes.length; i++) {
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
	}

	private static void FCFS(ArrayList<Process> processes) {
		// sort processes from lowest arrivalTime -> Greatest. In the event of a tie,
		// then lowest pid goes first
    System.out.println("Process arrival times before sort: ");
    for(int i = 0; i < processes.size(); i++){
      System.out.print(processes.get(i).getAT()+" ");
    }
		Collections.sort(processes, Process.arrivalTimeSort);
    System.out.println("Process arrival times after sort: ");
    for(int i = 0; i < processes.size(); i++){
      System.out.print(processes.get(i).getAT()+" ");
    }
    
	}

	private static void SJF(ArrayList<Process> processes) {
		//lowest arrival time goes first
		//after that process finishes, find next process that has entered system (arrival time), and has the lowest burstTime
	}

	private static void SRT(ArrayList<Process> processes) {
		//shortest remaining time goes first

	}

	private static void Priority(ArrayList<Process> processes) {
		//processes go based on prioirty
	}

	private static void RR(ArrayList<Process> processes) {
		//processes go based on the Quantum Q. Can be either fixed or vairable quantum
	}

}
