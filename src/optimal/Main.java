package optimal;

import problem.ProblemsManager;

public class Main {
    public static void main(String[] args) {
        BestMutationRateSearcher searcher = new BestMutationRateSearcher(ProblemsManager.ProblemType.ONE_MAX, 100,
                2,50,100,0.02, 0.5, 0.01);
        System.out.println(searcher.getBestMutationProbabilities());
    }
}
