package domainModel;

public class CongressCommunication extends Publication {
    private String congress;
    private String edition;
    private String place;
    private Integer initialPage;
    private Integer finalPage;

    public CongressCommunication(String title,
                                 Long year,
                                 String url,
                                 String congress,
                                 String edition,
                                 String place,
                                 Integer initialPage,
                                 Integer finalPage) {
        super(title, year, url);
        this.congress = congress;
        this.edition = edition;
        this.place = place;
        this.initialPage = initialPage;
        this.finalPage = finalPage;
    }

    public String getCongress() {
        return congress;
    }

    public void setCongress(String congress) {
        this.congress = congress;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getInitialPage() {
        return initialPage;
    }

    public void setInitialPage(int initialPage) {
        this.initialPage = initialPage;
    }

    public Integer getFinalPage() {
        return finalPage;
    }

    public void setFinalPage(int finalPage) {
        this.finalPage = finalPage;
    }

    @Override
    public String toString() {
        return "CongressCommunication{" +
                "congress='" + congress + '\'' +
                ", edition='" + edition + '\'' +
                ", place='" + place + '\'' +
                ", initialPage=" + initialPage +
                ", finalPage=" + finalPage +
                "} " + super.toString();
    }
}
