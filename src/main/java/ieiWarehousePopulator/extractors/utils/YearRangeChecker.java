package ieiWarehousePopulator.extractors.utils;

public class YearRangeChecker {
    public static boolean isGivenYearBetweenRange(Long givenYear, Long startYear, Long endYear) {
        return startYear <= givenYear && givenYear <= endYear;
    }
}
