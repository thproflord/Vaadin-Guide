package com.vaadin.tutorial.crm.views.projects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.tutorial.crm.views.list.ContactForm;
import com.vaadin.tutorial.crm.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;

@CssImport("./views/helloworld/hello-world-view.css")
@Route(value = "hello", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Proyectos")
public class Projects extends HorizontalLayout {


    public Projects() {
        addClassName("hello-world-view");
        Dialog dialog = new Dialog();
        ContactForm contactForm = new ContactForm(null);
        dialog.add(contactForm);
        dialog.setWidth("500px");
        dialog.setHeight("auto");
        add(dialog);
        dialog.open();
    }

}
