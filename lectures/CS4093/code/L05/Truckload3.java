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
 *  uses the global constraint "knapsack"
 *  
 *  Data is read in from file, where the file must contain
 *  size capacity 
 *  weight1 profit1 stock1
 *  weight2 profit2 stock2
 *  etc 
 */

public class Truckload3 {

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
		
		IntVar[] load = new IntVar[n];
		for (int i = 0; i<n; i++) {
			load[i] = VariableFactory.enumerated("load"+i, 0,  stock[i], solver); //how many of each type
		}
		//IntVar capacityVar = VariableFactory.bounded("total wgt", 0,  capacity, solver);    //the total weight of selected items
		IntVar capacityVar = VariableFactory.fixed("total wgt", capacity, solver);    //the total weight of selected items
		IntVar profitVar = VariableFactory.bounded("profit", 0, maxprofit*n*maxstock, solver);     //the total profit of selected items
		//IntVar profitVar = VariableFactory.enumerated("profit", 0, maxprofit*n*maxstock, solver);     //the total profit of selected items
		
		//CONSTRAINTS
        solver.post(IntConstraintFactory.knapsack(load, capacityVar, profitVar, weights, profits));

		
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
		solver.findOptimalSolution(ResolutionPolicy.MAXIMIZE, profitVar);

        //next code block interrogates the variables and gets the solutions for ourselves
		int totalWgt = 0;
		for (int i = 0; i<n; i++) {
			System.out.print("load[" + i + "]=" + load[i].getValue() + "; ");
			totalWgt += load[i].getValue()*weights[i];
		}
		System.out.println();
		System.out.println("totalWgt=" + totalWgt);
		System.out.println("totalProfit=" + profitVar.getValue());
		Chatterbox.printStatistics(solver);;

	}

}
