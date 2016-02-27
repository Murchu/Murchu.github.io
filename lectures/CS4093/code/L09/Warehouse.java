import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

/*
 * A class for solving a warehouse location problem
 * Data values are hard coded into the class
 */

public class Warehouse {

	public static void main(String[] args) {
		
        /*--------------- PROBLEM PARAMETERS ----------------*/
		/**/
        int numWHs = 5;           //number of warehouses
        int numShops = 10;        //number of shops
        int maintenance = 30;     //common maintenance cost per warehouse
        
        int[] capacity = {1,4,2,1,3}; //capacity of each warehouse
        
        int[][] supply = {          //supply[i][j] is the cost of supplying shop i from warehouse j
            {20, 24, 11, 25, 30},
            {28, 27, 82, 83, 74},
            {74, 97, 71, 96, 70},
            {2, 55, 73, 69, 61},
            {46, 96, 59, 83, 4},
            {42, 22, 29, 67, 59},
            {1, 5, 73, 59, 56},
            {10, 73, 13, 43, 96},
            {93, 35, 63, 85, 46},
            {47, 65, 55, 71, 95}};
        /**/
		/* SIMPLE TEST CASE 
		int numWHs = 3;
		int numShops = 4;
		int maintenance = 5;
		
		int[] capacity = {2,4,3};
		
		int[][] supply = {
				{4,6,8},
				{7,7,5},
				{4,5,6},
				{9,6,5}
		};
        */
        
		int maxShopCost = 0;    //the maximum supply cost for any shop
        for (int shop = 0; shop < numShops; shop++) {
        	for (int wh = 0; wh < numWHs; wh++) {
        		if (supply[shop][wh] > maxShopCost) {
        			maxShopCost = supply[shop][wh];
        		}
        	}
        }
        
        int maxCapacity = 0; //the maximum capacity for any warehouse
        for (int wh = 0; wh < numWHs; wh++) {
        	if (capacity[wh] > maxCapacity) {
        		maxCapacity = capacity[wh];
        	}
        }
        
        //display the computed problem parameters
        System.out.println("maxShopCost = " + maxShopCost);
        System.out.println("maxCapacity = " + maxCapacity);

		/* ------------- SOLVER -------------- */
		
		Solver solver = new Solver();
		
		/* ------------- VARIABLES ----------- */
				
		//an array of IntVars, stating for each shop which warehouse supplies it
		IntVar[] shopSupply = VF.boundedArray("shopSupply", numShops, 0, numWHs-1, solver);

		//an array of IntVars, stating the cost of supplying to each shop from its chosen warehouse
		IntVar[] shopCost = VF.boundedArray("shopCost",  numShops,  0,  maxShopCost, solver);
        
		//the cost of supplying all the shops
		IntVar allShopCost = VF.bounded("allShopCost",  0,  maxShopCost*numShops,  solver);
		
		//an array of booleans, stating for each warehouse whether or not it is active
        IntVar[] whActive = VF.enumeratedArray("whActive", numWHs, 0, 1, solver);
        
        //the number of active warehouses
        IntVar numberActive = VF.bounded("numberActive",  0,  numWHs, solver);
        
        //an array of Intvars, stating for each warehouse how many shops it supplies
        IntVar[] supplyCount = new IntVar[numWHs];
        for (int wh = 0; wh < numWHs; wh++) {
        	supplyCount[wh] = VF.bounded("supplyCount",  0,  capacity[wh], solver);
        }

        //the total cost of the chosen warehouses and warehouse,shop pairs
        IntVar totalCost = VF.enumerated("totalCost", 0, maxShopCost*numShops + numWHs*maintenance, solver);

        /* ------------- CONSTRAINTS ---------- */

        //If a shop is supplied by a warehouse, then it is active
        //for a given shop s, if shopSupply[s] == w, then warehouse w is active, and so whActive[w] == 1
        //i.e. whActive[shopSupply[s]] == 1
        for (int shop = 0; shop < numShops; shop++) {
        	solver.post(ICF.element(VF.fixed(1,solver), whActive, shopSupply[shop], 0));
        }
        		
        //no warehouse can supply more than its capacity of shops
        for (int wh = 0; wh < numWHs; wh++) {
        	solver.post(ICF.count(wh, shopSupply, supplyCount[wh]));
        }

        //the cost of a shop is the supply cost from its chosen warehouse
        //supply[shop] is the array of costs per warehouse to supply shop
        //so supply[shop][wh] is the cost of supplying shop from warehouse wh
        //shopSupply[shop] is the warehouse chosen to supply shop
        //so supply[shop][shopSupply[shop]] is the cost of supplying shop by its chosen warehouse
        for (int shop = 0; shop < numShops; shop++) {
        	solver.post(ICF.element(shopCost[shop], supply[shop], shopSupply[shop]));
        }

        //the cost of supply is the sum of the shop supply costs
        solver.post(ICF.sum(shopCost, allShopCost));

        //the maintenance cost for the warehouses is the number of active warehouses
        solver.post(ICF.count(VF.fixed(1,solver), whActive, numberActive));
        //solver.post(ICF.sum(whActive, numberActive));
        
        //the total cost is the sum of all shop costs plus the maintenance cost
        solver.post(ICF.scalar(new IntVar[]{numberActive, allShopCost}, new int[]{maintenance,1}, totalCost));
        

        /* ------------ SOLVE ------------------- */
        
        //set a search strategy
        //solver.set(IntStrategyFactory.minDom_LB(shopSupply));
        //solver.set(IntStrategyFactory.domOverWDeg(shopSupply, 0));
        solver.set(IntStrategyFactory.impact(shopSupply, 0)); //appears to be best on the 5wh problem
        
        //Chatterbox.showDecisions(solver);
        Chatterbox.showSolutions(solver);
        //solver.findSolution();
		solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, totalCost);
        Chatterbox.printStatistics(solver);
        
        //print out our own solution
        
        System.out.print("Active warehouses: ");
        for (int wh=0; wh < numWHs; wh++) {
        	if (whActive[wh].getValue() == 1)
        	   System.out.print(wh + " ");
        }
        System.out.println(" (Maintenance cost = " + maintenance*numberActive.getValue() + ")");
        for (int shop = 0; shop < numShops; shop++) {
        	System.out.println("Shop: " + shop + ": w " + shopSupply[shop].getValue() + " (cost " + shopCost[shop].getValue() + ")");
        }
        System.out.println("Total cost: " + totalCost.getValue());
        
	}

}
