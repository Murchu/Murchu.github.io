import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

/*
 * A class for solving a basic 1D bin packing problem, minimising the number of bins used
 * Data values are hard coded into the class
 * Uses the global bin packing constraint
 * Achieves If-and-only-if by forcing two reified constraints to be equal
 */

public class BP2Optimiser {

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
        
        //an array of 0/1 IntVars, to record whether a bin was used
        IntVar[] binUsed = VF.enumeratedArray("binUsed", nBins, 0, 1, solver);

        //a count of how many bins were used
        IntVar numberBinsUsed = VF.enumerated("numberBinsUsed", 0, nBins, solver);

        //CONSTRAINTS

		//a global bin packing constraint
        solver.post(ICF.bin_packing(binForObject, sizes, binLoad,0));


        //for each bin, reify a constraint to say it is used, and reify a constraint to say load > 0,
        //and then constraint them to be equal (i.e. used <==> load > 0)
        BoolVar[] binUsedReif = VF.boolArray("binUsedReif", nBins, solver);
        BoolVar[] binLoadReif = VF.boolArray("binLoadReif", nBins, solver);
        for (int bin=0; bin<nBins; bin++) {
        	LCF.reification(binUsedReif[bin],ICF.arithm(binUsed[bin], ">", 0));
        	LCF.reification(binLoadReif[bin],ICF.arithm(binLoad[bin], ">", 0));
        	solver.post(ICF.arithm(binUsedReif[bin], "=", binLoadReif[bin]));
        }
        
        //summing the individual bin used 0/1 vars gives the number of bins used
        solver.post(ICF.sum(binUsed,numberBinsUsed));

        //SOLVE
        
        Chatterbox.showSolutions(solver);
        //solver.findSolution();
		solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, numberBinsUsed);
        Chatterbox.printStatistics(solver);
        
        //print out our own solution
        //Bin i: [used?] obj j (size of j)* [bin load]
        //*        
        for (int bin = 0; bin < nBins; bin++) {
        	System.out.print("Bin " + bin + ": [" + binUsed[bin].getValue() + "] ");
        	for (int object = 0; object < nObjects; object++) {
        		if (binForObject[object].getValue() == bin) {
        			System.out.print(object + "(" + sizes[object] + ") ");
        		}
        	}
        	System.out.println("[" + binLoad[bin].getValue() + "]");
        }
        
	}

}
