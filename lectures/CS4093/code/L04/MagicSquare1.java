import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

public class MagicSquare1 {
	
	public static void main(String[] args) {

		//create a solver object that will solve the problem for us
        Solver solver = new Solver();
        
		//create the variables (with their domains) for the problem, and add to the solver
        IntVar x11 = VF.enumerated("[1,1]", 1, 9, solver);
        IntVar x12 = VF.enumerated("[1,2]", 1, 9, solver);
        IntVar x13 = VF.enumerated("[1,3]", 1, 9, solver);
        IntVar x21 = VF.enumerated("[2,1]", 1, 9, solver);
        IntVar x22 = VF.enumerated("[2,2]", 1, 9, solver);
        IntVar x23 = VF.enumerated("[2,3]", 1, 9, solver);
        IntVar x31 = VF.enumerated("[3,1]", 1, 9, solver);
        IntVar x32 = VF.enumerated("[3,2]", 1, 9, solver);
        IntVar x33 = VF.enumerated("[3,3]", 1, 9, solver);
        
        IntVar M = VF.fixed(15,  solver);
		
		//now create and post the constraints
		solver.post(ICF.sum(new IntVar[]{x11,x12,x13}, M));
		solver.post(ICF.sum(new IntVar[]{x21,x22,x23}, M));
		solver.post(ICF.sum(new IntVar[]{x31,x32,x33}, M));
		solver.post(ICF.sum(new IntVar[]{x11,x21,x31}, M));
		solver.post(ICF.sum(new IntVar[]{x12,x22,x32}, M));
		solver.post(ICF.sum(new IntVar[]{x13,x23,x33}, M));
		solver.post(ICF.sum(new IntVar[]{x11,x22,x33}, M));
		solver.post(ICF.sum(new IntVar[]{x31,x22,x13}, M));
               
		solver.post(ICF.alldifferent(new IntVar[]{x11,x12,x13,x21,x22,x23,x31,x32,x33}));
		
        //use the default search strategy
        //so don't need anything here

        //Decide what to display in the output
	    //Chatterbox.showDecisions(solver);
	    Chatterbox.showSolutions(solver);
	    
	    //solve the problem (and the Chatterbox will display some output)
	    solver.findSolution();
	    //solver.findAllSolutions();
	    
	    //display the search statistics
	    Chatterbox.printStatistics(solver);
	}

}
