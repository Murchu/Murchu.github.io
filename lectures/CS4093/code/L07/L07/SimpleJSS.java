import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Task;
import org.chocosolver.solver.variables.VF;


/*
 * A simple solver for a job shop scheduling problem
 */
public class SimpleJSS {

	public static void main(String[] args) {
		
		//read the problem in from file in standard format
		JSSPReader reader = new JSSPReader("data\\jssp2.txt");
		
		int numJobs = reader.getNumJobs();        //number of jobs
		int numRes = reader.getNumResources();    //number of resources
		int[][][] problem = reader.getProblem();  //3D array of jobs, tasks, {resource, duration}
		int sumDurations = reader.getSumDurations();  //the makespan if we just sequenced all the tasks in one line
		
		//a maximum value for the makespan
		//int deadline = 20;
		int deadline = sumDurations;
		
		//an array for recording the next index for each restask array
		int resIndices[] = new int[numRes];
		for (int i =0; i<numRes; i++) {
			resIndices[i] = 0;
		}
		
		//SOLVER
		Solver solver = new Solver();
		
		//VARIABLES
		
        //2D array of task resource consumption for cumulative - every entry will be "1" for JSSP
		IntVar[][] heights = new IntVar[numRes][numJobs];
		//array of capacities for the resources for cumulative - every entry will be "1" for JSSP
		IntVar[] capacity = new IntVar[numRes];
		//array of all task start times
		IntVar[] allTaskStarts = new IntVar[numJobs*numRes];
		//a 2d array for the Task variables [job][task]
		Task[][] jobtask = new Task[numJobs][numRes];
		//a 2d array for the tasks assigned to each machine [res][job]
		Task[][] restask = new Task[numRes][numJobs];
		
		//for each job, for each task, create a Task variable, and add it to the jobtask and restask arrays
		int resource = 0;
		int tasks = 0;
		for (int j = 0; j<numJobs; j++) {
			System.out.println("Job " + j);
			for (int t = 0; t < numRes; t++) {
				System.out.print("task " + t + " ");
				resource = problem[j][t][0]; //which resource is this job,task using
				System.out.println("is on resource " + resource +"; duration " + problem[j][t][1]);
				//create the Task object containing the start, duration and end variables
				jobtask[j][t] = VF.task(VF.bounded("j" + j + "t" + t + "s", 0, sumDurations, solver),
						VF.fixed("j" + j + "t" + t + "d", problem[j][t][1],solver),
						VF.bounded("j" + j + "t" + t + "e", 0, sumDurations, solver));
				//now create a reference to this task and put in the array of tasks per resource
				restask[resource][resIndices[resource]] = jobtask[j][t];
				//prepare the parameters for the cumulative constraint
				heights[resource][resIndices[resource]] = VF.fixed(1, solver);
                capacity[resource] = VF.fixed(1, solver);
				resIndices[resource]++;
				//finally add the task.start variable to the allTaskStarts array
				allTaskStarts[tasks++] = jobtask[j][t].getStart();
			}
		}

		//the finish time of the last task - the usual objective in JSSP
		IntVar makespan = VF.bounded("makespan",  0,  sumDurations, solver);

		//CONSTRAINTS
		
		//for each resource, create a cumulative constraint, for the relevant tasks, all with height 1, for capacity 1
		/*
		for (int res = 0; res < numRes; res++) {
			solver.post(ICF.cumulative(restask[res], heights[res], capacity[res]));
		}
		*/
		
		/**/
		 
		//for each resource, create "no overlap" constraints between the tasks
		//these constraints are not needed if we are using cumulative
		for (int res = 0; res < numRes; res++) {
			for (int task1 = 0; task1 < numJobs-1; task1++) {
				for (int task2 = task1+1; task2 < numJobs; task2++) {
			        solver.post(LCF.or(ICF.arithm(restask[res][task1].getEnd(), "<=", restask[res][task2].getStart()),
			        		           ICF.arithm(restask[res][task2].getEnd(), "<=", restask[res][task1].getStart())));	
				}
			}
		}
		
		/**/
		
		//for each job, create precedence constraints between its tasks
		for (int j = 0; j < numJobs; j++) {
			for (int before = 0; before < numRes-1; before++) {
					solver.post(ICF.arithm(jobtask[j][before].getEnd(), 
							               "<=", 
							               jobtask[j][before+1].getStart()));
			}
			//and post that the last task finishes before or on the makespan
			solver.post(ICF.arithm(jobtask[j][numRes-1].getEnd(), "<=", makespan));
		}
		
		//for decision problems, constrain the makespan to be less than a threshold
		solver.post(ICF.arithm(makespan, "<=", deadline));
				
		//Search Strategy
        //solver.set(IntStrategyFactory.minDom_LB(allTaskStarts));
        //solver.set(IntStrategyFactory.domOverWDeg(allTaskStarts, 0));
        //solver.set(IntStrategyFactory.impact(allTaskStarts, 0));

		//SOLVE
		
		Chatterbox.showSolutions(solver);
		//solver.findSolution();
		solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, makespan);
        Chatterbox.printStatistics(solver);		
	}
 
}
