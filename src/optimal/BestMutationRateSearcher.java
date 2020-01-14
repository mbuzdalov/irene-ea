package optimal;

import com.sun.istack.internal.NotNull;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;
import optimal.probabilitySampling.ProbabilitySearcher;
import problem.Problem;
import problem.ProblemsManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BestMutationRateSearcher {
    final ProblemsManager.ProblemType myProblemType;
    private final double INFINITY = Double.MAX_VALUE;
    private int myProblemSize;
    private int myLambda;
    private int myBeginFitness;
    private int myEndFitness;
    private final double minMutationProbability;
    private final double maxMutationProbability;
    private final double precisionForProbability;
    private static final Double EPS = 0.0000000001;

    public BestMutationRateSearcher(ProblemsManager.ProblemType problemType,
                                    int problemSize,
                                    int lambda,
                                    int beginFitness,
                                    int endFitness,
                                    double minMutationProbability,
                                    double maxMutationProbability,
                                    double precisionForProbability) {
        myProblemType = problemType;
        myProblemSize = problemSize;
        myLambda = lambda;
        myBeginFitness = beginFitness;
        myEndFitness = endFitness;
        this.minMutationProbability = minMutationProbability;
        this.maxMutationProbability = maxMutationProbability;
        this.precisionForProbability = precisionForProbability;
    }

    protected ProbabilityVectorGenerator getProbabilityVectorGenerator(double currentProbability, @NotNull Problem problem) {
        return new ProbabilityVectorGenerator(currentProbability, myProblemSize, myLambda,
                2.0 / (double) myProblemSize, problem);
    }

    protected ProbabilitySearcher getProbabilitySearcher() {
        return ProbabilitySamplingStrategy.ITERATIVE.create(minMutationProbability, maxMutationProbability, precisionForProbability);
    }

    public ArrayList<Double> getBestMutationProbabilities() {
        ArrayList<Double> T = new ArrayList<>(myEndFitness - myBeginFitness + 1); // optimization time for any fitness
        for (int f = myBeginFitness; f <= myEndFitness; f++) {
            T.add(INFINITY);
        }
        T.set(myEndFitness - myBeginFitness, 0.);
        HashMap<Integer, Double> pOpt = new HashMap<>(); // optimal mutation probability for any fitness
        for (int fitness = myEndFitness - 1; fitness >= myBeginFitness; fitness--) {
            Problem problem = ProblemsManager.createProblemInstanceWithFixedFitness(myProblemType, myProblemSize, fitness);
            ProbabilitySearcher ps = getProbabilitySearcher();
            int feedback = -1;
            for (double p = ps.getInitialProbability(); !ps.isFinished(); p = ps.getNextProb(feedback)) {
                ProbabilityVectorGenerator pvg = getProbabilityVectorGenerator(p, problem);
                ArrayList<Double> v = pvg.getProbabilityVector();
                Double p0Tilda = v.get(0);
                if (Math.abs(p0Tilda - 1.) < EPS) {
                    update(T, pOpt, INFINITY, p, fitness);
                } else {
                    double tFP = 1. / (1. - p0Tilda);
                    int fitnessOffset = fitness - myBeginFitness;
                    for (int i = 1; i < v.size(); i++) {
                        tFP += T.get(fitnessOffset + i) * v.get(i) / (1. - p0Tilda);
                    }
                    update(T, pOpt, tFP, p, fitness);
                }
            }
        }
        ArrayList<Double> pOptList = new ArrayList<>(pOpt.size());
        for (int i = 0; i < pOpt.size(); i++) {
            pOptList.add(-1.);
        }
        pOpt.forEach(pOptList::set);
        return pOptList;
    }

    private void update(ArrayList<Double> T, HashMap<Integer, Double> pOpt,
                               Double optimizationTime, Double currentProbability, int currentFitness) {
        int fitnessOffset = currentFitness - myBeginFitness;
        if ((Math.abs(INFINITY - optimizationTime) < EPS) && !pOpt.containsKey(fitnessOffset)) {
            pOpt.put(fitnessOffset, currentProbability);
        }

        if (optimizationTime < T.get(fitnessOffset)) {
            T.set(fitnessOffset, optimizationTime);
            pOpt.put(fitnessOffset, currentProbability);
        }
    }
}
