package com.example.application;

public class Person {
    protected String name;
    protected String address;
    protected String mail;
    protected String birth;
    public Person(String name, String address, String mail, String birth) {
        this.name = name;
        this.address = address;
        this.mail = mail;
        this.birth = birth;
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

    public String getBirth(){
        return this.birth;
    }
}
