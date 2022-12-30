package com.example.application;

import java.time.LocalDate;

public class Content {
    protected Person creator;
    protected String description;
    protected double recommendation;
    protected int accepted;
    protected int acknowledged;
    protected int ignored;
    protected int declined;
    protected LocalDate creationDate;

    public Content(Person creator, String description) {
        this.creator = creator;
        this.description = description;
    }

    public Content(Person creator, String description, double recommendation, boolean accepted, boolean acknowledged,
                   boolean ignored, boolean declined) {
        this(creator, description);
        this.recommendation = recommendation;
        if (accepted)
            this.accepted++;
        if (acknowledged)
            this.acknowledged++;
        if (ignored)
            this.ignored++;
        if (declined)
            this.declined++;
    }

    public Person creator() {
        return creator;
    }

    public String description() {
        return description;
    }

    public double recommendation() {
        return recommendation;
    }

    public int accepted() {
        return accepted;
    }

    public int acknowledged() {
        return acknowledged;
    }

    public int ignored() {
        return ignored;
    }

    public int declined() {
        return declined;
    }
}
