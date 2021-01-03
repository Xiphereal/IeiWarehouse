package dblpWrapper.restService.requestResponses;

import domainModel.Article;
import domainModel.Book;
import domainModel.CongressCommunication;
import org.json.JSONObject;

import java.util.List;

public class RequestResultResponse implements RequestResponse{
    private List<Article> articles;

    public RequestResultResponse(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
    public void setArticles(List<Article> articles) {
       this.articles = articles;
    }
}
