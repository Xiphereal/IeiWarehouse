package domain;

import java.util.List;

public class Person {
    private String name;
    private String surnames;
    private List<String> authoredPublications;

    public Person(String name, String surnames) {
        this.name = name;
        this.surnames = surnames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public List<String> getAuthoredPublications() {
        return authoredPublications;
    }

    public void setAuthoredPublications(List<String> authoredPublications) {
        this.authoredPublications = authoredPublications;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surnames='" + surnames + '\'' +
                ", authoredPublications=" + authoredPublications +
                '}';
    }
}
