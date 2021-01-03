package frontendWebApp.views.dataExtraction;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import frontendWebApp.views.main.MainView;

import java.util.HashSet;
import java.util.Set;

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

        addExtractorsCheckboxOptions();

        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });
    }

    private void addExtractorsCheckboxOptions() {
        CheckboxGroup<String> extractorsOptions = new CheckboxGroup<>();
        extractorsOptions.setLabel("Extractores a utilizar");
        extractorsOptions.setItems("DBLP", "IEEE", "Google Scholar");

        Set<String> values = new HashSet<>();
        values.add("DBLP");
        values.add("IEEE");
        values.add("Google Scholar");
        extractorsOptions.setValue(values);

        extractorsOptions.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        add(extractorsOptions);
    }
}
