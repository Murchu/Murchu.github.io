import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;


public class Pin1 {
	
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
	  
	  a10 = a*10
	  b10 = b*10
	  c10 = c*10
	  d10 = d*10
	  
	  ab = a10 + b
	  cd = c10 + d
	  da = d10 + a
	  bc = b10 + c
	  
	  cd = 3*ab
	  da = 2*bc
	
	  This requires new variables:
	  a10, b10, c10, d10 (but they should have values -determined- by a,b,c,d)
	  ab, cd, da, bc, each of which could be {0,...,99}
	  
	  Choco doesn't allow 3 variables in a simple 'arithm' constraint. 
	  Instead, we can constraint all the entries of an array of vars to be a final var.
	  
	  We can use the 'scale' constraint for the multiplications.
	  
	  This problem and solution were obtained from Patrick Prosser.
	*/
	
	public static void main(String[] args) {

		//create a solver object that will solve the problem for us
		Solver solver = new Solver();

		//create the variables and domains for the problem, and add to the solver
		IntVar a = VF.enumerated("a",0,9,solver);
		IntVar b = VF.enumerated("b",0,9,solver);
		IntVar c = VF.enumerated("c",0,9,solver);
		IntVar d = VF.enumerated("d",0,9,solver);
		
		/*
		 * VF is a shorthand notation for the VariableFactory class
		 * ICF is shorthand for IntegerConstraintFactory, and so on
		 */

		//next 8 variables are 'auxiliary' variables - we use them to help model the problem
		//the x10 variables will be tightly constrained to be exactly equal to x*10
		IntVar a10 = VF.scale(a,10); 
		IntVar b10 = VF.scale(b,10); 
		IntVar c10 = VF.scale(c,10);  
		IntVar d10 = VF.scale(d,10); 
		
		/*
		 * The scale constraint multiplies a variable by an integer, and returns a new variable
		 * So scale(x,a) returns a variable constrained to be equal to x*a
		 */

		//these variables are so we can express the constraints 
		//and we will need extra constraints to force the 2-digit number 'ab' to be b + 10*a, etc
		IntVar ab = VF.enumerated("ab",0,99,solver);
		IntVar bc = VF.enumerated("bc",0,99,solver);
		IntVar cd = VF.enumerated("cd",0,99,solver);
		IntVar da = VF.enumerated("da",0,99,solver);

		//now create and post the constraints

		//use the built-in global constraint to say that they are all different
		solver.post(ICF.alldifferent(new IntVar[]{a,b,c,d}));
				
        //now constrain the values of the 2-digit numbers 
		solver.post(ICF.sum(new IntVar[]{a10,b},ab)); // ab = b + 10*a
		solver.post(ICF.sum(new IntVar[]{b10,c},bc)); // bc = c + 10*d
		solver.post(ICF.sum(new IntVar[]{c10,d},cd)); // cd = d + 10*c
		solver.post(ICF.sum(new IntVar[]{d10,a},da)); // da = a + 10*d
		
		/* Note:
		 * (new IntVar[]{a10,b},      is standard Java creating and initialising an anonymous array
		 *                            of size 2, with elements a10 and b. It is the same result as
		 * IntVar[] abArray = new IntVar[2];
		 * abArray[0] = a10;
		 * abArray[1] = b;
		 * 
		 * and then passing abArray as the first argument to the sum constraint generator:
		 * solver.post(ICF.sum(abArray,ab)); // ab = b + 10*a
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
