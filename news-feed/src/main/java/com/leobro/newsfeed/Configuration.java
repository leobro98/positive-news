package com.leobro.newsfeed;

import java.io.*;
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

	public long getFeedingInterval() {
		return Long.parseLong(config.getProperty(INTERVAL_KEY, DEFAULT_INTERVAL));
	}

	public String getServerIp() {
		return config.getProperty(NEWS_ANALYSER_IP_KEY, DEFAULT_NEWS_ANALYSER_IP);
	}

	public int getServerPort() {
		return Integer.parseInt(config.getProperty(NEWS_ANALYSER_PORT_KEY, DEFAULT_NEWS_ANALYSER_PORT));
	}

	public String[] getHeadlineWords() {
		String words = config.getProperty(HEADLINE_WORDS_KEY, DEFAULT_HEADLINE_WORDS);
		return words.trim().split(",");
	}

	public double[] getPriorityWeights() {
		String weightsLine = config.getProperty(PRIORITY_WEIGHTS_KEY, DEFAULT_PRIORITY_WEIGHTS);
		String[] weightStrings = weightsLine.trim().split(",");

		double[] weights = new double[weightStrings.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Double.parseDouble(weightStrings[i]);
		}
		return weights;
	}

	public int getMinWordsInHeadline() {
		String wordCount = config.getProperty(MIN_WORDS_IN_HEADLINE_KEY, DEFAULT_MIN_WORDS_IN_HEADLINE);
		return Integer.parseInt(wordCount);
	}

	public int getMaxWordsInHeadline() {
		String wordCount = config.getProperty(MAX_WORDS_IN_HEADLINE_KEY, DEFAULT_MAX_WORDS_IN_HEADLINE);
		return Integer.parseInt(wordCount);
	}
}
