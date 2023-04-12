package game_event.daily_pass;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DailyPassTest {

    /**
     * Should return the same milestone for different times within the same day
     */
    @Test
    public void calcMilestoneForDifferentTimesInSameDay() {
        DailyPass dailyPass = new DailyPass();

        long currentTime = System.currentTimeMillis();
        int  milestone1  = dailyPass.calcMilestone(currentTime);
        int  milestone2  = dailyPass.calcMilestone(currentTime + 1000 * 60 * 60); // Add 1 hour
        int  milestone3  = dailyPass.calcMilestone(currentTime + 1000 * 60 * 60 * 23); // Add 23 hours

        assertEquals(milestone1, milestone2);
        assertEquals(milestone1, milestone3);
    }

    /**
     * Should handle edge cases like leap years and daylight saving time changes
     */
    @Test
    public void calcMilestoneForEdgeCases() {
        DailyPass dailyPass = new DailyPass();

        // Test for day 1
        long epochSec1  = dailyPass.snapToTime(System.currentTimeMillis(), 0, 0, 0);
        int  milestone1 = dailyPass.calcMilestone(epochSec1);
        assertEquals(1, milestone1);

        // Test for day 7
        long epochSec7 =
                dailyPass.snapToTime(
                        System.currentTimeMillis() - (6 * DailyPass.MILLIS_IN_DAY), 0, 0, 0);
        int milestone7 = dailyPass.calcMilestone(epochSec7);
        assertEquals(7, milestone7);

        // Test for day 8 (should return 1 as it's a new week)
        long epochSec8 =
                dailyPass.snapToTime(
                        System.currentTimeMillis() - (7 * DailyPass.MILLIS_IN_DAY), 0, 0, 0);
        int milestone8 = dailyPass.calcMilestone(epochSec8);
        assertEquals(1, milestone8);

        // Test for day 14
        long epochSec14 =
                dailyPass.snapToTime(
                        System.currentTimeMillis() - (13 * DailyPass.MILLIS_IN_DAY), 0, 0, 0);
        int milestone14 = dailyPass.calcMilestone(epochSec14);
        assertEquals(7, milestone14);

        // Test for day 15 (should return 1 as it's a new week)
        long epochSec15 =
                dailyPass.snapToTime(
                        System.currentTimeMillis() - (14 * DailyPass.MILLIS_IN_DAY), 0, 0, 0);
        int milestone15 = dailyPass.calcMilestone(epochSec15);
        assertEquals(1, milestone15);
    }

    /**
     * Should return different milestones for different days
     */
    @Test
    public void calcMilestoneForDifferentDays() {
        DailyPass dailyPass = new DailyPass();

        long day1 = dailyPass.snapToTime(System.currentTimeMillis(), 3, 0, 0);
        long day2 = dailyPass.snapToTime(System.currentTimeMillis() + 24 * 60 * 60 * 1000, 3, 0, 0);
        long day3 =
                dailyPass.snapToTime(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000, 3, 0, 0);
        long day4 =
                dailyPass.snapToTime(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000, 3, 0, 0);
        long day5 =
                dailyPass.snapToTime(System.currentTimeMillis() + 4 * 24 * 60 * 60 * 1000, 3, 0, 0);
        long day6 =
                dailyPass.snapToTime(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000, 3, 0, 0);
        long day7 =
                dailyPass.snapToTime(System.currentTimeMillis() + 6 * 24 * 60 * 60 * 1000, 3, 0, 0);

        assertEquals(1, dailyPass.calcMilestone(day1));
        assertEquals(2, dailyPass.calcMilestone(day2));
        assertEquals(3, dailyPass.calcMilestone(day3));
        assertEquals(4, dailyPass.calcMilestone(day4));
        assertEquals(5, dailyPass.calcMilestone(day5));
        assertEquals(6, dailyPass.calcMilestone(day6));
        assertEquals(7, dailyPass.calcMilestone(day7));
    }

    /**
     * Should calculate the correct milestone for a given epoch time
     */
    @Test
    public void calcMilestoneForGivenEpochTime() {
        DailyPass dailyPass = new DailyPass();

        long epochTime1         = 1627810800000L; // 2021-08-01T00:00:00Z
        int  expectedMilestone1 = 1;
        assertEquals(expectedMilestone1, dailyPass.calcMilestone(epochTime1));

        long epochTime2         = 1627897200000L; // 2021-08-02T00:00:00Z
        int  expectedMilestone2 = 2;
        assertEquals(expectedMilestone2, dailyPass.calcMilestone(epochTime2));

        long epochTime3         = 1627983600000L; // 2021-08-03T00:00:00Z
        int  expectedMilestone3 = 3;
        assertEquals(expectedMilestone3, dailyPass.calcMilestone(epochTime3));

        long epochTime4         = 1628070000000L; // 2021-08-04T00:00:00Z
        int  expectedMilestone4 = 4;
        assertEquals(expectedMilestone4, dailyPass.calcMilestone(epochTime4));

        long epochTime5         = 1628156400000L; // 2021-08-05T00:00:00Z
        int  expectedMilestone5 = 5;
        assertEquals(expectedMilestone5, dailyPass.calcMilestone(epochTime5));

        long epochTime6         = 1628242800000L; // 2021-08-06T00:00:00Z
        int  expectedMilestone6 = 6;
        assertEquals(expectedMilestone6, dailyPass.calcMilestone(epochTime6));

        long epochTime7         = 1628329200000L; // 2021-08-07T00:00:00Z
        int  expectedMilestone7 = 7;
        assertEquals(expectedMilestone7, dailyPass.calcMilestone(epochTime7));

        long epochTime8         = 1628415600000L; // 2021-08-08T00:00:00Z
        int  expectedMilestone8 = 1;
        assertEquals(expectedMilestone8, dailyPass.calcMilestone(epochTime8));
    }
}