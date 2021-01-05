package domainModel.utils;

public class YearRange {
    private Long startYear = 1000L;
    private Long endYear = 2999L;

    public YearRange(Long startYear, Long endYear) {
        // If the range is invalid in any way, the values will be the default ones.
        if (!isRangeValid(startYear, endYear))
            return;

        this.startYear = startYear;
        this.endYear = endYear;
    }

    public static boolean isRangeValid(Long startYear, Long endYear) {
        return startYear <= endYear;
    }

    public Long getStartYear() {
        return startYear;
    }

    public Long getEndYear() {
        return endYear;
    }

    public static boolean isYear(String input) {
        // REGEX: Numbers from 1000 to 2999.
        return input.matches("^[12][0-9]{3}$");
    }

    public boolean isGivenYearBetweenRange(Long givenYear) {
        // If there is no given year, the result is over-approximated.
        if (givenYear == null)
            return true;

        return this.startYear <= givenYear && givenYear <= this.endYear;
    }
}
