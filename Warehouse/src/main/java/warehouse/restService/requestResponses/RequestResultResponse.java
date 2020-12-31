package warehouse.restService.requestResponses;

import domainModel.Article;
import domainModel.Book;
import domainModel.CongressCommunication;

import java.util.List;

public class RequestResultResponse implements RequestResponse {
    private List<Article> articles;
    private List<Book> books;
    private List<CongressCommunication> congressCommunications;

    public RequestResultResponse(List<Article> articles, List<Book> books, List<CongressCommunication> congressCommunications) {
        this.articles = articles;
        this.books = books;
        this.congressCommunications = congressCommunications;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<CongressCommunication> getCongressCommunications() {
        return congressCommunications;
    }

    public void setCongressCommunications(List<CongressCommunication> congressCommunications) {
        this.congressCommunications = congressCommunications;
    }
}
