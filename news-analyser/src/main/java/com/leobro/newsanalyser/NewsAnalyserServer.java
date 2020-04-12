package com.leobro.newsanalyser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.IllegalBlockingModeException;

/**
 * TCP server. Opens TCP socket on the configured port and waits for the news feed client to connect.
 * When the connection with the news feed is obtained, creates a {@link NewsReceiver} in a separate thread
 * to receive messages from the client. Can maintain any number of simultaneous connections.
 */
class NewsAnalyserServer {

	private final ServerSocket serverSocket;
	private final Reporter reporter;

	/**
	 * Creates a new instance of the server.
	 *
	 * @throws IOException              if an I/O error occurs when opening the socket.
	 * @throws SecurityException        if a security manager exists and its checkListen method doesn't allow the operation.
	 * @throws IllegalArgumentException if the port parameter is outside the specified range of valid port values,
	 *                                  which is between 0 and 65535, inclusive.
	 */
	public NewsAnalyserServer() throws IOException {
		Configuration config = new Configuration();
		serverSocket = new ServerSocket(config.getServerPort());
		reporter = new Reporter(config.getReportingPeriod(), config.getNewsLimit(), config.getPositiveWords());
	}

	/**
	 * Starts listening to the connections from news feed TCP clients. Creates a {@link NewsReceiver}
	 * in a separate thread to receive messages from the client.
	 *
	 * @throws IOException                  if an I/O error occurs when waiting for a connection.
	 * @throws SecurityException            if a security manager exists and its checkAccept method doesn't allow the operation.
	 * @throws SocketTimeoutException       if a timeout was previously set with setSoTimeout and the timeout has been reached.
	 * @throws IllegalBlockingModeException if this socket has an associated channel, the channel is in non-blocking
	 *                                      mode, and there is no connection ready to be accepted.
	 */
	public void listenForNews() throws IOException {
		while (true) {
			Socket clientSocket = serverSocket.accept();
			if (clientSocket == null) {
				break;
			}
			NewsReceiver receiver = new NewsReceiver(clientSocket, reporter);
			receiver.start();
		}
	}
}
