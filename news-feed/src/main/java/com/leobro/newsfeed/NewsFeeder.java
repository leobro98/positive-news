package com.leobro.newsfeed;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * TCP client sending news messages to the News Analyser server. Establishes a TCP connection with the server at the
 * configured address and port and sends news messages periodically at a configured interval.
 */
class NewsFeeder {

	private final String ip;
	private final int port;
	private PrintWriter out;
	private NewsGenerator generator;

	/**
	 * Creates a new instance of the {@link NewsFeeder} class. Reads configuration and creates an instance of the
	 * {@link NewsGenerator} to produce news messages.
	 */
	public NewsFeeder() {
		Configuration config = new Configuration();
		this.ip = config.getServerIp();
		this.port = config.getServerPort();
		createNewsGenerator(config);
	}

	private void createNewsGenerator(Configuration config) {
		generator = new NewsGenerator(config.getFeedingInterval(), config.getHeadlineWords(), config.getPriorityWeights());
		generator.setMinWordsInHeadline(config.getMinWordsInHeadline());
		generator.setMaxWordsInHeadline(config.getMaxWordsInHeadline());
	}

	/**
	 * Connects to the configured News Analyser server and indefinitely sends news messages at the configured interval.
	 * {@link NewsGenerator} produces messages periodically. As soon as the message is produced, it is immediately
	 * sent to the server.
	 *
	 * @throws IOException if an I/O error occurs when creating the socket or
	 * if an I/O error occurs when creating the output stream or if the socket is not connected.
	 * @throws UnknownHostException if the IP address of the host could not be determined.
	 * @throws SecurityException if a security manager exists and its checkConnect method doesn't allow the operation.
	 * @throws IllegalArgumentException if the port parameter is outside the specified range of valid port values,
	 * which is between 0 and 65535, inclusive.
	 * @throws JsonProcessingException if serialising an object is failed.
	 */
	public void feedNews() throws IOException {
		connectToNewsAnalyser();
		generateMessagesPeriodically();
	}

	private void connectToNewsAnalyser() throws IOException {
		Socket clientSocket = new Socket(ip, port);
		boolean autoFlushBuffer;
		out = new PrintWriter(
				new OutputStreamWriter(
						clientSocket.getOutputStream(), StandardCharsets.UTF_16),
				autoFlushBuffer = true);
	}

	private void generateMessagesPeriodically() throws JsonProcessingException {
		String message;
		while ((message = generator.pauseAndGenerateMessage()) != null) {
			sendMessage(message);
		}
	}

	private void sendMessage(String newsMessage) {
		out.println(newsMessage);
	}
}
