package com.leobro.newsfeed;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class NewsGeneratorTest {

	private static final long CONFIGURED_DELAY_IN_MS = 3000L;
	private static final long DELAY_INACCURACY = 100L;
	private static final String HEADLINE = "headline";
	private static final String PRIORITY = "priority";
	private static final String PRIORITY_RANGE = "[0-9]";
	private static final int MINIMAL_WORD_COUNT = 3;
	private static final int MAXIMAL_WORD_COUNT = 5;
	private static final String[] HEADLINE_WORDS = new String[]
			{"up", "down", "rise", "fall", "good", "bad", "success", "failure", "high", "low", "über", "unter"};
	private static final double[] PRIORITY_WEIGHTS = {50, 50};

	private NewsGenerator generator;

	@Before
	public void setUp() {
		generator = new NewsGenerator(0, HEADLINE_WORDS, PRIORITY_WEIGHTS);
	}

	@Test
	public void when_generateMessageCalled_then_respondsAfterPause() throws JsonProcessingException {
		generator = new NewsGenerator(CONFIGURED_DELAY_IN_MS, HEADLINE_WORDS, PRIORITY_WEIGHTS);
		LocalTime startTime = LocalTime.now();

		generator.pauseAndGenerateMessage();
		Duration delay = Duration.between(startTime, LocalTime.now());

		assertThat(Math.abs(delay.toMillis() - CONFIGURED_DELAY_IN_MS), lessThan(DELAY_INACCURACY));
	}

	@Test
	public void when_generateMessageCalled_then_messageHasHeadlineAndPriority() throws JsonProcessingException {
		String message = generator.pauseAndGenerateMessage();

		assertTrue(message.matches(".*\"" + HEADLINE + "\":.*"));
		assertTrue(message.matches(".*\"" + PRIORITY + "\":.*"));
	}

	@Test
	public void when_generateMessageCalled_then_priorityIsFrom0To9() throws JsonProcessingException {
		String message = generator.pauseAndGenerateMessage();

		assertTrue(message.matches(".*\"" + PRIORITY + "\":" + PRIORITY_RANGE + ".*"));
	}

	@Test
	public void when_generateMessageCalled_then_headlineContainsOnlyDefinedWords() throws JsonProcessingException {
		List<String> definedWords = Arrays.asList(HEADLINE_WORDS);

		String message = generator.pauseAndGenerateMessage();

		JSONObject jsonObject = new JSONObject(message);
		String headline = jsonObject.getString(HEADLINE);
		List<String> words = Arrays.asList(headline.split(" "));

		assertTrue(definedWords.containsAll(words));
	}

	@Test
	public void when_generateMessageCalled_then_headlineWordCountIsInRange() throws JsonProcessingException {
		String message = generator.pauseAndGenerateMessage();

		JSONObject jsonObject = new JSONObject(message);
		String headline = jsonObject.getString(HEADLINE);
		String[] words = headline.split(" ");

		assertThat(words.length, greaterThanOrEqualTo(MINIMAL_WORD_COUNT));
		assertThat(words.length, lessThanOrEqualTo(MAXIMAL_WORD_COUNT));
	}
}
