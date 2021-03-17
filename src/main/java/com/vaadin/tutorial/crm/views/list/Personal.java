package com.vaadin.tutorial.crm.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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

@Route(value = "personal", layout = MainView.class)
@PageTitle("Personal")
@Scope("prototype")
@Component
public class Personal extends VerticalLayout {

    //Components
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filter = new TextField("Filtrar por nombre");
    Dialog dialog = new Dialog();

    // Services
    private ContactService contactService;
    private CompanyService companyService;

    ContactForm contactForm;

    public Personal(ContactService contactService, CompanyService companyService) {
        this.contactService = contactService;
        configureGrid();
        getToolbar();
        contactForm = new ContactForm(companyService.findAll());

        closeEditor();
        contactForm.addListener(ContactForm.SaveEvent.class, this::saveContact);
        contactForm.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        contactForm.addListener(ContactForm.CloseEvent.class, e -> closeEditor());

        createDialogForm();
        addClassName("list-view");
        setSizeFull();

        Div content = new Div();
        content.add(grid);
        content.addClassName("content");
        content.setSizeFull();

        Button addContactButton = new Button("New Contact");
        addContactButton.addClickListener(click -> addContact());

        add(getToolbar(), content ,addContactButton);

        updateList();
    }

    private void createDialogForm(){
        dialog.add(contactForm);
        dialog.setWidth("500px");
        dialog.setHeight("auto");
        add(dialog);

    }

    private void configureGrid() {
        grid.removeColumnByKey("company");
        grid.setColumns("firstName", "lastName", "email", "status","cargo","tipo","condicion");
        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return  company == null ? "---" : company.getName();
        }).setHeader("Company");
        
        grid.setSizeFull();
        grid.addClassName("contact-grid");
        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));
        grid.setColumnReorderingAllowed(true);
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


        HorizontalLayout toolbar = new HorizontalLayout(filter);

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
            dialog.open();
        }
    }

    private void closeEditor() {
        grid.recalculateColumnWidths();
        contactForm.setContact(null);
        dialog.close();
    }

    private void saveContact(ContactForm.SaveEvent event) {
        contactService.save(event.getContact());
        Notification success = new Notification("Creado Correctamente",3000);
        success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        success.open();
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        contactService.delete(event.getContact());
        Notification deleted = new Notification("Borrado",3000);
        deleted.addThemeVariants(NotificationVariant.LUMO_ERROR);
        deleted.open();
        updateList();
        closeEditor();
    }



}
