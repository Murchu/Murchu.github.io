import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class VerySimpleCSP {

	public static void main(String[] args) {

		//create a solver object that will solve the problem for us
		Solver solver = new Solver();  
		
		/* A simple problem:
		 *    Variables: {AliceTime,BobTime,CarolTime}
		 *    Domains: 
		 *       D0(Alice): {2,3}
		 *       D1(Bob): {1,2}
		 *       D2(Carol): {1,2}
		 *    Constraints:
		 *       C1: AliceTime != BobTime   (and so C1 subset D0xD1 = {(2,1),(3,1),(3,2)})
		 *       C2: AliceTime != CarolTime   (and so C2 subset D0xD2 = {(2,1),(3,1),(3,2)})
		 *       C3: BobTime != CarolTime   (and so C3 subset D1xD2 = {(1,2),(2,1)})
		 *    
		 */
		
		//create the variables (with their domains) for the problem, and add to the solver
		IntVar AliceTime = VariableFactory.enumerated("Alice time", 2, 3, solver);
		IntVar BobTime = VariableFactory.enumerated("Bob time", 1, 2, solver);
		IntVar CarolTime = VariableFactory.enumerated("Carol time", 1, 2, solver);				
		
		//create the constraints
		solver.post(IntConstraintFactory.arithm(AliceTime, "!=", BobTime));
		solver.post(IntConstraintFactory.arithm(AliceTime, "!=", CarolTime));
		solver.post(IntConstraintFactory.arithm(BobTime, "!=", CarolTime));
		
	    //use a pretty print object to display the results
	    Chatterbox.showSolutions(solver); //tell it we want to see just the final result
	    Chatterbox.showDecisions(solver); //tell it we want to see the search tree
	    
	    //ask the solver to find a solution
	    solver.findSolution();
	    
	    //print out the search statistics
	    Chatterbox.printStatistics(solver);

	}

}