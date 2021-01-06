package frontendWebApp.views.dataExtraction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import domainModel.requestResponses.RequestStatusResponse;
import frontendWebApp.views.main.MainView;
import utils.HttpService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Route(value = "data-extraction", layout = MainView.class)
@PageTitle("Extracción de datos")
@CssImport("./styles/views/dataExtraction/dataextraction-view.css")
public class DataExtractionView extends HorizontalLayout {
    private TextField startYear;
    private TextField endYear;

    private boolean extractFromDblp = true;
    private boolean extractFromIeee = true;
    private boolean extractFromGoogleScholar = true;

    public DataExtractionView() {
        setId("dataextraction-view");

        addComponentsToView();
    }

    private void addComponentsToView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setAlignSelf(Alignment.CENTER);

        verticalLayout.add(new H1("Extracción de referencias bibliográficas IEI"));

        addExtractorsCheckboxOptionsTo(verticalLayout);

        addYearRangeTextFieldsTo(verticalLayout);

        addButtonsTo(verticalLayout);

        add(verticalLayout);
    }

    private void addExtractorsCheckboxOptionsTo(VerticalLayout verticalLayout) {
        final String dblpOptionName = "DBLP";
        final String ieeeOptionName = "IEEE";
        final String googleScholarOptionName = "Google Scholar";

        CheckboxGroup<String> extractorsOptions = new CheckboxGroup<>();
        extractorsOptions.setLabel("Extractores a utilizar");
        extractorsOptions.setItems(dblpOptionName, ieeeOptionName, googleScholarOptionName);

        Set<String> values = new HashSet<>();
        values.add(dblpOptionName);
        values.add(ieeeOptionName);
        values.add(googleScholarOptionName);
        extractorsOptions.setValue(values);

        extractorsOptions.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        extractorsOptions.addSelectionListener(selection -> {
            Set<String> selectedOptionsNames = selection.getValue();

            extractFromDblp = selectedOptionsNames.contains(dblpOptionName);
            extractFromIeee = selectedOptionsNames.contains(ieeeOptionName);
            extractFromGoogleScholar = selectedOptionsNames.contains(googleScholarOptionName);
        });

        verticalLayout.add(extractorsOptions);
    }

    private void addYearRangeTextFieldsTo(VerticalLayout verticalLayout) {
        startYear = new TextField("Desde año");
        endYear = new TextField("Hasta año");

        HorizontalLayout yearRangeLayout = new HorizontalLayout();
        yearRangeLayout.add(startYear, endYear);

        verticalLayout.add(yearRangeLayout);
    }

    private void addButtonsTo(VerticalLayout verticalLayout) {
        Button loadButton = new Button("Cargar");
        loadButton.addClickListener(e -> commandDataExtractionToWarehouse());

        Button clearFiltersButton = new Button("Limpiar filtros");
        clearFiltersButton.addClickListener(e -> clearAllFilters());

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(loadButton, clearFiltersButton);

        verticalLayout.add(buttonsLayout);
    }

    private void commandDataExtractionToWarehouse() {
        String request = buildRequest();
        String dataWarehouseLocation = "localhost:8081";
        String jsonResponse = HttpService.executeGet("http://" + dataWarehouseLocation + request);

        if (jsonResponse == null)
            System.err.println("An error has occurred while establishing the connection to the " +
                    "data Warehouse. The JSON response is null.");

        RequestStatusResponse requestResult = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            requestResult = objectMapper.readValue(jsonResponse, RequestStatusResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (requestResult != null) {
            if (requestResult.getMessage().contains("OK"))
                Notification.show("Extrayendo referencias bibliográficas a la Warehouse. " +
                        "Los resultados estarán listos en la Warehouse en unos minutos.");
            else
                Notification.show(requestResult.getMessage());

        } else {
            Notification.show("Opss! Ha ocurrido un error al intentar conectar con la Warehouse.");
        }
    }

    private String buildRequest() {
        String request = "/extractData";

        // Use for checking whether to add '?' as first parameter, or '&' for chaining the consequent.
        boolean firstParameter = true;

        if (!extractFromDblp) {
            firstParameter = false;
            request += "?extractFromDBLP=false";
        }

        if (!extractFromIeee) {
            if (firstParameter) {
                request += '?';
                firstParameter = false;
            } else
                request += '&';

            request += "extractFromIEEE=false";
        }

        if (!extractFromGoogleScholar) {
            if (firstParameter) {
                request += '?';
                firstParameter = false;
            } else
                request += '&';

            request += "extractFromGoogleScholar=false";
        }

        if (!startYear.isEmpty()) {
            if (firstParameter) {
                request += '?';
                firstParameter = false;
            } else
                request += '&';

            request += "startYear=" + startYear.getValue();
        }

        if (!endYear.isEmpty()) {
            if (firstParameter)
                request += '?';
            else
                request += '&';

            request += "endYear=" + endYear.getValue();
        }

        // Debug.
        Notification.show("DEBUG: The REST URI was " + request, 10000, Notification.Position.TOP_END);

        return request;
    }

    private void clearAllFilters() {
        startYear.clear();
        endYear.clear();
    }
}
