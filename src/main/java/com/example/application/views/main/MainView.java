package com.example.application.views.main;

import com.example.application.Content;
import com.example.application.DriverIntroductionExample;
import com.example.application.ExampleData;
import com.example.application.Person;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


@PageTitle("Personenverwaltung")
@Route(value = "")
public class MainView extends VerticalLayout {

    private H2 headline;
    private Grid<Person> personGrid;
    private Grid<Content> contentGrid;
    private HorizontalLayout footer;
    private ArrayList<Person> people;

    private final String nameText = "Name";
    private final String addressText = "Adresse";
    private final String mailText = "E-Mailadresse";
    private final String birthText = "Geburtsdatum";
    private String creatorText = "Ersteller";
    private String descriptionText = "Beschreibung";
    private String recommendationText = "Empfehlung";
    private String acceptedText = "Akzeptiert";
    private String acknowledgedText = "Zur Kenntnis genommen";
    private String ignoredText = "Ignoriert";
    private String declinedText = "Abgelehnt";

    public MainView() {
        this.getElement().getThemeList().add(Lumo.DARK);
        add(createMenuBar());
        people = ExampleData.getExampleData();
        //showContentsGrid();
        showBlog();
    }

    private void showBlog() {

        headline = new H2("My Blog");
        add(headline);
        MessageList list = new MessageList();

        Instant yesterday = LocalDateTime.now().minusDays(1)
                .toInstant(ZoneOffset.UTC);
        ArrayList<MessageListItem> messageListItems = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            for (int j = 0; j < person.contents.size(); j++) {
                MessageListItem newMessage = new MessageListItem(person.contents.get(j).description(), yesterday,
                        person.getName());
                newMessage.setUserColorIndex(i);
                messageListItems.add(newMessage);
            }
        }
        list.setItems(messageListItems);
        add(list);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Text selected = new Text("");
        ComponentEventListener<ClickEvent<MenuItem>> listener = e -> {
            selected.setText(e.getSource().getText());
            if ("Persons".equals(selected.getText())) {
                if (contentGrid != null)
                    hideContentGrid();
                showPersonsGrid();
            } else if ("Contents".equals(selected.getText())) {
                if (personGrid != null)
                    hidePersonGrid();
                showContentsGrid();
            }
        };

        MenuItem view = menuBar.addItem("View", listener);
        SubMenu viewSubMenu = view.getSubMenu();
        viewSubMenu.addItem("Persons", listener);
        viewSubMenu.addItem("Contents", listener);
//        menuBar.addItem("Edit", listener);

//        MenuItem share = menuBar.addItem("Share");
//        SubMenu shareSubMenu = share.getSubMenu();
//        MenuItem onSocialMedia = shareSubMenu.addItem("On social media");
//        SubMenu socialMediaSubMenu = onSocialMedia.getSubMenu();
//        socialMediaSubMenu.addItem("Facebook", listener);
//        socialMediaSubMenu.addItem("Twitter", listener);
//        socialMediaSubMenu.addItem("Instagram", listener);
//        shareSubMenu.addItem("By email", listener);
//        shareSubMenu.addItem("Get Link", listener);
        return menuBar;
    }

    private void showPersonsGrid() {
        headline = new H2("Personenverwaltung");
        add(headline);
        personGrid = buildPersonsGrid();
        buildPersonFooterButtons();
    }

    private void hidePersonGrid() {
        remove(headline);
        remove(personGrid);
        remove(footer);
    }

    private void hideContentGrid() {
        remove(headline);
        remove(contentGrid);
        remove(footer);
    }

    private void showContentsGrid() {
        headline = new H2("Inhalte");
        add(headline);
        contentGrid = buildContentsGrid();
        buildContentsFooterButtons();
    }

    private Grid<Person> buildPersonsGrid() {
        Grid<Person> grid = new Grid<>();
        grid.setItems(people);
        grid.addColumn(Person::getName).setHeader(nameText);
        grid.addColumn(Person::getAddress).setHeader(addressText);
        grid.addColumn(Person::getMail).setHeader(mailText);
        grid.addColumn(Person::getBirth).setHeader(birthText);
        add(grid);
        return grid;
    }

    private Grid<Content> buildContentsGrid() {
        Grid<Content> grid = new Grid<>();
        grid.setItems(getContents());
        grid.addColumn(Content::creator).setHeader(creatorText);
        grid.addColumn(Content::description).setHeader(descriptionText);
        grid.addColumn(Content::recommendation).setHeader(recommendationText);
        grid.addColumn(Content::accepted).setHeader(acceptedText);
        grid.addColumn(Content::acknowledged).setHeader(acknowledgedText);
        grid.addColumn(Content::ignored).setHeader(ignoredText);
        grid.addColumn(Content::declined).setHeader(declinedText);
        add(grid);
        return grid;
    }

    private ArrayList<Content> getContents() {
        ArrayList<Content> contents = new ArrayList<>();
        for (Person person: people) {
            contents.addAll(person.contents);
        }
        return contents;
    }

    private void buildPersonFooterButtons() {
        personGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        Button deleteButton = new Button("Entfernen", e -> createRemovePersonDialog().open());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setEnabled(false);
        personGrid.addSelectionListener(selection -> deleteButton.setEnabled(selection.getAllSelectedItems().size() != 0));

        Button editButton = new Button("Bearbeiten", e -> createEditPersonDialog().open());
        editButton.setEnabled(false);
        personGrid.addSelectionListener(selection -> editButton.setEnabled(selection.getAllSelectedItems().size() == 1));

        Button addButton = new Button("Hinzufügen", e -> createAddPersonDialog().open());

        footer = new HorizontalLayout(deleteButton, editButton, addButton);
        add(footer);
    }

    private void buildContentsFooterButtons() {
        contentGrid.setSelectionMode(Grid.SelectionMode.MULTI);

        Button deleteButton = new Button("Entfernen", e -> createRemovePersonDialog().open());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setEnabled(false);
        contentGrid.addSelectionListener(selection -> deleteButton.setEnabled(selection.getAllSelectedItems().size() != 0));

        Button editButton = new Button("Bearbeiten", e -> createEditPersonDialog().open());
        editButton.setEnabled(false);
        contentGrid.addSelectionListener(selection -> editButton.setEnabled(selection.getAllSelectedItems().size() == 1));

        Button addButton = new Button("Hinzufügen", e -> createAddContentDialog().open());

        footer = new HorizontalLayout(deleteButton, editButton, addButton);
        add(footer);
    }

    private Dialog createAddContentDialog() {VerticalLayout dialogLayout = new VerticalLayout();
        Button confirmButton = new Button("Hinzufügen");
        Dialog dialog = createDialog("Eintrag hinzufügen", dialogLayout, confirmButton);

        TextField contentField = new TextField(descriptionText);
        dialogLayout.add(contentField);

        confirmButton.addClickListener(e -> {
            Content newContent = new Content(people.get(0), contentField.getValue());
            try (var app = new DriverIntroductionExample()) {
                app.createContent(newContent);
            }
            //contentGrid.getDataProvider().refreshAll();
            hideContentGrid();
            showContentsGrid();
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return dialog;
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
            try (var app = new DriverIntroductionExample()) {
                app.createPerson(newPerson);
            }
            people.add(newPerson);
            personGrid.getDataProvider().refreshAll();
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
            people.removeAll(personGrid.getSelectedItems());
            personGrid.getDataProvider().refreshAll();
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return dialog;
    }

    private Dialog createEditPersonDialog() {
        VerticalLayout dialogLayout = new VerticalLayout();
        Button confirmButton = new Button("Bearbeiten");
        Dialog dialog = createDialog("Eintrag bearbeiten", dialogLayout, confirmButton);

        if (personGrid.getSelectedItems().size() != 1) {  //should not be possible anymore!
            confirmButton.setText("Okay");
            dialogLayout.add("Bitte wählen Sie nur ein Element zum bearbeiten aus.");
            confirmButton.addClickListener(e -> dialog.close());
        } else {
            Person selectedItem = (Person) personGrid.getSelectedItems().toArray()[0];
            TextField nameField = new TextField(nameText);
            nameField.setValue(selectedItem.getName());
            TextField addressField = new TextField(addressText);
            addressField.setValue(selectedItem.getAddress());
            EmailField mailField = new EmailField(mailText);
            mailField.setValue(selectedItem.getMail());
            mailField.setErrorMessage("Bitte geben Sie eine gültige E-Mailadresse ein.");
            DatePicker birthField = new DatePicker(birthText);
            birthField.setValue(selectedItem.getBirth());
            dialogLayout.add(nameField);
            dialogLayout.add(addressField);
            dialogLayout.add(mailField);
            dialogLayout.add(birthField);

            confirmButton.addClickListener(e -> {
                selectedItem.update(nameField.getValue(), addressField.getValue(), mailField.getValue(),
                        birthField.getValue());
                personGrid.getDataProvider().refreshAll();
                personGrid.deselect(selectedItem);
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
