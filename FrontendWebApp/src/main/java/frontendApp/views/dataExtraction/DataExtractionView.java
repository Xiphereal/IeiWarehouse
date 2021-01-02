package frontendApp.views.dataExtraction;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import frontendApp.views.main.MainView;

@Route(value = "data-extraction", layout = MainView.class)
@PageTitle("Extracción de datos")
@CssImport("./styles/views/dataExtraction/dataextraction-view.css")
public class DataExtractionView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public DataExtractionView() {
        setId("dataextraction-view");
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        add(name, sayHello);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }
}
