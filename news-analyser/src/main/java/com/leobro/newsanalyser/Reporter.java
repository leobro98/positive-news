package com.leobro.newsanalyser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Responsible for accepting positive news messages from separate threads and periodically running a report on the
 * messages received since the last report. After the reporting, the accumulated messages are discarded.
 * <p>The class is thread-safe.
 */
public class Reporter {

	private final List<NewsMessage> messages;
	private final String[] positiveWords;
	private final ConsolePrinter printer;

	/**
	 * Creates a new instance of the {@link Reporter} class. Runs a Timer task at the configured interval
	 * in a separate thread to print the report.
	 *
	 * @param period        the period between reports in milliseconds,
	 * @param newsLimit     the maximal count of most important news to show in the report,
	 * @param positiveWords array of words that regarded positive.
	 */
	public Reporter(int period, int newsLimit, String[] positiveWords) {
		this.messages = new ArrayList<>();
		this.positiveWords = positiveWords;

		scheduleReports(period);
		printer = new ConsolePrinter(newsLimit);
	}

	private void scheduleReports(int period) {
		TimerTask printTask = new ReporterTask(this);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(printTask, period, period);
	}

	/**
	 * Adds a news message to the collection of last messages. This method is thread-safe.
	 *
	 * @param message the news message to add.
	 */
	public void add(NewsMessage message) {
		synchronized (this) {
			messages.add(message);
		}
	}

	/**
	 * Returns the array of configured positive words.
	 *
	 * @return words that are configured as positive.
	 */
	public String[] getPositiveWords() {
		return positiveWords;
	}

	/**
	 * The task running in the Timer thread to print the report.
	 */
	private class ReporterTask extends TimerTask {

		private final Reporter reporter;

		/**
		 * Creates a new {@link ReporterTask}.
		 *
		 * @param reporter the reference to the parent creator class to perform the thread synchronisation.
		 */
		public ReporterTask(Reporter reporter) {
			this.reporter = reporter;
		}

		/**
		 * Started periodically by the Timer to print the report. Passes accumulated messages to the
		 * {@link ConsolePrinter} and clears their original collection. The clearing of the collection is synchronised
		 * to avoid race condition with adding of new messages from different threads.
		 */
		@Override
		public void run() {
			List<NewsMessage> lastNews;

			synchronized (reporter) {
				lastNews = copyMessages(messages);
				messages.clear();
			}

			printer.print(lastNews);
		}

		private List<NewsMessage> copyMessages(List<NewsMessage> messages) {
			return new ArrayList<>(messages);
		}
	}
}
