package domainModel.utils;

public class YearRange {
    private final Long startYear;
    private final Long endYear;

    public YearRange() {
        this.startYear = 1000L;
        this.endYear = 2999L;
    }

    public YearRange(Long startYear, Long endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public static boolean isRangeValid(String startYear, String endYear) {
        boolean isStartYearValid = startYear.isEmpty() || isYear(startYear);
        boolean isEndYearValid = endYear.isEmpty() || isYear(endYear);

        // If any of the input years are not valid, there's no need in
        // continuing and throwing a NumberFormatException on
        // Long.parseLong invocations.
        if (!isStartYearValid || !isEndYearValid)
            return false;

        boolean isStartYearBeforeEndYear = Long.parseLong(startYear) <= Long.parseLong(endYear);

        return isStartYearBeforeEndYear;
    }

    public Long getStartYear() {
        return startYear;
    }

    public Long getEndYear() {
        return endYear;
    }

    private static boolean isYear(String input) {
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
