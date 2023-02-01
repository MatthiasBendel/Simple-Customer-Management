package com.example.application;

import java.util.ArrayList;

public class ExampleData {

    public static ArrayList<Person> getExampleData() {
        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person("Nicolaus Copernicus", "Mainstreet 1", "a-test@mail.com", "01.03.1971"));
        people.add(new Person("Galileo Galilei", "Secondstreet 2", "b-test@mail.com", "01.04.1972"));
        people.add(new Person("Johannes Kepler", "Thirdstreet 3", "c-test@mail.com", "01.05.1973"));
        addContent(people);
        try (var app = new DriverIntroductionExample()) {
            people.addAll(app.findPersons());
        }
        return people;
    }

    private static void addContent(ArrayList<Person> people) {
        for (Person person: people) {
            person.addContent("I'm an Engineer");
        }
    }
}
