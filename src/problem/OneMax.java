package problem;

import java.util.List;

public class OneMax extends AbstractProblem {

    public OneMax(int n) {
        super(n);
    }

    @Override
    protected int getHonestFitness() {
        int fit = 0;
        for (boolean b : myOffspring) {
            if (b) {
                fit++;
            }
        }
        return fit;
    }

    @Override
    public int calculatePatchFitness(List<Integer> patch) {
        int newFitness = myFitness;
        for (Integer i : patch) {
            if (myOffspring[i]) {
                newFitness--;
            } else {
                newFitness++;
            }
        }
        return newFitness;
    }
}
