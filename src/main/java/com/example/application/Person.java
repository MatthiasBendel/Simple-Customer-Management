package com.example.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Person {
    protected String name;
    protected String address;
    protected String mail;
    protected LocalDate birth;
    public ArrayList<Content> contents = new ArrayList<>();

    public Person(String name, String address, String mail, String birth) {
        this.name = name;
        this.address = address;
        this.mail = mail;
        this.birth = LocalDate.parse(birth, DateTimeFormatter.ofPattern("dd.MM.yyyy")); //birth == null ? "" : birth.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getName(){
        return this.name;
    }

    public String getAddress(){
        return this.address;
    }

    public String getMail(){
        return this.mail;
    }

    public LocalDate getBirth(){
        return this.birth;
    }

    public void update(String name, String address, String mail, LocalDate birth) {
        this.name = name;
        this.address = address;
        this.mail = mail;
        this.birth = birth;
    }

    public void addContent(String contentDescription) {
        contents.add(new Content(this, contentDescription));
    }
}
