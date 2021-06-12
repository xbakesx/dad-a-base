package com.robotsidekick.dadabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class JokeController {

    private final Logger logger = LoggerFactory.getLogger(JokeController.class);

    private HashMap<String, List<Joke>> taggedJokes;
    private List<Joke> allJokes;

    @Autowired
    public JokeController(final JokeFileResource jokeFileResource) {
        taggedJokes = jokeFileResource.getTaggedJokes();
        allJokes = jokeFileResource.getJokes();
    }

    @GetMapping
    public String getRandomJoke() {
        return allJokes.get(new Random().nextInt(allJokes.size())).getJoke();
    }

    @GetMapping("/random")
    public Joke getRandomJokeWithMetadata() {
        return allJokes.get(new Random().nextInt(allJokes.size()));
    }

    @GetMapping("/daily")
    public Joke getTodaysJoke() {
        final int dayOfYear = new GregorianCalendar().get(Calendar.DAY_OF_YEAR);
        return allJokes.get(dayOfYear % allJokes.size());
    }

    @GetMapping("/tag/{tag}")
    public String getRandomJokeWithinTag(@PathVariable("tag") final String tag) {
        final List<Joke> jokes = taggedJokes.get(tag);
        return jokes.get(new Random().nextInt(jokes.size())).getJoke();
    }

    @GetMapping("/tag/{tag}/random")
    public Joke getRandomJokeWithinTagWithMetadata(@PathVariable("tag") final String tag) {
        final List<Joke> jokes = taggedJokes.get(tag);
        return jokes.get(new Random().nextInt(jokes.size()));
    }

    @GetMapping("/tag/{tag}/daily")
    public Joke getTodaysJokeWithinTag(@PathVariable("tag") final String tag) {
        final int dayOfYear = new GregorianCalendar().get(Calendar.DAY_OF_YEAR);
        final List<Joke> jokes = taggedJokes.get(tag);
        return jokes.get(dayOfYear % jokes.size());
    }
}
