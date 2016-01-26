import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class VerySimpleCSP2 {

	public static void main(String[] args) {

		//create a solver object that will solve the problem for us
		Solver solver = new Solver(); 
		
		
		/* A simple problem:
		 *    Variables: {X0,X1,X2}
		 *    Domains: 
		 *       D0: {2,3}
		 *       D1: {1,2}
		 *       D2: {1,2}
		 *    Constraints:
		 *       C1: X0 != X1   (and so C1 subset D0xD1 = {(2,1),(3,1),(3,2)})
		 *       C2: X0 != X2   (and so C2 subset D0xD2 = {(2,1),(3,1),(3,2)})
		 *       C3: X1 != X2   (and so C3 subset D1xD2 = {(1,2),(2,1)})
		 *    
		 */
		
		//create the variables (with their domains) for the problem, and add to the solver
        //create them as an array of variables,  for easier handling	
		//create a parallel array with the variable names
		IntVar[] allVars = VariableFactory.enumeratedArray("allVars", 3, 1, 3, solver);
		String[] names = {"Alice", "Bob", "Carol"};
		
		//and immediately post constraints to restrict the domains
		solver.post(IntConstraintFactory.arithm(allVars[0], "!=", 1));
		solver.post(IntConstraintFactory.arithm(allVars[1], "!=", 3));
		solver.post(IntConstraintFactory.arithm(allVars[2], "!=", 3));
		
		
		//create the constraints
		//use the built-in global constraint to say that they are all different
        solver.post(IntConstraintFactory.alldifferent(allVars));

		
		//tell the solver how to do the search - omit for default search
		solver.set(IntStrategyFactory.minDom_LB(allVars));

	    //use a pretty print object to display the results
	    Chatterbox.showSolutions(solver); //tell it we want to see just the final result
	    //Chatterbox.showDecisions(solver); //tell it we want to see the search tree
	    
	    //ask the solver to find a solution
	    //solver.findSolution();
	    //ask the solver to find all solutions
	    //solver.findAllSolutions();
	    //control the solver iterations manually, and for each one, print our own version
	    if(solver.findSolution()){
	    	
	    	do {
	    	  	System.out.println("New Solution:");
	    		for (int person = 0; person < 3; person++) {
	    			System.out.println("   " + names[person] + ": " + allVars[person].getValue());
	    	   }
	    	} while(solver.nextSolution());
	    }
	    
	    //print out the search statistics
	    //Chatterbox.printStatistics(solver);

	}

}