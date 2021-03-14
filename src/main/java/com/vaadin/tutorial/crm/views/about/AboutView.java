package com.vaadin.tutorial.crm.views.about;


import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.tutorial.crm.views.main.MainView;


@CssImport("./views/about/about-view.css")
@Route(value = "about", layout = MainView.class)
@PageTitle("About")
public class AboutView extends Div {

    public AboutView() {
        VaadinSession.getCurrent().getSession().invalidate();
    }

}
