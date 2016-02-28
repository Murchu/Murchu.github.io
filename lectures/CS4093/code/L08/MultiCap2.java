import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Task;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.util.ESat;

/*
 * simple multi-capacity scheduling problem, using the cumulative constraint
 * now with a capacity of the resource that varies over time
 */
public class MultiCap2 {

	public static void main(String[] args) {

		/*------PROBLEM SETUP--------*/
		
		//as input, the duration of each task
		int[] lengths = {3,3,2,2,4,1,3,1,1,1,1};
		
		//the resource consumption for each task
		int[] consumption = {1,1,2,3,1,5,2,6,4,1,2};
		
		//get the number of tasks (and quit if the input arrays were not of same length)
		int numberOfTasks = lengths.length;
		if (numberOfTasks != consumption.length) {
			System.exit(1);
		}
		
		//the capacity profile of the resource
		//int[] capacity = {8, 8, 6, 7, 7, 8, 8};
		int[] capacity = {8, 8, 8, 8, 8, 8, 8};
		
		//the length of the capacity profile
		int profileLength = capacity.length;
		
		//the maximum number of time units in which everything must be scheduled
		int deadline = 6;
		
		//check that the deadline is not greater than the profile of the resource
		if (deadline > profileLength) {
			System.exit(2);
		}
		
		//derive the max capacity of the resource
		int maxcap = capacity[0];
		for (int i = 1; i < profileLength; i++) {
			if (capacity[i] > maxcap) {
				maxcap = capacity[i];
			}
		}
		
		//derive the array of capacity reductions
		int[] capReductions = new int[profileLength];
		int drops = 0;
		for (int t = 0; t<profileLength; t++) {
			if (capacity[t] < maxcap) {
				capReductions[t] = maxcap-capacity[t];
				drops++;
			}
			else {
				capReductions[t] = 0;
			}
		}
		
		/*------SOLVER---------------*/
		
		Solver solver = new Solver();
		
		/*------VARIABLES------------*/
		
		//an array of start time variables, one for each task
		IntVar[] start = VF.boundedArray("start", numberOfTasks, 0, deadline-1, solver);

		//an array of end time variables, one for each task
		IntVar[] end = VF.boundedArray("end", numberOfTasks, 1, deadline, solver); //a task will be active from start up to end-1
		
		//create an array of dummy tasks
		Task[] dummy = new Task[drops];
		int[] dummyHeight = new int[drops];
		int time = 0;
		for (int i = 0; i<drops; i++) {
			while (capReductions[time] == 0) {
				time++;
			}
			dummy[i] = new Task(VF.fixed(time, solver ), VF.fixed(1, solver), VF.fixed(time+1, solver));
			dummyHeight[i] = capReductions[time];
			System.out.println("Created dummy with start: " + time + "; height: " + capReductions[time]);
			time++;
		}
		
		//an array of Task objects for the cumulative constraint, plus the drops dummy tasks
		//Each task has a start, a duration and an end, all of them IntVars
		//the durations are fixed as input, so use VF.fixed
		Task[] tasks = new Task[numberOfTasks + drops];
		for (int task = 0; task < numberOfTasks; task++) {
		    tasks[task] = new Task(start[task], VF.fixed(lengths[task], solver), end[task]);   	
		}
		int d = 0;
		for (int task = numberOfTasks; task < numberOfTasks + drops; task++ ) {
           tasks[task] = dummy[d++];
		}
		
		//an array of heights for cumulative (i.e. the consumption of each task)
		IntVar[] height = new IntVar[numberOfTasks + drops];

		/*------CONSTRAINTS----------*/

		//the end time for each task is the start time plus the duration
		//Using arithm, we can write this as end-start = (fixed) length
		for (int task = 0; task < numberOfTasks; task++) {
			solver.post(ICF.arithm(end[task], "-", start[task], "=", lengths[task]));
		}
		
		//create a (fixed) IntVar for each height for the cumulative constraint
		for (int task=0; task < numberOfTasks; task++) {
			height[task] = VF.fixed(consumption[task],  solver);
		}
		int d2 = 0;
		for (int task = numberOfTasks; task < numberOfTasks + drops; task++ ) {
           height[task] = VF.fixed(dummyHeight[d2++],solver);
		}
		
		//post the cumulative constraint saying all must be slotted into the schedule, respecting the capacity
		solver.post(ICF.cumulative(tasks, height, VF.fixed(maxcap, solver)));
		
		//post some other constraints to test the model
		
		/*
		solver.post(ICF.arithm(end[0],  "<=", start[1]));
		solver.post(ICF.arithm(end[6], "<=", start[10]));
		solver.post(LCF.or(ICF.arithm(end[6],  "<=", start[2]),
			               ICF.arithm(end[2], "<=", start[6])));
		solver.post(ICF.arithm(end[3], "<=", start[6]));
		*/
		
		/*------SEARCH STRATEGY-------*/
		
		/*------SOLUTION-------------*/
		
		Chatterbox.showSolutions(solver);
		solver.findSolution();
		Chatterbox.printStatistics(solver);
		
		//if a solution is found, display it
		if (solver.isSatisfied() == ESat.TRUE) {
			System.out.print("   ");
			for (int t = 0; t < deadline; t++) {
				System.out.print(t + " ");
			}
			System.out.println();
			for (int task = 0; task < numberOfTasks; task++) {
				System.out.print(task + ": ");
				int startTime = start[task].getValue();
				for (int t = 0; t < startTime; t++) {
					System.out.print("0 ");
				}
				for (int t=0; t<lengths[task]; t++ ) {
					System.out.print(consumption[task] + " ");
				}
				for (int t = end[task].getValue(); t<deadline; t++) {
					System.out.print("0 ");
				}
				System.out.println();
			}
		}
	}

}
