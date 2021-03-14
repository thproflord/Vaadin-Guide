package com.vaadin.tutorial.crm.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.backend.entity.Company;
import com.vaadin.tutorial.crm.backend.entity.Contact;
import com.vaadin.tutorial.crm.backend.service.CompanyService;
import com.vaadin.tutorial.crm.backend.service.ContactService;
import com.vaadin.tutorial.crm.views.main.MainView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Route(value = "contacts", layout = MainView.class)
@PageTitle("Contacts")
@Scope("prototype")
@Component
public class ListView extends VerticalLayout {

    //Components
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filter = new TextField("Filter by name");

    // Services
    private ContactService contactService;
    private CompanyService companyService;

    ContactForm contactForm;

    public ListView(ContactService contactService, CompanyService companyService) {
        this.contactService = contactService;
        configureGrid();
        getToolbar();
        contactForm = new ContactForm(companyService.findAll());

        closeEditor();
        contactForm.addListener(ContactForm.SaveEvent.class, this::saveContact);
        contactForm.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        contactForm.addListener(ContactForm.CloseEvent.class, e -> closeEditor());

        addClassName("list-view");
        setSizeFull();

        Div content = new Div();
        content.add(grid, contactForm);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolbar(), content );

        updateList();
    }

    private void configureGrid() {
        grid.removeColumnByKey("company");
        grid.setColumns("firstName", "lastName", "email", "status");
        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return  company == null ? "---" : company.getName();
        }).setHeader("Company");
        
        grid.setSizeFull();
        grid.addClassName("contact-grid");
        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));

    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private HorizontalLayout getToolbar() {
        filter.setClearButtonVisible(true);
        filter.addThemeVariants(TextFieldVariant.LUMO_HELPER_ABOVE_FIELD);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("New Contact");
        addContactButton.addClickListener(click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filter, addContactButton);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void updateList() {
        grid.setItems(contactService.findAll(filter.getValue()));
    }

    private void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            contactForm.setContact(contact);
            contactForm.setVisible(true);
            contactForm.addClassName("editing");
        }
    }

    private void closeEditor() {
        contactForm.setContact(null);
        contactForm.setVisible(false);
        removeClassName("editing");
    }

    private void saveContact(ContactForm.SaveEvent event) {
        contactService.save(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        contactService.delete(event.getContact());
        updateList();
        closeEditor();
    }



}
