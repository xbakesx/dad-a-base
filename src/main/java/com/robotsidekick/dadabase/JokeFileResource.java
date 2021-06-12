package com.robotsidekick.dadabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class JokeFileResource {

    private static final Logger logger = LoggerFactory.getLogger(JokeFileResource.class);

    private HashMap<String, List<Joke>> taggedJokes;
    private List<Joke> jokes;

    @Autowired
    public JokeFileResource(final ResourceLoader resourceLoader) {

        taggedJokes = new HashMap<>();
        jokes = new ArrayList<>();

        try (final BufferedReader reader =
                     new BufferedReader(new InputStreamReader(resourceLoader.getResource("classpath:jokes.txt").getInputStream()))) {
            while (reader.ready()) {
                final String line = reader.readLine();
                if (line.trim().length() == 0) {
                    continue;
                }
                final Joke joke = new Joke();
                final StringBuilder jokeBuilder = new StringBuilder();
                for (String word : line.split(" ")) {
                    if (word.startsWith("#")) {
                        joke.addTag(word);
                    }
                    else if (word.trim().length() > 0){
                        jokeBuilder.append(word).append(" ");
                    }
                }
                joke.setJoke(jokeBuilder.toString());
                jokes.add(joke);
                for (final String tag : joke.getTags()) {
                    final List<Joke> jokesForTag = taggedJokes.getOrDefault(tag, new ArrayList<>());
                    jokesForTag.add(joke);
                    taggedJokes.put(tag, jokesForTag);
                }
            }
        }
        catch (final IOException e) {
            logger.error("Failed to read the joke file: {}", e.getMessage(), e);
        }

        Collections.sort(jokes);  // Sort so we have a consistent ordering for the daily endpoint

        taggedJokes.entrySet().stream()
                   .filter(entry -> entry.getValue().size() == 1)
                   .forEach(entry -> logger.info("{} has only one joke: {}", entry.getKey(), entry.getValue().get(0).getJoke()));
        for (final String tag : taggedJokes.keySet()) {
            // Sort each tagged list so we have a consistent ordering for the daily endpoint
            final List<Joke> jokesWithinTag = taggedJokes.get(tag);
            Collections.sort(jokesWithinTag);
            taggedJokes.put(tag, jokesWithinTag);
        }
        jokes.stream()
             .filter(joke -> joke.getTags().size() == 0)
             .forEach(joke -> logger.info("Joke with no tags: {}", joke.getJoke()));
    }

    public HashMap<String, List<Joke>> getTaggedJokes() {
        return taggedJokes;
    }

    public List<Joke> getJokes() {
        return jokes;
    }
}
