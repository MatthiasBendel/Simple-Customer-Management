package com.example.application.views.main;

import com.example.application.ExampleData;
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

        people = ExampleData.getExampleData();

        grid = buildGrid();
        buildFooterButtons();
    }

    private Grid<Person> buildGrid() {
        Grid<Person> grid = new Grid<>();
        grid.setItems(people);
        grid.addColumn(Person::getName).setHeader(nameText);
        grid.addColumn(Person::getAddress).setHeader(addressText);
        grid.addColumn(Person::getMail).setHeader(mailText);
        grid.addColumn(Person::getBirth).setHeader(birthText);
        add(grid);
        return grid;
    }

    private void buildFooterButtons() {
        Button deleteButton = new Button("Entfernen", e -> createRemovePersonDialog().open());
        deleteButton.setEnabled(false);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selection -> deleteButton.setEnabled(selection.getAllSelectedItems().size() != 0));

        Button editButton = new Button("Bearbeiten", e -> createEditPersonDialog().open());
        grid.addSelectionListener(selection -> editButton.setEnabled(selection.getAllSelectedItems().size() == 1));

        Button addButton = new Button("Hinzufügen", e -> createAddPersonDialog().open());

        HorizontalLayout footer = new HorizontalLayout(deleteButton, editButton, addButton);
        add(footer);
    }

    private Dialog createAddPersonDialog() {
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

    private Dialog createRemovePersonDialog() {
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

    private Dialog createEditPersonDialog() {
        VerticalLayout dialogLayout = new VerticalLayout();
        Button confirmButton = new Button("Bearbeiten");
        Dialog dialog = createDialog("Eintrag bearbeiten", dialogLayout, confirmButton);

        if (grid.getSelectedItems().size() != 1) {
            confirmButton.setText("Okay");
            dialogLayout.add("Bitte wählen Sie nur ein Element zum bearbeiten aus.");
            confirmButton.addClickListener(e -> dialog.close());
        } else {
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
        }
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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
