package ieiWarehousePopulator.extractors.utils;

public class YearRangeChecker {
    public static boolean isGivenYearBetweenRange(Long givenYear, Long startYear, Long endYear) {

        // If there is no given year, the result is over-approximated.
        if (givenYear == null)
            return true;

        return startYear <= givenYear && givenYear <= endYear;
    }
}
