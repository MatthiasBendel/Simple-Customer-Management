package com.example.application.views.main;

import com.example.application.Person;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.grid.Grid;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@PageTitle("Kundenverwaltung")
@Route(value = "")
public class MainView extends VerticalLayout {

    public MainView() {

        TextField nameField = new TextField("Name");
        TextField addressField = new TextField("Adresse");
        TextField mailField = new TextField("E-Mail Addresse");
        TextField birthField = new TextField("Geburtsdatum");
        Button addButton = new Button("Add");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(nameField);
        horizontalLayout.add(addressField);
        horizontalLayout.add(mailField);
        horizontalLayout.add(birthField);
        horizontalLayout.add(addButton);

        add(horizontalLayout);

        // example data
        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person("Nicolaus Copernicus", "Mainstreet 1", "a-test@mail.com", "01.03.1971"));
        people.add(new Person("Galileo Galilei", "Secondstreet 2", "b-test@mail.com", "01.04.1972"));
        people.add(new Person("Johannes Kepler", "Secondstreet 3", "c-test@mail.com", "01.05.1973"));

        // show data in grid
        Grid<Person> grid = new Grid<>();
        grid.setItems(people);
        grid.addColumn(Person::getName).setHeader("Name");
        grid.addColumn(Person::getAddress).setHeader("Address");
        grid.addColumn(Person::getMail).setHeader("Mail");
        grid.addColumn(Person::getBirth).setHeader("Birth");
        grid.addComponentColumn(item -> {
            Icon icon = new Icon("lumo", "cross");
            icon.addClickListener(e -> {
                people.remove(item);
                grid.getDataProvider().refreshAll();
            });
            return icon;
        });
        add(grid);

        addButton.addClickListener(e -> {
            Person newPerson = new Person(nameField.getValue(), addressField.getValue(), mailField.getValue(),
                    birthField.getValue());
            people.add(newPerson);
            grid.getDataProvider().refreshAll();
            //cleanup fields
            nameField.setValue("");
            addressField.setValue("");
            mailField.setValue("");
            birthField.setValue("");
        });
    }
}
