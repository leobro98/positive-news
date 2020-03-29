package com.leobro.newsfeed;

import java.io.IOException;
import java.net.ConnectException;

public class Main {

	public static void main(String[] args) throws IOException {
		NewsFeeder feeder = new NewsFeeder();
		try {
			feeder.feedNews();
		} catch (ConnectException e) {
			System.out.println(e.toString());
			System.out.println("Server doesn't accept connections");
		}
	}
}
