package com.leobro.newsanalyser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        NewsReceiverServer server = new NewsReceiverServer();
        server.listenForNews();
    }
}
