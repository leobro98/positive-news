package com.leobro.newsanalyser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Responsible for formatting and printing the report in the console. Selects the limited number of the most important
 * news messages (according to their priority) and prints their headlines together with the total number of
 * positive news messages received since the last reporting.
 */
class ConsolePrinter {

	private int newsLimit;

	/**
	 * Creates a new instance of the {@link ConsolePrinter}.
	 *
	 * @param newsLimit the maximal count of news messages to be shown in the report.
	 */
	public ConsolePrinter(int newsLimit) {
		this.newsLimit = newsLimit;
	}

	/**
	 * Prints a periodic report - total count of positive messages since the last report and the limited count
	 * of headlines of the most important messages since the last report.
	 *
	 * @param messages all positive news messages received since the last report.
	 */
	public void print(List<NewsMessage> messages) {
		int totalCount = messages.size();

		System.out.println("======================================");
		System.out.println("Positive news since the last digest: " + totalCount);
		System.out.println("The most important news:");
		System.out.println("--------------------------------------");

		List<NewsMessage> importantNews = selectImportantNews(messages);

		for (NewsMessage message : importantNews) {
			System.out.println(message.getHeadline());
		}
	}

	List<NewsMessage> selectImportantNews(List<NewsMessage> messages) {
		return messages.stream()
				.filter(distinctByKey(NewsMessage::getHeadline))
				.sorted((m1, m2) -> Integer.compare(m2.getPriority(), m1.getPriority()))
				.limit(newsLimit)
				.collect(Collectors.toList());
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
	{
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
