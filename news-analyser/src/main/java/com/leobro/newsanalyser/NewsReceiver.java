package com.leobro.newsanalyser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * Responsible for the connection with one TCP client, which is a news feeder. Makes its work in a separate thread.
 * Created by {@link NewsAnalyserServer} for a new connection with a client. Accepts news messages and passes them
 * to the instance of the {@link Analyser} class for the analysis and further reporting.
 */
class NewsReceiver extends Thread {

	private final Socket clientSocket;
	private BufferedReader in;
	private final Analyser analyser;

	/**
	 * Creates a new instance of the {@link NewsReceiver} class. Creates as well an {@link Analyser} instance
	 * to analyse news messages coming from the client.
	 *
	 * @param clientSocket TCP socket with established connection from the client,
	 * @param reporter     an instance of the {@link Reporter} class to collect and then report news messages.
	 */
	public NewsReceiver(Socket clientSocket, Reporter reporter) {
		this.clientSocket = clientSocket;
		analyser = new Analyser(reporter);
	}

	/**
	 * Called by JVM to perform the work in a separate thread. Waits indefinitely for the news message from the
	 * client to come and passes it to the instance of the {@link Analyser}. Finishes when the client disconnects.
	 */
	@Override
	public void run() {
		try {
			in = createInputReader();
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				analyser.analyse(inputLine);
			}
		} catch (SocketException e) {
			// client socket has disconnected (error message: Connection reset)
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			tearDown();
		}
	}

	private BufferedReader createInputReader() throws IOException {
		return new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_16));
	}

	private void tearDown() {
		try {
			if (in != null) {
				in.close();
			}
			if (clientSocket != null) {
				clientSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
