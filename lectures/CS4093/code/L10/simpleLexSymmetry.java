import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.util.ESat;


public class simpleLexSymmetry {

	public static void main(String[] args) {
		
		Solver solver = new Solver();
		
		/*----------VARIABLES--------------*/
		
		//the matrix of 0/1 variables
		IntVar[][] matrix = VF.enumeratedMatrix("matrix",  4, 3, 0, 1,solver);
		
		//the transpose of the matrix
		IntVar[][] matrixT = new IntVar[3][4];
		for (int i = 0; i<4; i++) {
			for (int j = 0; j<3; j++) {
				matrixT[j][i] = matrix[i][j];
			}
		}
		
		//and the flattened matrix
		IntVar[] flattened = new IntVar[3*4];
		for (int i = 0; i<4; i++) {
			for (int j = 0; j<3; j++) {
				flattened[i*3 + j] = matrix[i][j];
			}
		}
		
		//for the scalar products of pairs of rows
		//a matrix, where row i*(4-1)+j is the sequence of products for rows i and j, j>i
		
		
		/*---------CONSTRAINTS-------------*/
		
		solver.post(ICF.sum(flattened, VF.fixed(7,solver)));
		
        IntVar scalarprod = VF.fixed(1, solver);
        for (int i1 = 0; i1 < 4; i1++) {
            for (int i2 = i1 + 1; i2 < 4; i2++) {
                BoolVar[] product = VF.boolArray("products", 3, solver);
                for (int j = 0; j < 3; j++) {
                    solver.post(ICF.times(matrix[i1][j], matrix[i2][j], product[j]));
                }
                solver.post(IntConstraintFactory.sum(product, "<=", scalarprod));
            }
        }
        
        //solver.post(IntConstraintFactory.lex_chain_less_eq(matrix));
        //solver.post(IntConstraintFactory.lex_chain_less_eq(matrixT));

        /*-------------SEARCH------------------*/
        
        Chatterbox.showSolutions(solver);
        solver.findSolution();
        
        
        while (solver.isSatisfied() == ESat.TRUE) {
            System.out.println();
            for (int i = 0; i<4; i++) {
            	for (int j = 0; j<3; j++) {
            		System.out.print( matrix[i][j].getValue() + " ");
            	}
            	System.out.println();
            }
            System.out.println();

            solver.nextSolution();
        }
        
        Chatterbox.printStatistics(solver);

	}

}
