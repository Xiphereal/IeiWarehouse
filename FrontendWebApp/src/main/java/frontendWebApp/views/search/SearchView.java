package frontendWebApp.views.search;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import domainModel.Article;
import domainModel.Book;
import domainModel.CongressCommunication;
import frontendWebApp.views.main.MainView;

import java.util.ArrayList;
import java.util.List;

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

        addArticlesResultsDataGrid();
        addBooksResultsDataGrid();
        addCongressCommunicationResultsDataGrid();

        // Add components listeners.
        searchButton.addClickListener(e -> Notification.show("Buscando referencias bibligráficas..."));
        clearFiltersButton.addClickListener(e -> clearAllFilters());
    }

    private void addArticlesResultsDataGrid() {
        List<Article> articles = new ArrayList<>();

        articles.add(new Article("Third publication", 2017L, "https://url.com", 200, 230));
        articles.add(new Article("Fourth publication", 2012L, "https://url.com", 210, 230));

        Grid<Article> articlesGrid = new Grid<>();

        articlesGrid.addColumn(Article::getTitle).setHeader("Título").setKey("title").setSortable(true)
                .setFooter("Total: " + articles.size() + " artículos").setResizable(true);
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

        articlesGrid.setItems(articles);

        H3 articlesGridTitle = new H3("Artículos");
        add(articlesGridTitle, articlesGrid);
    }

    private void addBooksResultsDataGrid() {
        List<Book> books = new ArrayList<>();

        books.add(new Book("Third publication", 2017L, "https://url.com", "Editorial"));
        books.add(new Book("gasg publication", 2017L, "https://url.com", "Editorial"));
        books.add(new Book("Third publication", 2017L, "https://url.com", "Editorial"));
        books.add(new Book("Third aseas", 2017L, "https://url.com", "Editorial"));
        books.add(new Book("Third aseg", 2017L, "https://url.com", "Editorial"));
        books.add(new Book("Third publication", 2017L, "https://url.com", "Editorial"));
        books.add(new Book("Third aseg", 2017L, "https://url.com", "Editorial"));
        books.add(new Book("asegsa publication", 2017L, "https://url.com", "Editorial"));

        Grid<Book> booksGrid = new Grid<>();

        booksGrid.addColumn(Book::getTitle).setHeader("Título").setKey("title").setSortable(true)
                .setFooter("Total: " + books.size() + " libros").setResizable(true);
        booksGrid.addColumn(Book::getYear).setHeader("Año").setKey("year")
                .setSortable(true).setResizable(true);
        booksGrid.addColumn(Book::getUrl).setHeader("URL").setKey("url")
                .setSortable(true).setResizable(true);
        booksGrid.addColumn(Book::getPublisher).setHeader("Editorial").setKey("publisher")
                .setSortable(true).setResizable(true);
        booksGrid.addColumn(Book::getAuthors).setHeader("Autores").setKey("authors")
                .setSortable(true).setResizable(true);

        booksGrid.setItems(books);

        H3 booksGridTitle = new H3("Libros");
        add(booksGridTitle, booksGrid);
    }

    private void addCongressCommunicationResultsDataGrid() {
        List<CongressCommunication> congressCommunications = new ArrayList<>();

        congressCommunications.add(
                new CongressCommunication("Third publication",
                        2017L,
                        "https://url.com",
                        "Congress",
                        "Edition",
                        "Place",
                        120,
                        150));
        congressCommunications.add(
                new CongressCommunication("Third publication",
                        2017L,
                        "https://url.com",
                        "Congress",
                        "Edition",
                        "Place",
                        120,
                        150));

        Grid<CongressCommunication> congressCommunicationsGrid = new Grid<>();

        congressCommunicationsGrid
                .addColumn(CongressCommunication::getTitle).setHeader("Título").setKey("title").setSortable(true)
                .setFooter("Total: " + congressCommunications.size() + " comunicaciones de congreso").setResizable(true);
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

        congressCommunicationsGrid.setItems(congressCommunications);

        H3 congressCommunicationsGridTitle = new H3("Comunicaciones de congreso");
        add(congressCommunicationsGridTitle, congressCommunicationsGrid);
    }

    private void clearAllFilters() {
        author.clear();
        publicationTitle.clear();
        startYear.clear();
        endYear.clear();
    }
}
