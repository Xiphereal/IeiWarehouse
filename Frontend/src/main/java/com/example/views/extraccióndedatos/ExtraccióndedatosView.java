package com.example.views.extraccióndedatos;

import com.example.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "hello-world", layout = MainView.class)
@PageTitle("Extracción de datos")
@CssImport("./styles/views/extraccióndedatos/extraccióndedatos-view.css")
public class ExtraccióndedatosView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public ExtraccióndedatosView() {
        setId("extraccióndedatos-view");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        add(name, sayHello);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }

}
