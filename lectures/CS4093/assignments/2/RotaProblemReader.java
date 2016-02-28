import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/*
   numWorkers numDays numSkills minDays maxSeq
   otrate[w0], otrate[w1], ..., otrate[wW]
   skillw[s0][w0], skillw[s0][w1], ..., skillw[s0][wW]
   :
   skillw[sS][w0], skillw[sS][w1], ..., skillw[sS][wW]
   minsd[s0][d0], minsd[s0][d1], ..., minsd[s0][dD]
   :
   minsd[sS][d0], mins[sS][d1], ..., minsd[sS][dD]
 */

public class RotaProblemReader {

	private int numberOfWorkers;   //the number of workers in the problem
	private int numberOfDays;      //the number of days in the problem
	private int numberOfSkills;      //the number of skills in the problem
	private int minDaysWorked;     //the minimum days worked for any worker
	private int maxSequence;       //the maximum number of days any worker can work in a row
	private int[] overtimeRate;   //the daily overtime rate for each worker
	private int[][] skillWorkers; //for each skill, which workers have it
    private int[][] minSkills;    //minimum number of workers with skills reqd each day

	public RotaProblemReader(String filename) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
			numberOfWorkers = scanner.nextInt();
			numberOfDays = scanner.nextInt();
			numberOfSkills = scanner.nextInt();
			minDaysWorked = scanner.nextInt();
			maxSequence = scanner.nextInt();

			overtimeRate = new int[numberOfWorkers];
            for (int worker = 0; worker < numberOfWorkers; worker++) {
            	overtimeRate[worker] = scanner.nextInt();
            }

			skillWorkers = new int[numberOfSkills][numberOfWorkers];
			for (int skill=0;skill<numberOfSkills;skill++) {
				for (int worker=0;worker<numberOfWorkers;worker++){
					skillWorkers[skill][worker] = scanner.nextInt();
				}
			}

			minSkills = new int[numberOfSkills][numberOfDays];
			for (int skill=0;skill<numberOfSkills;skill++){
				for (int day=0;day<numberOfDays;day++) {
					minSkills[skill][day] = scanner.nextInt();
				}
			}
		}
		catch (IOException e) {
			System.out.println("File error:" + e);
		}
	}



	public int getNumberOfWorkers() {
		return numberOfWorkers;
	}



	public int getNumberOfDays() {
		return numberOfDays;
	}

	public int getNumberOfSkills() {
		return numberOfSkills;
	}

	public int getMinDaysWorked() {
		return minDaysWorked;
	}

	public int getMaxSequence() {
		return maxSequence;
	}

	public int[] getOvertimeRate() {
		return overtimeRate;
	}

	public int[][] getSkillWorkers() {
		return skillWorkers;
	}

	public int[][] getMinSkills() {
		return minSkills;
	}

	/*
	public int[] getWelderRate() {
		return welderRate;
	}

	public int getCostBound() {
		return costBound;
	}
    */


	public static void main(String[] args) {
		RotaProblemReader reader = new RotaProblemReader("data\\rota1.txt");
		int numWorkers = reader.getNumberOfWorkers();
		int numDays = reader.getNumberOfDays();
		int numSkills = reader.getNumberOfSkills();
		int minDays = reader.getMinDaysWorked();
		int maxSequence = reader.getMaxSequence();
		int[] overtimeRate = reader.getOvertimeRate();
		int[][] skillWorkersArray = reader.getSkillWorkers();
		int[][] minSkillsArray = reader.getMinSkills();

		System.out.println("Number of workers: " + numWorkers);
		System.out.println("Number of days: " + numDays);
		System.out.println("Number of skills: " + numSkills);
		System.out.println("Minimum days per worker: " + minDays);
		System.out.println("Max days worked in sequnce: " + maxSequence);
		System.out.print("Overtime rate per worker: ");
		for (int worker = 0; worker < numWorkers; worker++) {
			System.out.print(overtimeRate[worker] + " ");
		}
		System.out.println();
		System.out.println("Skill array:");
		for (int skill = 0; skill < numSkills; skill++) {
			for (int worker = 0; worker<numWorkers; worker++) {
		       System.out.print(skillWorkersArray[skill][worker] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Required array:");
		for (int skill = 0; skill < numSkills; skill++) {
			for (int day = 0; day < numDays; day++) {
			   System.out.print(minSkillsArray[skill][day] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
