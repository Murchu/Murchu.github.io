import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

/*
 * A class for solving a basic 1D bin packing decision problem
 * Data values are hard coded into the class
 * Uses the global bin packing constraint
 */

public class BP2 {

	public static void main(String[] args) {

		/*
		//the sizes of the individual objects
		int[] sizes = {4,7,2,9,5,8,4};
		int nObjects = sizes.length;
		int nBins = 3;
		int binSize = 15; 
        */
		
		//the sizes of the individual objects
		int[] sizes = {42, 63, 67, 57, 93, 90, 38, 36, 45, 42};
		int nObjects = sizes.length;  //the number of objects
		int nBins = 5;                //the number of bins
		int binSize = 150;            //the (uniform) bin capacity
		
		//create the solver
		Solver solver = new Solver();
		
		//VARIABLES
		
		//an array of intVars, for the total size added to each bin
		IntVar[] binLoad = VF.enumeratedArray("loads", nBins, 0, binSize, solver);

		//an array of intVars, stating which bin each object has been added to
		IntVar[] binForObject = VF.enumeratedArray("binForObject",  nObjects,  0,  nBins-1, solver);
		
		//CONSTRAINTS
        
		//a global bin packing constraint
        solver.post(ICF.bin_packing(binForObject, sizes, binLoad,0));

        //SEARCH
        
        Chatterbox.showSolutions(solver);
        solver.findSolution();
        Chatterbox.printStatistics(solver);
        
        //print out our own solution
        //Bin i: obj j (size of j)* [bin load]
        //*
        for (int bin = 0; bin < nBins; bin++) {
        	System.out.print("Bin " + bin + ": ");
        	for (int object = 0; object < nObjects; object++) {
        		if (binForObject[object].getValue() == bin) {
        			System.out.print(object + "(" + sizes[object] + ") ");   //print the details
        		}
        	}
        	System.out.println("[" + binLoad[bin].getValue() + "]");    //print the bin load
        }
	}

}
