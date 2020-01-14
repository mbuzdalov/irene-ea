package optimal.probabilitySampling;

public abstract class ProbabilitySearcher {
    protected final double myLeftProb;
    protected final double myRightProb;
    protected final double myPrecision;
    protected double myLastReturnedPrecision;

    protected ProbabilitySearcher(double leftProb, double rightProb, double precision) {
        myLeftProb = leftProb;
        myRightProb = rightProb;
        myPrecision = precision;
        myLastReturnedPrecision = leftProb - precision;
    }

    // if feedback > 0 then previous returned probability improved something
    public abstract double getNextProb(double feedback);

    public abstract double getInitialProbability();

    public abstract boolean isFinished();
}
