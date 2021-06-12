package com.robotsidekick.dadabase;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Joke {

    private final UUID id;
    private String joke;
    private Set<String> tags;

    public Joke() {
        id = UUID.randomUUID();
        tags = new HashSet<>();
    }

    public UUID getId() {
        return id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(final String joke) {
        this.joke = joke;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTag(final String tag) {
        if (tag.startsWith("#")) {
            tags.add(tag.substring(1));
        }
        else {
            tags.add(tag);
        }
    }
}
