package ieiWarehousePopulator.extractors.utils;

public class YearRange {
    private final Long startYear;
    private final Long endYear;

    public YearRange(Long startYear, Long endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public boolean isGivenYearBetweenRange(Long givenYear) {
        // If there is no given year, the result is over-approximated.
        if (givenYear == null)
            return true;

        return this.startYear <= givenYear && givenYear <= this.endYear;
    }
}
