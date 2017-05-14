package helpers;

import java.util.ArrayList;
import java.util.List;

public class IterationIntegralContainer {
    private List<IterationAndValue> myPairs;

    public IterationIntegralContainer() {
        this.myPairs = new ArrayList<>();
    }

    public void add(Integer iteration, Double value) {
        myPairs.add(new IterationAndValue(iteration, value));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (IterationAndValue pair : myPairs) {
            sb.append(pair).append("\n");
        }

        return sb.toString();
    }

    public List<IterationAndValue> getMyPairs() {
        return myPairs;
    }

    public static class IterationAndValue {
        private Integer iteration;

        private Double value;

        public IterationAndValue(Integer iteration, Double value) {
            this.iteration = iteration;
            this.value = value;
        }

        public Integer getIteration() {
            return iteration;
        }

        public Double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return iteration + "\t" + value;
        }
    }
}
