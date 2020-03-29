package com.leobro.newsanalyser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Utility class to read properties from the {@code application.properties} file.
 */
class Configuration {

	private static final String PROPERTIES_FILE = "application.properties";
	private static final String PORT_KEY = "port";
	private static final String DEFAULT_PORT = "5555";
	private static final String REPORTING_PERIOD_KEY = "reportingPeriodInMilliseconds";
	private static final String DEFAULT_REPORTING_PERIOD = "10000";
	private static final String NEWS_LIMIT_KEY = "maxNewsCountToShow";
	private static final String DEFAULT_NEWS_LIMIT = "3";
	private static final String POSITIVE_WORDS_KEY = "positiveWords";
	private static final String DEFAULT_POSITIVE_WORDS = "up,rise,good,success,high,über";

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
	 * Returns number of port on which TCP server will wait connections.
	 *
	 * @return the port number.
	 */
	public int getServerPort() {
		return Integer.parseInt(config.getProperty(PORT_KEY, DEFAULT_PORT));
	}

	/**
	 * Returns the period in milliseconds for printing information on latest news.
	 *
	 * @return the period in milliseconds.
	 */
	public int getReportingPeriod() {
		return Integer.parseInt(config.getProperty(REPORTING_PERIOD_KEY, DEFAULT_REPORTING_PERIOD));
	}

	/**
	 * Returns the maximum number of positive news items reported periodically.
	 *
	 * @return the limit of news items count in one report.
	 */
	public int getNewsLimit() {
		return Integer.parseInt(config.getProperty(NEWS_LIMIT_KEY, DEFAULT_NEWS_LIMIT));
	}

	public String[] getPositiveWords() {
		String positiveLine = config.getProperty(POSITIVE_WORDS_KEY, DEFAULT_POSITIVE_WORDS);
		return positiveLine.trim().split(",");
	}
}
