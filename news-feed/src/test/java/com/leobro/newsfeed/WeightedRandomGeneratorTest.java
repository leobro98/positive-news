package com.leobro.newsfeed;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class WeightedRandomGeneratorTest {

	@Test
	public void when_givenWeights_then_returnsWeightedIntegers() {
		double[] weights = new double[]{50, 30, 20};
		int attemptCount = 1000000;
		Map<Integer, Double> distribution = new HashMap<>();
		WeightedRandomGenerator generator = new WeightedRandomGenerator(weights);

		for (int i = 0; i < attemptCount; i++) {
			int random = generator.getWeightedRandom();
			distribution.put(random, (distribution.get(random) == null)
					? 100d / attemptCount
					: distribution.get(random) + 100d / attemptCount);
		}

		assertThat(Math.round(distribution.get(0)), is(50L));
		assertThat(Math.round(distribution.get(1)), is(30L));
		assertThat(Math.round(distribution.get(2)), is(20L));
	}
}
