package com.example.application.views.main;

import com.example.application.Person;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
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

    private final Grid<Person> grid;
    private final ArrayList<Person> people;

    public MainView() {

        H2 users = new H2("Kundenverwaltung");
        HorizontalLayout header = new HorizontalLayout(users);
        header.setAlignItems(Alignment.CENTER);
        header.getThemeList().clear();
        add(header);


//        // example data
        people = new ArrayList<>();
        people.add(new Person("Nicolaus Copernicus", "Mainstreet 1", "a-test@mail.com", "01.03.1971"));
        people.add(new Person("Galileo Galilei", "Secondstreet 2", "b-test@mail.com", "01.04.1972"));
        people.add(new Person("Johannes Kepler", "Secondstreet 3", "c-test@mail.com", "01.05.1973"));

        // show data in grid
        grid = new Grid<>();
        grid.setItems(people);
        grid.addColumn(Person::getName).setHeader("Name");
        grid.addColumn(Person::getAddress).setHeader("Address");
        grid.addColumn(Person::getMail).setHeader("Mail");
        grid.addColumn(Person::getBirth).setHeader("Birth");
        add(grid);

        users.getStyle().set("margin", "0 auto 0 0");

        Button deleteButton = new Button("Entfernen", e -> createRemoveDialog().open());
        deleteButton.setEnabled(false);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().set("margin-inline-start", "auto");

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> deleteButton.setEnabled(selection.getAllSelectedItems().size() != 0));

        Button addButton = new Button("Hinzufügen");

        TextField nameField = new TextField("Name");
        TextField addressField = new TextField("Adresse");
        TextField mailField = new TextField("E-Mail Addresse");
        TextField birthField = new TextField("Geburtsdatum");
        Button addCustomerButton = new Button("Kunde hinzufügen");

        HorizontalLayout addCustomerLayout = new HorizontalLayout();
        addCustomerLayout.add(nameField);
        addCustomerLayout.add(addressField);
        addCustomerLayout.add(mailField);
        addCustomerLayout.add(birthField);
        addCustomerLayout.add(addCustomerButton);

        addButton.addClickListener(e -> add(addCustomerLayout));
        HorizontalLayout footer = new HorizontalLayout(deleteButton, addButton);
        footer.getStyle().set("flex-wrap", "wrap");
        add(footer);

        addCustomerButton.addClickListener(e -> {
            Person newPerson = new Person(nameField.getValue(), addressField.getValue(), mailField.getValue(),
                    birthField.getValue());
            people.add(newPerson);
            grid.getDataProvider().refreshAll();
            //cleanup fields
            nameField.setValue("");
            addressField.setValue("");
            mailField.setValue("");
            birthField.setValue("");
            remove(addCustomerLayout);
        });
    }

    private Dialog createRemoveDialog() {
        Dialog dialog = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();
        dialog.add(dialogLayout);
        dialogLayout.add("Sind Sie sicher, dass der Eintrag entfernt werden soll?");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        dialogLayout.add(buttonLayout);
        Button cancelButton = new Button("Abbrechen", e -> dialog.close());
        Button confirmButton = new Button("Entfernen", e -> {
            people.removeAll(grid.getSelectedItems());
            grid.getDataProvider().refreshAll();
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(cancelButton, confirmButton);
        return dialog;
    }

}
