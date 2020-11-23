package ieiWarehousePopulator.domain;

public class Copy {
    private Integer volume;
    private Integer number;
    private Integer month;
    private Magazine magazinePublishBy;

    public Copy(Integer volume, Integer number, Integer month) {
        this.volume = volume;
        this.number = number;
        this.month = month;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Magazine getMagazinePublishBy() {
        return magazinePublishBy;
    }

    public void setMagazinePublishBy(Magazine magazinePublishBy) {
        this.magazinePublishBy = magazinePublishBy;
    }

    @Override
    public String toString() {
        return "Copy{" +
                "volume=" + volume +
                ", number=" + number +
                ", month=" + month +
                ", magazinePublishBy=" + magazinePublishBy +
                '}';
    }
}
