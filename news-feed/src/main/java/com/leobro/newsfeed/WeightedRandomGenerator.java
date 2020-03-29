package com.leobro.newsfeed;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates random integer numbers with specified weights. Numbers are generated from 0 to the count of specified
 * weights minus 1.
 */
public class WeightedRandomGenerator {

	private Map<Integer, Double> weights;
	private double weightSum;

	/**
	 * Creates an instance of the {@link WeightedRandomGenerator}.
	 *
	 * @param weights array of weights for the whole range of generated numbers; number corresponds to the index of
	 *                the weight in the array.
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
		double ratio = 100.0d / weightSum;
		double tempWeight = 0;

		for (Integer i : weights.keySet()) {
			tempWeight += weights.get(i);

			if (tempWeight >= random / ratio) {
				return i;
			}
		}
		return 0;
	}
}
