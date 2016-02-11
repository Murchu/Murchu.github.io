import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;


public class Pin2 {
	
	/* 
	  I find it difficult to remember my PIN, and we are not supposed to write it down on paper.
	  So I have created a constraint problem whose solution is my PIN
	  The four digits a-b-c-d are all different
	  The 2-digit number cd is 3 times the 2-digit number ab
	  The 2-digit number da is 2 times the 2-digit number bc
	
	  If the 4 variables are a, b, c and d, each with domain {0,1,2,...,9}, then the basic problem is:
	     (c*10) + d = 3*((a*10) + b)
	     (d*10) + a = 2*((b*10) + c)
	
	  But Choco doesn't allow us to input arbitrary arithmetic expressions into a constraint.
	  Instead, we have to build it up in pieces.
	  One way is shown below:
	  
	  ab = a*10 + b
	  bc = b*10 + c
	  cd = c*10 + d
	  da = d*10 + a
	  	  
	  cd = 3*ab
	  da = 2*bc
	
	  This requires new variables:
	  ab, cd, da, bc, each of which could be {0,...,99}
	  
	  Choco doesn't allow 3 variables in a simple 'arithm' constraint. 
	  Instead, we can constraint all the entries of an array of vars to be a final var.
	  
	  We can use the 'scale' constraint for the multiplications.
	  
	  This problem and solution were obtained from Patrick Prosser and Charles Prudhomme.
	*/
	
	public static void main(String[] args) {

		//create a solver object that will solve the problem for us
		Solver solver = new Solver();

		//create the variables and domains for the problem, and add to the solver
		IntVar a = VF.enumerated("a",0,9,solver);
		IntVar b = VF.enumerated("b",0,9,solver);
		IntVar c = VF.enumerated("c",0,9,solver);
		IntVar d = VF.enumerated("d",0,9,solver);

		//these 'auxiliary' variables are so we can express the constraints 
		//and we will need extra constraints to force the 2-digit number 'ab' to be b + 10*a, etc
		IntVar ab = VF.enumerated("ab",0,99,solver);
		IntVar bc = VF.enumerated("bc",0,99,solver);
		IntVar cd = VF.enumerated("cd",0,99,solver);
		IntVar da = VF.enumerated("da",0,99,solver);

		//now create and post the constraints

		//use the built-in global constraint to say that they are all different
		solver.post(ICF.alldifferent(new IntVar[]{a,b,c,d}));
				
        //now constrain the values of the 2-digit numbers 
		solver.post(ICF.scalar(new IntVar[]{a,b},new int[]{10,1}, ab));
		solver.post(ICF.scalar(new IntVar[]{c,d},new int[]{10,1}, cd));
		solver.post(ICF.scalar(new IntVar[]{b,c},new int[]{10,1}, bc));
		solver.post(ICF.scalar(new IntVar[]{d,a},new int[]{10,1}, da));

		/*
		 * the scalar constraint takes an array of variables [x,y,z]
		 * and an array of coefficients [a,b,c], and a target variable w,
		 * and posts the constraint x*a + y*b + z*c == w
		 * It represents the dot product of two arrays or vectors.
		 * Other versions allow you to apply other comparison operators instead of '='
		 */
		
		//now apply the constraints between the 2-digit numbers
		solver.post(ICF.arithm(VF.scale(ab,3),"=",cd)); // 3*ab = cd
		solver.post(ICF.arithm(VF.scale(bc,2),"=",da)); // 2*bc = da

		//set a search strategy
        //we will take the default, so do nothing

		//Decide what to display in the output
	    Chatterbox.showSolutions(solver);
	    
	    //solve the problem (and the Chatterbox will display some output)
	    solver.findAllSolutions();
	    
	    //display the search statistics
	    Chatterbox.printStatistics(solver);

	}
}
