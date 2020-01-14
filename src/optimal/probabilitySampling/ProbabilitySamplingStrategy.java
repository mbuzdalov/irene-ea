package optimal.probabilitySampling;

public enum ProbabilitySamplingStrategy {
    ITERATIVE {
        @Override
        public ProbabilitySearcher create(double leftProb, double rightProb, double precision) {
            return new IterativeProbabilitySearcher(leftProb, rightProb, precision);
        }
    }, TERNARY_SEARCH {
        @Override
        public ProbabilitySearcher create(double leftProb, double rightProb, double precision) {
            throw new UnsupportedOperationException("Ternary search is not implemented yet");
        }
    };

    public abstract ProbabilitySearcher create(double leftProb, double rightProb, double precision);
}
