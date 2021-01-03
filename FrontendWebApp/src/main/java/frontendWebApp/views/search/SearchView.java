package frontendWebApp.views.search;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import frontendWebApp.views.main.MainView;

@Route(value = "search", layout = MainView.class)
@PageTitle("Búsqueda")
@CssImport("./styles/views/search/search-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class SearchView extends HorizontalLayout {
    private TextField author;
    private TextField publicationTitle;
    private TextField startYear;
    private TextField endYear;

    public SearchView() {
        setId("search-view");

        addComponentsToView();
    }

    private void addComponentsToView() {
        H1 pageTitle = new H1("Búsqueda bibliográfica IEI");

        author = new TextField("Autor");
        publicationTitle = new TextField("Título");
        startYear = new TextField("Desde año");
        endYear = new TextField("Hasta año");

        Button searchButton = new Button("Buscar");
        Button clearFiltersButton = new Button("Limpiar filtros");

        HorizontalLayout yearRangeLayout = new HorizontalLayout();
        yearRangeLayout.add(startYear, endYear);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(searchButton, clearFiltersButton);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(pageTitle, author, publicationTitle, yearRangeLayout, buttonsLayout);
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setAlignSelf(Alignment.CENTER);

        add(verticalLayout);

        // Add components listeners.
        searchButton.addClickListener(e -> Notification.show("Buscando referencias bibligráficas..."));
        clearFiltersButton.addClickListener(e -> clearAllFilters());
    }

    private void clearAllFilters() {
        author.clear();
        publicationTitle.clear();
        startYear.clear();
        endYear.clear();
    }
}
