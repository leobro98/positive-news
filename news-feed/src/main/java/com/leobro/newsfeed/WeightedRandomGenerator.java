package com.leobro.newsfeed;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates random integer numbers with the frequency distribution corresponding to the specified weights in percents.
 * Numbers are generated from 0 to the count of specified weights minus 1.
 */
public class WeightedRandomGenerator {

	private final Map<Integer, Double> weights;
	private double weightSum;

	/**
	 * Creates an instance of the {@link WeightedRandomGenerator} with the defined distribution of random numbers.
	 *
	 * @param weights array of weights for the whole range of generated numbers; number corresponds to the index of
	 *                the weight in the array; the weights are given in percents. If e.g. weights 30, 70 are given,
	 *                this means that 0 is generated in 30% of calls and 1 is generated in 70% of calls.
	 */
	public WeightedRandomGenerator(double[] weights) {
		this.weights = new HashMap<>();

		for (int i = 0; i < weights.length; i++) {
			addWeight(i, weights[i]);
		}
	}

	private void addWeight(int number, double weight) {
		if (weights.get(number) != null) {
			weightSum -= weights.get(number);
		}
		weights.put(number, weight);
		weightSum += weight;
	}

	/**
	 * Returns the next random number with the average frequency corresponding to its weight.
	 *
	 * @return the next random number from the distribution.
	 */
	public int getWeightedRandom() {
		double random = Math.random() * 100.0d;
		// correction for the case when weights do not produce 100% in sum
		double ratio = 100.0d / weightSum;
		double tempWeight = 0;

		// Let's take for example weights 5, 3, 2 and generate the uniformly distributed random number in the range
		// from 0 to 10. Then we can distribute the numbers to be generated according the weights over the range the
		// following way: 0, 0, 0, 0, 0, 1, 1, 1, 2, 2. Then if the uniformly random number is between 0 and 5, we
		// return 0; if it is between 5 and 8, we return 1; if it is between 8 and 10, we return 2.
		for (Integer i : weights.keySet()) {
			tempWeight += weights.get(i);

			if (tempWeight >= random / ratio) {
				return i;
			}
		}
		return 0;
	}
}
