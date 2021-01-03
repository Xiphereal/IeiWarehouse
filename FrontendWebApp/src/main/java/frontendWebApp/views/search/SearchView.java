package frontendWebApp.views.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import domainModel.Article;
import domainModel.Book;
import domainModel.CongressCommunication;
import frontendWebApp.RequestResultResponse;
import frontendWebApp.views.main.MainView;
import utils.HttpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route(value = "search", layout = MainView.class)
@PageTitle("Búsqueda")
@CssImport("./styles/views/search/search-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class SearchView extends HorizontalLayout {
    private final SplitLayout splitLayout = new SplitLayout();

    private TextField author;
    private TextField publicationTitle;
    private TextField startYear;
    private TextField endYear;

    private Grid<Article> articlesGrid;
    private Grid<Book> booksGrid;
    private Grid<CongressCommunication> congressCommunicationsGrid;

    public SearchView() {
        setId("search-view");

        addComponentsToView();
    }

    private void addComponentsToView() {
        H1 pageTitle = new H1("Búsqueda bibliográfica IEI");

        startYear = new TextField("Desde año");
        endYear = new TextField("Hasta año");

        HorizontalLayout yearRangeLayout = new HorizontalLayout();
        yearRangeLayout.add(startYear, endYear);

        author = new TextField("Autor");
        publicationTitle = new TextField("Título");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(pageTitle, author, publicationTitle, yearRangeLayout);

        addPublicationsCheckboxOptionsTo(verticalLayout);

        Button searchButton = new Button("Buscar");
        Button clearFiltersButton = new Button("Limpiar filtros");

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(searchButton, clearFiltersButton);

        verticalLayout.add(buttonsLayout);
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setAlignSelf(Alignment.CENTER);

        add(verticalLayout);

        distributeElementsInSplitLayout(verticalLayout);

        // Add components listeners.
        searchButton.addClickListener(e -> {
            Notification.show("Buscando referencias bibliográficas...");

            requestPublicationsToDataWarehouse();
        });
        clearFiltersButton.addClickListener(e -> clearAllFilters());
    }

    private void addPublicationsCheckboxOptionsTo(VerticalLayout verticalLayout) {
        final String articlesOptionName = "Artículos";
        final String booksOptionName = "Libros";
        final String congressCommunicationOptionName = "Comunicaciones de congreso";

        CheckboxGroup<String> extractorsOptions = new CheckboxGroup<>();
        extractorsOptions.setLabel("Tipos de publicaciones a recuperar");
        extractorsOptions.setItems(articlesOptionName, booksOptionName, congressCommunicationOptionName);

        Set<String> values = new HashSet<>();
        values.add(articlesOptionName);
        values.add(booksOptionName);
        values.add(congressCommunicationOptionName);
        extractorsOptions.setValue(values);

        extractorsOptions.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        verticalLayout.add(extractorsOptions);
    }

    private void distributeElementsInSplitLayout(VerticalLayout verticalLayout) {
        splitLayout.addToPrimary(verticalLayout);

        VerticalLayout dataGridsLayout = new VerticalLayout();

        buildArticlesResultsDataGrid(dataGridsLayout);
        buildBooksResultsDataGrid(dataGridsLayout);
        buildCongressCommunicationResultsDataGrid(dataGridsLayout);

        splitLayout.addToSecondary(dataGridsLayout);

        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        add(splitLayout);
    }

    private void buildArticlesResultsDataGrid(VerticalLayout dataGridsLayout) {
        articlesGrid = new Grid<>();

        articlesGrid.addColumn(Article::getTitle).setHeader("Título").setKey("title").setSortable(true)
                .setFooter("Total: " + 0 + " artículos").setResizable(true);
        articlesGrid.addColumn(Article::getYear).setHeader("Año").setKey("year")
                .setSortable(true).setResizable(true);
        articlesGrid.addColumn(Article::getUrl).setHeader("URL").setKey("url")
                .setSortable(true).setResizable(true);
        articlesGrid.addColumn(Article::getInitialPage).setHeader("Página de inicio").setKey("initialPage")
                .setSortable(true).setResizable(true);
        articlesGrid.addColumn(Article::getFinalPage).setHeader("Página de fin").setKey("finalPage")
                .setSortable(true).setResizable(true);
        articlesGrid.addColumn(Article::getCopyPublishedBy).setHeader("Ejemplar").setKey("copyPublishedBy")
                .setSortable(true).setResizable(true);
        articlesGrid.addColumn(Article::getAuthors).setHeader("Autores").setKey("authors")
                .setSortable(true).setResizable(true);

        H3 articlesGridTitle = new H3("Artículos");
        dataGridsLayout.add(articlesGridTitle, articlesGrid);
    }

    private void buildBooksResultsDataGrid(VerticalLayout dataGridsLayout) {
        booksGrid = new Grid<>();

        booksGrid.addColumn(Book::getTitle).setHeader("Título").setKey("title").setSortable(true)
                .setFooter("Total: " + 0 + " libros").setResizable(true);
        booksGrid.addColumn(Book::getYear).setHeader("Año").setKey("year")
                .setSortable(true).setResizable(true);
        booksGrid.addColumn(Book::getUrl).setHeader("URL").setKey("url")
                .setSortable(true).setResizable(true);
        booksGrid.addColumn(Book::getPublisher).setHeader("Editorial").setKey("publisher")
                .setSortable(true).setResizable(true);
        booksGrid.addColumn(Book::getAuthors).setHeader("Autores").setKey("authors")
                .setSortable(true).setResizable(true);

        H3 booksGridTitle = new H3("Libros");
        dataGridsLayout.add(booksGridTitle, booksGrid);
    }

    private void buildCongressCommunicationResultsDataGrid(VerticalLayout dataGridsLayout) {
        congressCommunicationsGrid = new Grid<>();

        congressCommunicationsGrid
                .addColumn(CongressCommunication::getTitle).setHeader("Título").setKey("title").setSortable(true)
                .setFooter("Total: " + 0 + " comunicaciones de congreso").setResizable(true);
        congressCommunicationsGrid
                .addColumn(CongressCommunication::getYear).setHeader("Año").setKey("year")
                .setSortable(true).setResizable(true);
        congressCommunicationsGrid
                .addColumn(CongressCommunication::getUrl).setHeader("URL").setKey("url")
                .setSortable(true).setResizable(true);
        congressCommunicationsGrid
                .addColumn(CongressCommunication::getCongress).setHeader("Congreso").setKey("congress")
                .setSortable(true).setResizable(true);
        congressCommunicationsGrid
                .addColumn(CongressCommunication::getCongress).setHeader("Edición").setKey("edition")
                .setSortable(true).setResizable(true);
        congressCommunicationsGrid
                .addColumn(CongressCommunication::getCongress).setHeader("Lugar").setKey("place")
                .setSortable(true).setResizable(true);
        congressCommunicationsGrid
                .addColumn(CongressCommunication::getInitialPage).setHeader("Página de inicio").setKey("initialPage")
                .setSortable(true).setResizable(true);
        congressCommunicationsGrid
                .addColumn(CongressCommunication::getFinalPage).setHeader("Página de fin").setKey("finalPage")
                .setSortable(true).setResizable(true);
        congressCommunicationsGrid
                .addColumn(CongressCommunication::getAuthors).setHeader("Autores").setKey("authors")
                .setSortable(true).setResizable(true);

        H3 congressCommunicationsGridTitle = new H3("Comunicaciones de congreso");
        dataGridsLayout.add(congressCommunicationsGridTitle, congressCommunicationsGrid);
    }

    private void requestPublicationsToDataWarehouse() {
        String dataWarehouseLocation = "localhost:8081";
        String jsonResponse = HttpService.executeGet("http://" + dataWarehouseLocation + "/getData");

        if (jsonResponse == null)
            System.err.println("An error has occurred while establishing the connection to the " +
                    "data Warehouse. The JSON response is null.");

        RequestResultResponse requestResult = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            requestResult = objectMapper.readValue(jsonResponse, RequestResultResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (requestResult != null) {
            List<Article> articles = requestResult.getArticles();
            List<Book> books = requestResult.getBooks();
            List<CongressCommunication> congressCommunications = requestResult.getCongressCommunications();

            articlesGrid.setItems(new ArrayList<>());
            booksGrid.setItems(new ArrayList<>());
            congressCommunicationsGrid.setItems(new ArrayList<>());

            if (articles != null)
                articlesGrid.setItems(articles);

            if (books != null)
                booksGrid.setItems(books);

            if (congressCommunications != null)
                congressCommunicationsGrid.setItems(congressCommunications);

            articlesGrid.getDataProvider().refreshAll();
            booksGrid.getDataProvider().refreshAll();
            congressCommunicationsGrid.getDataProvider().refreshAll();
        } else {
            Notification.show("Opss! Ha ocurrido un error al intentar recuperar los datos.",
                    7000,
                    Notification.Position.BOTTOM_CENTER);
        }
    }

    private void clearAllFilters() {
        author.clear();
        publicationTitle.clear();
        startYear.clear();
        endYear.clear();
    }
}
