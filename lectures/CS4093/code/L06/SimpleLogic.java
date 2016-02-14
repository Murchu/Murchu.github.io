import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;


public class SimpleLogic {

	public static void main(String[] args) {
		Solver solver = new Solver();
		
		BoolVar b1 = VF.bool("b1", solver);
		IntVar v1 = VF.enumerated("v1", 0, 3, solver);
		IntVar v2 = VF.enumerated("v2", 0, 3, solver);
		
		Constraint c1 = ICF.arithm(v1, "<", 2);
		Constraint c2 = ICF.arithm(v2, ">", 1);
		LCF.ifThen(c1, c2);
		LCF.reification(b1, c1);
		
		Chatterbox.showSolutions(solver);
		solver.findAllSolutions();

		

	}

}
