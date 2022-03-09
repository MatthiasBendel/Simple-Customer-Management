package com.example.application.views.main;

import com.example.application.Person;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@PageTitle("Kundenverwaltung")
@Route(value = "")
public class MainView extends VerticalLayout {

    private final Grid<Person> grid;
    private final ArrayList<Person> people;

    private final String nameText = "Name";
    private final String addressText = "Adresse";
    private final String mailText = "E-Mailadresse";
    private final String birthText = "Geburtsdatum";


    public MainView() {
        add(new H2("Kundenverwaltung"));

        people = getExampleData();

        grid = new Grid<>();
        grid.setItems(people);
        grid.addColumn(Person::getName).setHeader(nameText);
        grid.addColumn(Person::getAddress).setHeader(addressText);
        grid.addColumn(Person::getMail).setHeader(mailText);
        grid.addColumn(Person::getBirth).setHeader(birthText);
        add(grid);

        Button deleteButton = new Button("Entfernen", e -> createRemoveDialog().open());
        deleteButton.setEnabled(false);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> deleteButton.setEnabled(selection.getAllSelectedItems().size() != 0));

        Button addButton = new Button("Hinzufügen", e -> createAddDialog().open());

        HorizontalLayout footer = new HorizontalLayout(deleteButton, addButton);
        add(footer);
    }

    private ArrayList<Person> getExampleData() {
        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person("Nicolaus Copernicus", "Mainstreet 1", "a-test@mail.com", "01.03.1971"));
        people.add(new Person("Galileo Galilei", "Secondstreet 2", "b-test@mail.com", "01.04.1972"));
        people.add(new Person("Johannes Kepler", "Thirdstreet 3", "c-test@mail.com", "01.05.1973"));
        return people;
    }

    private Dialog createAddDialog() {
        VerticalLayout dialogLayout = new VerticalLayout();
        Button confirmButton = new Button("Hinzufügen");
        Dialog dialog = createDialog("Eintrag hinzufügen", dialogLayout, confirmButton);

        TextField nameField = new TextField(nameText);
        TextField addressField = new TextField(addressText);
        EmailField mailField = new EmailField(mailText);
        mailField.setErrorMessage("Bitte geben Sie eine gültige E-Mailadresse ein.");
        DatePicker birthField = new DatePicker(birthText);
        dialogLayout.add(nameField);
        dialogLayout.add(addressField);
        dialogLayout.add(mailField);
        dialogLayout.add(birthField);

        confirmButton.addClickListener(e -> {
            String birthFieldValue = birthField.getValue() == null ? "" :
                    birthField.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            Person newPerson = new Person(nameField.getValue(), addressField.getValue(), mailField.getValue(),
                    birthFieldValue);
            people.add(newPerson);
            grid.getDataProvider().refreshAll();
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return dialog;
    }

    private Dialog createRemoveDialog() {
        VerticalLayout dialogLayout = new VerticalLayout();
        Button confirmButton = new Button("Entfernen");
        Dialog dialog = createDialog("Eintrag entfernen", dialogLayout, confirmButton);

        dialogLayout.add("Sind Sie sicher, dass der Eintrag entfernt werden soll?");

        confirmButton.addClickListener(e -> {
            people.removeAll(grid.getSelectedItems());
            grid.getDataProvider().refreshAll();
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return dialog;
    }

    private Dialog createDialog(String dialogHeadline, VerticalLayout innerLayout, Button confirmButton) {
        Dialog dialog = new Dialog();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button cancelButton = new Button("Abbrechen", e -> dialog.close());
        buttonLayout.add(cancelButton, confirmButton);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(new H3(dialogHeadline));
        dialogLayout.add(innerLayout);
        dialogLayout.add(buttonLayout);

        dialog.add(dialogLayout);
        return dialog;
    }
}
