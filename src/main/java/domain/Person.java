package domain;

import java.util.List;

public class Person {
    private String name;
    private String lastNames;
    private List<String> authoredPublications;

    public Person(String name, String lastNames, List<String> authoredPublications) {
        this.name = name;
        this.lastNames = lastNames;
        this.authoredPublications = authoredPublications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public List<String> getAuthoredPublications() {
        return authoredPublications;
    }

    public void setAuthoredPublications(List<String> authoredPublications) {
        this.authoredPublications = authoredPublications;
    }
}
