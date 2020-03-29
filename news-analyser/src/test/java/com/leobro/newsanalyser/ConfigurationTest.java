package com.leobro.newsanalyser;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ConfigurationTest {

	private static final String PORT_KEY = "port";
	private static final int PORT = 7777;
	private static final String PERIOD_KEY = "reportingPeriod";
	private static final int PERIOD = 10000;
	private static final String NEWS_LIMIT_KEY = "maxNewsCountToShow";
	private static final int NEWS_LIMIT = 3;
	private static final String POSITIVE_WORDS_KEY = "positiveWords";
	private static final String[] POSITIVE_WORDS = new String[]{"positive", "good"};

	private Configuration config;

	@Before
	public void setUp() {
		String properties = PORT_KEY + "=" + PORT + "\n"
				+ PERIOD_KEY + "=" + PERIOD + "\n"
				+ NEWS_LIMIT_KEY + "=" + NEWS_LIMIT + "\n"
				+ POSITIVE_WORDS_KEY + "=" + POSITIVE_WORDS[0] + "," + POSITIVE_WORDS[1];

		config = new Configuration(){
			@Override
			Reader getPropertiesFileReader() {
				return new BufferedReader(
						new InputStreamReader(
								new ByteArrayInputStream(properties.getBytes(StandardCharsets.UTF_8)),
								StandardCharsets.UTF_8));
			}
		};
	}

	@Test
	public void when_propertiesAreGiven_then_serverPortIsRead() {
		int port = config.getServerPort();
		assertThat(port, is(PORT));
	}

	@Test
	public void when_propertiesAreGiven_then_PeriodIsRead() {
		int period = config.getReportingPeriod();
		assertThat(period, is(PERIOD));
	}

	@Test
	public void when_propertiesAreGiven_then_newsLimitIsRead() {
		int newsLimit = config.getNewsLimit();
		assertThat(newsLimit, is(NEWS_LIMIT));
	}

	@Test
	public void when_propertiesAreGiven_then_positiveWordsAreRead() {
		String[] words = config.getPositiveWords();

		assertThat(words.length, is(POSITIVE_WORDS.length));
		assertThat(words[0], is(POSITIVE_WORDS[0]));
		assertThat(words[1], is(POSITIVE_WORDS[1]));
	}
}
