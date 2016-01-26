import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class FirstExercise {
    public static void main(String[] args) {
	//create a solver object that will solve the problem for us
	Solver solver = new Solver();  
		
	//create the variables and domains for the problem, and add to the solver
	IntVar V1 = VariableFactory.enumerated("V1", 1, 5, solver);
	IntVar V2 = VariableFactory.enumerated("V2", 1, 5, solver);
	IntVar V3 = VariableFactory.enumerated("V3", 1, 5, solver);
	IntVar V4 = VariableFactory.enumerated("V4", 1, 5, solver);
		
	//create the constraints
	solver.post(IntConstraintFactory.arithm(V1, "-", V4, "<=", -1));
	solver.post(IntConstraintFactory.arithm(V1, "<", V2));
	solver.post(IntConstraintFactory.arithm(V2, "+", V3, ">", 6));
	solver.post(IntConstraintFactory.arithm(V2, "+", V4, "=", 5));
	solver.post(IntConstraintFactory.arithm(V4, "<", V3));
		
	Chatterbox.showSolutions(solver); //just show the final result
	    
	//ask the solver to find a solution
	//solver.findSolution();
	solver.findAllSolutions();
	    
	Chatterbox.printStatistics(solver);
    }
}
