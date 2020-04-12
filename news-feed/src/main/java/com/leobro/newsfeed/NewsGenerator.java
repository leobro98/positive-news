package com.leobro.newsfeed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generates news messages at the configured interval. The priority of a message is a random integer within the range
 * [0..9] (0 is the lowest and 9 is the highest priority). News messages with the higher priority are generated with
 * less probability than those with the lower priority. The headline of a message is a random combination of several
 * words from a configured list. The word count in the headline is a random number from a minimal word count to a
 * maximal word count (default is from three to five words).
 */
class NewsGenerator {

	private static final int MIN_WORDS_IN_HEADLINE = 3;
	private static final int MAX_WORDS_IN_HEADLINE = 5;

	private int minWords = MIN_WORDS_IN_HEADLINE;
	private int maxWords = MAX_WORDS_IN_HEADLINE;
	private final long interval;
	private final String[] headlineWords;
	private final WeightedRandomGenerator generator;
	private final ObjectMapper objectMapper;

	/**
	 * Creates a new instance of the NewsGenerator.
	 *
	 * @param interval        the interval between news messages in milliseconds,
	 * @param headlineWords   the array of all words from which the news headline is composed,
	 * @param priorityWeights the array of weights (in percent) for all possible priority values, the value
	 *                        corresponding to the index of the weight in the array.
	 */
	public NewsGenerator(long interval, String[] headlineWords, double[] priorityWeights) {
		this.interval = interval;
		this.headlineWords = headlineWords;
		generator = new WeightedRandomGenerator(priorityWeights);
		objectMapper = new ObjectMapper();
	}

	/**
	 * Waits configured time, then outputs a news message.
	 *
	 * @return the news message as JSON string.
	 */
	public String pauseAndGenerateMessage() throws JsonProcessingException {
		pause(interval);
		NewsMessage message = createMessage();
		return convertToJson(message);
	}

	private void pause(long delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private NewsMessage createMessage() {
		NewsMessage message = new NewsMessage();
		message.setPriority(generatePriority());
		message.setHeadline(generateHeadline());
		return message;
	}

	/**
	 * Priority values obey defined statistical distribution.
	 *
	 * @return random priority value with defined distribution.
	 */
	private int generatePriority() {
		return generator.getWeightedRandom();
	}

	private String generateHeadline() {
		String headline = "";
		int wordCount = getRandomIntegerWithinRange(minWords, maxWords);

		for (int i = 0; i < wordCount; i++) {
			int wordIndex = getRandomIntegerWithinRange(0, headlineWords.length - 1);
			headline += headlineWords[wordIndex] + " ";
		}
		return headline.trim();
	}

	private static int getRandomIntegerWithinRange(double min, double max) {
		return (int) ((Math.random() * ((max - min) + 1)) + min);
	}

	private String convertToJson(NewsMessage message) throws JsonProcessingException {
		return objectMapper.writeValueAsString(message);
	}

	/**
	 * Sets the minimal word count in the news headline.
	 *
	 * @param wordCount the minimal word count for the headline.
	 */
	public void setMinWordsInHeadline(int wordCount) {
		minWords = wordCount;
	}

	/**
	 * Sets the maximal word count in the news headline.
	 *
	 * @param wordCount the maximal word count for the headline.
	 */
	public void setMaxWordsInHeadline(int wordCount) {
		maxWords = wordCount;
	}
}
