import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

public class CrystalMaze {
	
	/* A solver for the Crystal maze problem:
	 *    8 variables, with domains 1 to 8
	 *    all different, with no adjacent variables having consecutive values
	 */

	public static void main(String[] args) {

		//create a solver object that will solve the problem for us
        Solver solver = new Solver();
		
		//create the variables (with their domains) for the problem, and add to the solver
		IntVar[] places = VariableFactory.enumeratedArray("places", 8, 1, 8, solver);
		
		//now create and post the constraints
		
		//first state that all places must be different
		//use either the forloop binary constraints, or the alldifferent, but not both
        /* for each pair of different places, state that they must have different values
		*/
		for (int i = 0; i<8; i++) {
			for (int j = i+1; j<8; j++) {
				solver.post(IntConstraintFactory.arithm(places[i], "!=", places[j]));
			}
		}
		/* use the built-in global constraint to say that they are all different
        solver.post(IntConstraintFactory.alldifferent(places));
		*/
        
        //then add the edge constraints
        //Note: distance(X,Y,relation, k) says |X-Y| relation k
        //so distance(X,Y,">" 1) says |X-Y| > 1 - i.e. X and Y cannot have equal or consecutive values
        solver.post(IntConstraintFactory.distance(places[0], places[1], ">", 1));
        solver.post(IntConstraintFactory.distance(places[0], places[2], ">", 1));
        solver.post(IntConstraintFactory.distance(places[0], places[3], ">", 1));
        solver.post(IntConstraintFactory.distance(places[1], places[2], ">", 1));
        solver.post(IntConstraintFactory.distance(places[1], places[4], ">", 1));
        solver.post(IntConstraintFactory.distance(places[1], places[5], ">", 1));
        solver.post(IntConstraintFactory.distance(places[2], places[3], ">", 1));
        solver.post(IntConstraintFactory.distance(places[2], places[4], ">", 1));
        solver.post(IntConstraintFactory.distance(places[2], places[5], ">", 1));
        solver.post(IntConstraintFactory.distance(places[2], places[6], ">", 1));
        solver.post(IntConstraintFactory.distance(places[3], places[5], ">", 1));
        solver.post(IntConstraintFactory.distance(places[3], places[6], ">", 1));
        solver.post(IntConstraintFactory.distance(places[4], places[5], ">", 1));
        solver.post(IntConstraintFactory.distance(places[4], places[7], ">", 1));
        solver.post(IntConstraintFactory.distance(places[5], places[6], ">", 1));
        solver.post(IntConstraintFactory.distance(places[5], places[7], ">", 1));
        solver.post(IntConstraintFactory.distance(places[6], places[7], ">", 1));
        
        //set a search strategy
        //solver.set(IntStrategyFactory.random_value(places));
        //solver.set(IntStrategyFactory.lexico_LB(places));
        //solver.set(IntStrategyFactory.minDom_LB(places));
        solver.set(IntStrategyFactory.domOverWDeg(places, 0, IntStrategyFactory.randomBound_value_selector(0)));
        //solver.set(IntStrategyFactory.impact(places, 0));

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
