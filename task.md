# Task description

The task involves the development of two separate Java SE 8 programs: a "mock news feed" and a "news analyser". Several instances of the mock news feed will be run simultaneously, each connecting to the same news analyser.

The mock news feed should periodically generate messages containing random news item.

The news analyser should handle the messages from the news feeds and periodically display a short summary about news items that are considered "interesting“.

## Mock news feed
* Creates a persistent TCP connection to the news analyser and periodically sends news items over that connection. Each news item should be comprised of a headline and a priority.
* The headline of a news item should be a random combination of three to five words (random number from three to five) from the following list: up, down, rise, fall, good, bad, success, failure, high, low, über, unter.
* The priority of a news item should be an integer within the range [0..9] (0 is the lowest and 9 is the highest priority). News messages with higher priority should be generated with less probability than those with lower priority. The probability of the priorities from 0 to 9 should be in percent: 29.3, 19.3, 14.3, 10.9, 8.4, 6.5, 4.8, 3.4, 2.1, 1.
* The frequency of news items being emitted by the feed should be configurable via a Java property separately for each news feed instance.

## News analyser
* Listens on a TCP port for connections from mock news feeds and receives news item messages.
* Inspects the news item headlines and decides whether they are overall positive or negative. If more than 50% of words in the headline are positive ("up", "rise", "good", "success", "high" or "über"), the news item as a whole is considered positive. Negative news items are ignored by the analyser.
* Every 10 seconds, the news analyser should output to the console:
  * the count of positive news items seen during the last 10 seconds,
  * the unique headlines of up to three of the highest-priority positive news items seen during the last 10 seconds.
