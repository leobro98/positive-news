package com.leobro.newsfeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Utility class to read properties from the {@code application.properties} file.
 */
class Configuration {

	private static final String PROPERTIES_FILE = "application.properties";
	private static final String INTERVAL_KEY = "feedingIntervalInMilliseconds";
	private static final String DEFAULT_INTERVAL = "1000";
	private static final String NEWS_ANALYSER_IP_KEY = "newsAnalyserIp";
	private static final String DEFAULT_NEWS_ANALYSER_IP = "127.0.0.1";
	private static final String NEWS_ANALYSER_PORT_KEY = "newsAnalyserPort";
	private static final String DEFAULT_NEWS_ANALYSER_PORT = "5555";
	private static final String HEADLINE_WORDS_KEY = "headlineWords";
	private static final String DEFAULT_HEADLINE_WORDS = "positive,negative";
	private static final String PRIORITY_WEIGHTS_KEY = "priorityWeights";
	private static final String DEFAULT_PRIORITY_WEIGHTS = "29.3, 19.3, 14.3, 10.9, 8.4, 6.5, 4.8, 3.4, 2.1, 1";
	private static final String MIN_WORDS_IN_HEADLINE_KEY = "minimumWordsInHeadline";
	private static final String DEFAULT_MIN_WORDS_IN_HEADLINE = "3";
	private static final String MAX_WORDS_IN_HEADLINE_KEY = "maximumWordsInHeadline";
	private static final String DEFAULT_MAX_WORDS_IN_HEADLINE = "5";

	Properties config;

	/**
	 * Creates a new instance of the {@link Configuration} class. Loads all properties from the file.
	 */
	public Configuration() {
		config = new Properties();

		try {
			config.load(getPropertiesFileReader());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Reader getPropertiesFileReader() {
		return new BufferedReader(
				new InputStreamReader(
						Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE),
						StandardCharsets.UTF_8));
	}

	/**
	 * Returns the interval in milliseconds between successive news messages.
	 *
	 * @return interval between news messages in milliseconds.
	 */
	public long getFeedingInterval() {
		return Long.parseLong(config.getProperty(INTERVAL_KEY, DEFAULT_INTERVAL));
	}

	/**
	 * Returns the IP address of the computer hosting the news receiver server.
	 *
	 * @return the IP address of the news analyser server.
	 */
	public String getServerIp() {
		return config.getProperty(NEWS_ANALYSER_IP_KEY, DEFAULT_NEWS_ANALYSER_IP);
	}

	/**
	 * Returns the port number at which the news receiver server is listening.
	 *
	 * @return the port number of the news analyser server.
	 */
	public int getServerPort() {
		return Integer.parseInt(config.getProperty(NEWS_ANALYSER_PORT_KEY, DEFAULT_NEWS_ANALYSER_PORT));
	}

	/**
	 * Returns the list of words from which the news headlines are to be composed.
	 *
	 * @return the list of words for the news headlines.
	 */
	public String[] getHeadlineWords() {
		String words = config.getProperty(HEADLINE_WORDS_KEY, DEFAULT_HEADLINE_WORDS);
		return words.trim().split(",");
	}

	/**
	 * Returns the list of weights for the messages' priorities in percents. The more the weight, the more frequent
	 * should be the priority. The index of the priority weight in the array is the priority value.
	 *
	 * @return the list of weights (in percents) of priorities of news messages.
	 */
	public double[] getPriorityWeights() {
		String weightsLine = config.getProperty(PRIORITY_WEIGHTS_KEY, DEFAULT_PRIORITY_WEIGHTS);
		String[] weightStrings = weightsLine.trim().split(",");

		double[] weights = new double[weightStrings.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Double.parseDouble(weightStrings[i]);
		}
		return weights;
	}

	/**
	 * Returns the minimal count of words the headline of a news message should contain.
	 *
	 * @return the minimal word count in the headline of a news message.
	 */
	public int getMinWordsInHeadline() {
		String wordCount = config.getProperty(MIN_WORDS_IN_HEADLINE_KEY, DEFAULT_MIN_WORDS_IN_HEADLINE);
		return Integer.parseInt(wordCount);
	}

	/**
	 * Returns the maximal count of words the headline of a news message should contain.
	 *
	 * @return the maximal word count in the headline of a news message.
	 */
	public int getMaxWordsInHeadline() {
		String wordCount = config.getProperty(MAX_WORDS_IN_HEADLINE_KEY, DEFAULT_MAX_WORDS_IN_HEADLINE);
		return Integer.parseInt(wordCount);
	}
}
