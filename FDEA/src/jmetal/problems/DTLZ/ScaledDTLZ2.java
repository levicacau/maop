package jmetal.problems.DTLZ;



import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

/**
 * Class representing problem ScaledDTLZ2
 */
public class ScaledDTLZ2 extends Problem {

	/**
	 * Creates a default SDTLZ2 problem (12 variables and 3 objectives)
	 * 
	 * @param solutionType
	 *            The solution type must "Real" or "BinaryReal".
	 */
	public ScaledDTLZ2(String solutionType) throws ClassNotFoundException {
		this(solutionType, 12, 3);
	} // DTLZ2

	/**
	 * Creates a new instance of SDTLZ2
	 * 
	 * @param numberOfVariables
	 *            Number of variables
	 * @param numberOfObjectives
	 *            Number of objective functions
	 * @param solutionType
	 *            The solution type must "Real" or "BinaryReal".
	 */
	public ScaledDTLZ2(String solutionType, Integer numberOfVariables,
			Integer numberOfObjectives) {
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = numberOfObjectives;
		numberOfConstraints_ = 0;
		problemName_ = "ScaledDTLZ2";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
		for (int var = 0; var < numberOfVariables_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for

		if (solutionType.compareTo("BinaryReal") == 0)
			solutionType_ = new BinaryRealSolutionType(this);
		else if (solutionType.compareTo("Real") == 0)
			solutionType_ = new RealSolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}
	} // DTLZ2

	/**
	 * Evaluates a solution
	 * 
	 * @param solution
	 *            The solution to evaluate
	 * @throws JMException
	 */
	public void evaluate(Solution solution) throws JMException {
		Variable[] gen = solution.getDecisionVariables();

		double[] x = new double[numberOfVariables_];
		double[] f = new double[numberOfObjectives_];
		int k = numberOfVariables_ - numberOfObjectives_ + 1;

		for (int i = 0; i < numberOfVariables_; i++)
			x[i] = gen[i].getValue();

		double g = 0.0;
		for (int i = numberOfVariables_ - k; i < numberOfVariables_; i++)
			g += (x[i] - 0.5) * (x[i] - 0.5);

		for (int i = 0; i < numberOfObjectives_; i++)
			f[i] = 1.0 + g;

		for (int i = 0; i < numberOfObjectives_; i++) {
			for (int j = 0; j < numberOfObjectives_ - (i + 1); j++)
				f[i] *= Math.cos(x[j] * 0.5 * Math.PI);
			if (i != 0) {
				int aux = numberOfObjectives_ - (i + 1);
				f[i] *= Math.sin(x[aux] * 0.5 * Math.PI);
			} // if
		} // for

		double factor = getFactor(numberOfObjectives_);
		for (int i = 0; i < numberOfObjectives_; i++)
			solution.setObjective(i, f[i] * Math.pow(factor, i));
	}
	
	
	double getFactor(int obj){
		if (obj == 3)
			return 10;
		else if (obj == 5)
			return 10;
		else if (obj == 8)
			return 3;
		else if (obj == 10)
			return 3;
		else if (obj == 15)
			return 2;
		else if (obj == 20)
			return 1.2;
		else 
			return 1;
		
	}
} // evaluate
