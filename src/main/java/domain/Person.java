package domain;

public class Person {
    private String name;
    private String surnames;
    private String authoredPublication;

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

    public String getAuthoredPublication() {
        return authoredPublication;
    }

    public void setAuthoredPublication(String authoredPublication) {
        this.authoredPublication = authoredPublication;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surnames='" + surnames + '\'' +
                ", authoredPublication=" + authoredPublication +
                '}';
    }
}
