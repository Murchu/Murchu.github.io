import java.io.IOException;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

/*
 *  Simple truck loading problem (or bounded knapsack)
 *  Given an inventory of items, where each item has a weight and a profit,
 *  find a selection of the items to load into a truck, where there is a limit
 *  on the total weight carried by the truck, and
 *  where the maximum possible profit is achieved.
 *  
 *  Data is read in from file, where the file must contain
 *  size capacity 
 *  weight1 profit1 stock1
 *  weight2 profit2 stock2
 *  etc 
 */

public class Truckload2 {

	public static void main(String[] args) throws IOException {
		Solver solver = new Solver();

		MyData data = new MyData("data\\data3.txt");
		
		int n = data.getSize(); //the number of item types
		int capacity = data.getCapacity();
		int[] weights = data.getWeights(); //the weights of a group of item types 
		int[] profits = data.getProfits(); //the profit obtained from each type - must be same length as weights
		int[] stock =   data.getStock(); //the maximum number of each item 		
        int maxprofit = data.getMaxProfit();
        int maxweight = data.getMaxWeight();
        int maxstock = data.getMaxStock();
        
	    System.out.println("Size: " + n + "; capacity: " + capacity);
	    System.out.println("Max profit: " + maxprofit + "; maxweight: " + maxweight + "; max stock: " + maxstock);

        
        //VARIABLES
		
	    //rather than use a built-in method to create an array of variables, I am creating each one individually,
	    //since each one will have a different domain
		IntVar[] load = new IntVar[n];
		for (int i = 0; i<n; i++) {
			load[i] = VariableFactory.bounded("load"+i, 0,  stock[i], solver); //how many of each type
		}
		IntVar totalWgt = VariableFactory.bounded("total wgt", 0,  capacity, solver);    //the total weight of selected items
		IntVar totalProfit = VariableFactory.bounded("profit", 0, maxprofit*n*maxstock, solver);     //the total profit of selected items
		
		//CONSTRAINTS
		
		//compute the total weight of the chosen items
		//for each type, multiply the number of items by the unit weight, and sum the total to get total weight
		//i.e. compute the scalar product of the load and weights arrays
		solver.post(IntConstraintFactory.scalar(load, weights, totalWgt));
		
		//constraint the total weight to be less than the capacity
		solver.post(IntConstraintFactory.arithm(totalWgt, "<=", capacity));
		
		//compute the total profit of the chosen items
		//i.e. compute the scalar product of load and profits arrays
		solver.post(IntConstraintFactory.scalar(load, profits,  totalProfit));
		
		//constraint the total profit to be above the cost
		//solver.post(IntConstraintFactory.arithm(totalProfit, ">", cost));
		

		
		//SEARCH STRATEGY
        //solver.set(IntStrategyFactory.random_value(load));
        //solver.set(IntStrategyFactory.lexico_LB(load));
        //solver.set(IntStrategyFactory.minDom_LB(load));
        solver.set(IntStrategyFactory.domOverWDeg(load, 0));
        //solver.set(IntStrategyFactory.impact(load, 0));
        //solver.set(IntStrategyFactory.impact(load, 2,3,10, 0, false));
		
		//SOLVE
		
		//next line would show each intermediate solution
		//Chatterbox.showDecisions(solver);
		//solver.findSolution();
		solver.findOptimalSolution(ResolutionPolicy.MAXIMIZE, totalProfit);

		//next code block interrogates the variables and gets the solution for ourselves
		for (int i = 0; i<n; i++) {
			System.out.print("load[" + i + "]=" + load[i].getValue() + "; ");
		}
		System.out.println();
		System.out.println("totalWgt=" + totalWgt.getValue());
		System.out.println("totalProfit=" + totalProfit.getValue());
		Chatterbox.printStatistics(solver);;
		
	}

}
