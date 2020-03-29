package com.leobro.newsfeed;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ConfigurationTest {

	private static final String INTERVAL_KEY = "feedingIntervalInMilliseconds";
	private static final long INTERVAL = 500;
	private static final String ANALYSER_IP_KEY = "newsAnalyserIp";
	private static final String ANALYSER_IP = "127.0.0.1";
	private static final String ANALYSER_PORT_KEY = "newsAnalyserPort";
	private static final int ANALYSER_PORT = 7777;
	private static final String HEADLINE_WORDS_KEY = "headlineWords";
	private static final String[] HEADLINE_WORDS = new String[]{"good", "bad"};
	private static final String PRIORITY_WEIGHTS_KEY = "priorityWeights";
	private static final double[] PRIORITY_WEIGHTS = new double[]{50, 50};

	private Configuration config;

	@Before
	public void setUp() {
		String properties = INTERVAL_KEY + "=" + INTERVAL + "\n"
				+ ANALYSER_IP_KEY + "=" + ANALYSER_IP + "\n"
				+ ANALYSER_PORT_KEY + "=" + ANALYSER_PORT + "\n"
				+ HEADLINE_WORDS_KEY + "=" + HEADLINE_WORDS[0] + "," + HEADLINE_WORDS[1] + "\n"
				+ PRIORITY_WEIGHTS_KEY + "=" + PRIORITY_WEIGHTS[0] + ", " + PRIORITY_WEIGHTS[1];

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
	public void when_propertiesAreGiven_then_intervalIsRead() {
		long interval = config.getFeedingInterval();
		assertThat(interval, is(INTERVAL));
	}

	@Test
	public void when_propertiesAreGiven_then_serverIpIsRead() {
		String serverIp = config.getServerIp();
		assertThat(serverIp, is(ANALYSER_IP));
	}

	@Test
	public void when_propertiesAreGiven_then_serverPortIsRead() {
		int port = config.getServerPort();
		assertThat(port, is(ANALYSER_PORT));
	}

	@Test
	public void when_propertiesAreGiven_then_headlineWordsAreRead() {
		String[] words = config.getHeadlineWords();

		assertThat(words.length, is(2));
		assertThat(words[0], is(HEADLINE_WORDS[0]));
		assertThat(words[1], is(HEADLINE_WORDS[1]));
	}

	@Test
	public void when_propertiesAreGiven_then_priorityWeightsAreRead() {
		double[] weights = config.getPriorityWeights();

		assertThat(weights.length, is(PRIORITY_WEIGHTS.length));
		assertThat(weights[0], is(PRIORITY_WEIGHTS[0]));
		assertThat(weights[1], is(PRIORITY_WEIGHTS[1]));
	}
}
