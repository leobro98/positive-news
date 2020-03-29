package com.leobro.newsanalyser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class AnalyserTest {

	private static final String HEADLINE = "headline";
	private static final String PRIORITY = "priority";
	private static final String[] GOOD_WORDS = new String[]{"up", "good", "high", "success"};
	private static final String[] BAD_WORDS = new String[]{"bad", "failure", "down", "fall"};
	private static final int PIORITY_VALUE = 9;

	private boolean isMessagePositive;
	private Reporter reporter;
	private Analyser stubbedAnalyser;
	private Analyser analyser;

	@Before
	public void setUp() {
		reporter = Mockito.mock(Reporter.class);
		Mockito.when(reporter.getPositiveWords()).thenReturn(GOOD_WORDS);

		stubbedAnalyser = new Analyser(reporter) {
			@Override
			NewsMessage convertToMessage(String inputLine) {
				return new NewsMessage();
			}

			@Override
			boolean isPositive(NewsMessage message) {
				return isMessagePositive;
			}
		};
		analyser = new Analyser(reporter);
	}

	@Test
	public void when_analyseIsCalledAndMessageIsPositive_then_messageIsAddedToReporter() {
		isMessagePositive = true;
		stubbedAnalyser.analyse("");
		Mockito.verify(reporter, times(1)).add(any(NewsMessage.class));
	}

	@Test
	public void when_analyseIsCalledAndMessageIsNegative_then_messageIsIgnored() {
		isMessagePositive = false;
		stubbedAnalyser.analyse("");
		Mockito.verify(reporter, never()).add(any(NewsMessage.class));
	}

	@Test
	public void when_moreThanHalfWordsArePositive_then_messageIsPositive() {
		NewsMessage message = new NewsMessage();
		message.setHeadline(GOOD_WORDS[0] + " " + GOOD_WORDS[1] + " " + GOOD_WORDS[2] + " "
				+ BAD_WORDS[0] + " " + BAD_WORDS[1]);

		boolean isPositive = analyser.isPositive(message);

		assertTrue(isPositive);
	}

	@Test
	public void when_AllWordsArePositive_then_messageIsPositive() {
		NewsMessage message = new NewsMessage();
		message.setHeadline(GOOD_WORDS[0] + " " + GOOD_WORDS[1] + " " + GOOD_WORDS[2] + " " + GOOD_WORDS[3]);

		boolean isPositive = analyser.isPositive(message);

		assertTrue(isPositive);
	}

	@Test
	public void when_lessThanHalfWordsArePositive_then_messageIsNegative() {
		NewsMessage message = new NewsMessage();
		message.setHeadline(GOOD_WORDS[0] + " " + BAD_WORDS[0] + " " + BAD_WORDS[1]);

		boolean isPositive = analyser.isPositive(message);

		assertFalse(isPositive);
	}

	@Test
	public void when_noneOfWordsArePositive_then_messageIsNegative() {
		NewsMessage message = new NewsMessage();
		message.setHeadline(BAD_WORDS[0] + " " + BAD_WORDS[1] + " " + BAD_WORDS[2] + " " + BAD_WORDS[3]);

		boolean isPositive = analyser.isPositive(message);

		assertFalse(isPositive);
	}

	@Test
	public void when_convertToMessage_then_messageIsCorrect() {
		NewsMessage message = analyser.convertToMessage("{\"" + HEADLINE + "\":\"" + GOOD_WORDS[0] + "\",\""
				+ PRIORITY + "\":" + PIORITY_VALUE + "}");

		assertNotNull(message);
		assertThat(message.getHeadline(), is(GOOD_WORDS[0]));
		assertThat(message.getPriority(), is(PIORITY_VALUE));
	}
}
