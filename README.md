# Positive News

This is a solution to the test task for an application consisting of a TCP cerver and a TCP client. The task description can be found in `task.md`.

In short, several TCP clients send messages to the TCP server. The server analyses the messages and periodicaly prints reports on messages received.

## Prerequisites

The project was built using Apache Maven 3.5.4 and Java 8.

## Building and running the application

### Using Maven and command line

1. Open command prompt.
2. Go to the `news-analyser` folder. Run the command `mvn clean package`.
3. Go to the `news-analyser/target` folder. Run the command `java -jar newsanalyser-1.0-SNAPSHOT-jar-with-dependencies.jar`.

The News Analyser server is running and printing (empty) digests every 10 seconds.

1. Open another command prompt.
2. Go to the `news-feed` folder. Run the command `mvn clean package`.
3. Go the the `news-feed/target` folder. Run the command `java -jar news-feed-1.0-SNAPSHOT-jar-with-dependencies.jar`.

The client should be running. Return to the first command prompt (server). The digests should contain up to 3 news headlines sorted by their priority. When needed, finish the running process pressing Ctrl+C.

You can run several TCP clients simultaneously. To run one more, open yet another command prompt and repeat the step 3 from the last list.

### Using IntelliJ IDEA

Creating configuration for the server.

1. Go to the "Edit Configurations" dialog.
2. Add new configuration of the Application type.
3. In the "Name:" for the configuration type `Server`.
4. For the "Main class:" select or type `com.leobro.newsanalyser.Main`.
5. For the "Working directory:" should be selected `<your path>\positive-news\news-analyser`.
6. For the "Use classpath of module:" select `news-analyser` module.
7. Save the configuration pressing Apply button.

Creating configuration for the client.

1. Add another new configuration of the Application type.
2. In the "Name:" for the configuration type `Client`.
3. For the "Main class:" select or type `com.leobro.newsfeed.Main`.
4. For the "Working directory:" should be selected `<your path>\positive-news\news-feed`.
5. For the "Use classpath of module:" select `news-feed` module.
6. Save the configuration pressing OK button.

Run the Server configuration. Run the Client configuration. Go to the "Run" tool window, select Server tab. You should see news digests every 10 seconds.

## Applications and their components

The project consists of two separate applications - news-analyser and news-feed. Their source can be imported into IntelliJ IDEA as two *modules* of a single project or as two separate projects. Each application has its own Main class with the `main()` method and can be run separately.

### News Analyser

This application resides in the `news-analyser` folder, or in the `news-analyser` module of the IntelliJ project.

#### NewsAnalyserServer

When the application is started, an instance of the `NewsAnalyserServer` class is created. In the constructor, an instance of the `ServerSocket` and of the `Reporter` class are created. Then the `listenForNews()` method is called. The method works indefinitely until the application is cancelled:

```Java
1	while (true) {
2		Socket clientSocket = serverSocket.accept();
3		...
4		NewsReceiver receiver = new NewsReceiver(clientSocket, reporter);
5		receiver.start();
6	}
```

The code comes to the line 2 and suspends until a new connection request from a TCP client comes to the port on which the `serverSocket` is created. When this happens, a new `Socket` instance `clientSocket` is created. This socket is connected with this specific client and can communicate with it. It is then passed on the line 4 together with the `reporter` object to the new instance of the `NewsReceiver` class.

#### NewsReceiver 

The `NewsReceiver` class extends `Thread`, so when its `start()` method is called, the control is passed to the `run()` method which works in a separate thread. The method works indefinitely until the TCP client disconnects:

```Java
1	while ((inputLine = in.readLine()) != null) {
2		analyser.analyse(inputLine);
3	}
```

`BufferedReader in` is created from the output of the `clientSocket.getInputStream()` method. The execution is suspended on the line 1 until any input comes from the client. Then the input is passed to the instance of the `Analyser` class.

#### Analyser

The class is created to obey separation of concerns principle. It is responsible for determining if a message is positive and thus contains all business logic required for such classification. The `Analyser` class works in the same thread as `NewsReceiver` devoted to a certain client. When a message is regarded positive it is passed to the instance of the `Reporter` class.

#### Reporter

The object of this class is the same for all client threads and works in the thread where it was created - in the same thread as instance of the `NewsAnalyserServer` class. It collects messages from all client socket threads into a common collection.

The `Reporter` also periodically prints reports regarding received news. When an instance of the class is created, the `Timer` instance is also created and the `ReporterTask` (internal class of the `Reporter`) is scheduled to run with a configured period. Its `run()` method works periodically in a separate timer thread. The method prints a news digest using the `ConsolePrinter` class and then clears all accumulated news messages.

The class is thread-safe and thus adding messages from different threads and deleting them during the reporting does not create race conditions.

#### ConsolePrinter

This class is also created to maintain separation of concerns. It contains all business rules regarding printing the news reports and their formatting. It also depends on an output media (console in this case).

#### Configuration

This is a utility class to read the `application.properties` file and return configuration properties as typed values.

#### NewsMessage

This is a utility class to conveniently handle received string (JSON format) messages as objects.

### News Feed

This application resides in the `news-feed` folder, or in the `news-feed` module of the IntelliJ project.

#### NewsFeeder

When the application is started, an instance of the `NewsFeeder` class is created. In the constructor, an instance of the `NewsGenerator` is created. Then `feedNews()` method of the `NewsFeeder` is called. The method makes a TCP connection to the news analysing server at the known IP address and the port and starts to generate news messages periodically. For this purpose, it uses the instance of the `NewsGenerator`. When a message is generated, it is immediately sent to the server.

#### NewsGenerator

It makes a pause for the configured time, then creates a message. The priority and the headline are generated randomly. For the message priority, `WeightedRandomGenerator` is used which produces priority values with the avarage frequency corresponding to the priority's weight. For the headline, the random word count is used from the minimal to maximal count value. The words are randomly chosen from the given set of words.

#### WeightedRandomGenerator

Produces random integer numbers with the frequency distribution according to specified weights of the numbers.

#### Configuration

This is a utility class to read the `application.properties` file and return configuration properties as typed values.

#### NewsMessage

This is a utility class to conveniently handle news messages and then convert them into a JSON string to be sent to the server.
