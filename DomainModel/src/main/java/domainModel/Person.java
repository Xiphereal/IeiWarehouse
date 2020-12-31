package domainModel;

import domainModel.utils.Tuple;

import java.util.*;

public class Person {
    private String name;
    private String surnames;
    private String authoredPublication;

    public Person(String name, String surnames) {
        this.name = name;
        this.surnames = surnames;
    }

    public static List<Person> convertToPerson(List<Tuple<String, String>> tupleCollection) {
        List<Person> personList = new ArrayList<>();

        for (Tuple<String, String> tuple : tupleCollection)
            personList.add(new Person(tuple.getFirstElement(), tuple.getSecondElement()));

        return personList;
    }

    /**
     * @param author The name is the first encountered word, the surname the second.
     */
    public static Person extractPersonAttributes(String author) {
        // Split the string using spaces as separators.
        String[] splitAuthor = author.split(" ");

        String name = splitAuthor[0];
        String surname = splitAuthor.length > 1 ? splitAuthor[1] : null;

        return new Person(name, surname);
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

    @Override
    public int hashCode() {
        return Objects.hash(name, surnames);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Person person = (Person) o;

        return Objects.equals(name, person.name) &&
                Objects.equals(surnames, person.surnames);
    }
}
