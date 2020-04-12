package com.leobro.newsanalyser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

/**
 * Contains business logic for analysing the incoming news. If more than 50% of words in the headline of the news
 * message are positive, the news message as a whole is considered positive. The list of words regarded as positive
 * is configured. Negative news messages are ignored, positive ones are passed to the instance of the {@link Reporter}.
 * Several {@link Analyser} instances working in different threads pass the messages to one instance of the
 * {@link Reporter}.
 */
class Analyser {

	private final Reporter reporter;
	private final ObjectMapper objectMapper;
	private final List<String> positiveWords;

	/**
	 * Creates a new instance of the {@link Analyser} class.
	 *
	 * @param reporter instance of the {@link Reporter} class to collect news messages.
	 */
	public Analyser(Reporter reporter) {
		this.reporter = reporter;
		positiveWords = Arrays.asList(reporter.getPositiveWords());
		objectMapper = new ObjectMapper();
	}

	/**
	 * Used for analysis of a news message. If the message is regarded positive, it is passed to {@link Reporter}
	 * for future reporting. The adding of the message to the reporter is synchronised to prevent a race condition
	 * with another threads trying to do the same.
	 *
	 * @param inputLine the text containing a news message in JSON format.
	 */
	public void analyse(String inputLine) {
		NewsMessage message = convertToMessage(inputLine);

		if (isPositive(message)) {
			synchronized (reporter) {
				reporter.add(message);
			}
		}
	}

	NewsMessage convertToMessage(String inputLine) {
		NewsMessage message = null;
		try {
			message = objectMapper.readValue(inputLine, NewsMessage.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return message;
	}

	boolean isPositive(NewsMessage message) {
		if (message == null) {
			return false;
		}

		String[] words = message.getHeadline().split(" ");
		int positiveCount = 0;

		for (String word : words) {
			if (positiveWords.contains(word)) {
				positiveCount++;
			}
		}
		// cast to do floating point calculation
		float ratio = positiveCount / (float) words.length;
		return ratio > 0.5;
	}
}
