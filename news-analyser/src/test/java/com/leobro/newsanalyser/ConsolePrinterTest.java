package com.leobro.newsanalyser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConsolePrinterTest {

	private static final int NEWS_LIMIT = 3;

	@Test
	public void when_selectImportantNews_then_selectsDistinctAndSortsAndLimitsCount() {
		ConsolePrinter printer = new ConsolePrinter(NEWS_LIMIT);
		List<NewsMessage> messages = createMessages();

		List<NewsMessage> selectedMessages = printer.selectImportantNews(messages);

		assertThat(selectedMessages.size(), is(NEWS_LIMIT));

		assertThat(selectedMessages.get(0).getPriority(), is(2));
		assertThat(selectedMessages.get(0).getHeadline(), is("headline3"));

		assertThat(selectedMessages.get(1).getPriority(), is(1));
		assertThat(selectedMessages.get(1).getHeadline(), is("headline1"));

		assertThat(selectedMessages.get(2).getPriority(), is(0));
		assertThat(selectedMessages.get(2).getHeadline(), is("headline2"));
	}

	private List<NewsMessage> createMessages() {
		List<NewsMessage> messages = new ArrayList<>();

		messages.add(createMessage("headline1", 1));
		messages.add(createMessage("headline2", 0));
		messages.add(createMessage("headline3", 2));
		messages.add(createMessage("headline4", 0));

		return messages;
	}

	private NewsMessage createMessage(String headline, int priority) {
		NewsMessage message = new NewsMessage();
		message.setHeadline(headline);
		message.setPriority(priority);
		return message;
	}
}
