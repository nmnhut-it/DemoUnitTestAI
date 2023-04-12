package game_event.daily_pass;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class DailyPassMachinetTest {

    /**
     * Should count the streak only for days with the required amount of G
     */
    @Test
    public void countStreakWithRequiredAmountOfG() {
        DailyPass dailyPass = new DailyPass();
        dailyPass.startRoundTime = dailyPass.snapTime(dailyPass.now());

        long               today    = dailyPass.snapTime(dailyPass.now());
        Map<Long, Integer> testMapG = new HashMap<>();

        for (int i = 0; i < DailyPass.STREAK_REQUIRED_DAYS; i++) {
            long day = today - (i * DailyPass.MILLIS_IN_DAY);
            testMapG.put(day, DailyPass.REQUIRED_G);
        }

        long streak = dailyPass.countStreak(today, testMapG, dailyPass.startRoundTime);
        assertEquals(DailyPass.STREAK_REQUIRED_DAYS, streak);
    }

    /**
     * Should count the streak considering only the days within the period
     */
    @Test
    public void countStreakWithinPeriod() {
        DailyPass dailyPass = new DailyPass();
        dailyPass.startRoundTime = dailyPass.snapTime(dailyPass.now());

        long               today    = dailyPass.snapTime(dailyPass.now());
        Map<Long, Integer> testMapG = new HashMap<>();
        testMapG.put(today - DailyPass.MILLIS_IN_DAY * 1, DailyPass.REQUIRED_G);
        testMapG.put(today - DailyPass.MILLIS_IN_DAY * 2, DailyPass.REQUIRED_G);
        testMapG.put(today - DailyPass.MILLIS_IN_DAY * 3, DailyPass.REQUIRED_G);
        testMapG.put(today - DailyPass.MILLIS_IN_DAY * 4, DailyPass.REQUIRED_G);
        testMapG.put(today - DailyPass.MILLIS_IN_DAY * 5, DailyPass.REQUIRED_G);

        long streak = dailyPass.countStreak(today, testMapG, dailyPass.startRoundTime);
        assertEquals(5, streak);
    }

    /**
     * Should count the streak correctly when the conditions are met
     */
    @Test
    public void countStreakWhenConditionsAreMet() { // Prepare test data
        long               today          = System.currentTimeMillis();
        long               startRoundTime = today - DailyPass.PERIOD_DAY * 3 * DailyPass.MILLIS_IN_DAY;
        Map<Long, Integer> mapG           = new HashMap<>();
        mapG.put(startRoundTime, DailyPass.REQUIRED_G);
        mapG.put(startRoundTime + DailyPass.MILLIS_IN_DAY, DailyPass.REQUIRED_G);
        mapG.put(startRoundTime + 2 * DailyPass.MILLIS_IN_DAY, DailyPass.REQUIRED_G);
        mapG.put(startRoundTime + 3 * DailyPass.MILLIS_IN_DAY, DailyPass.REQUIRED_G);
        mapG.put(startRoundTime + 4 * DailyPass.MILLIS_IN_DAY, DailyPass.REQUIRED_G);

        // Create a spy of DailyPass to test the protected method countStreak
        DailyPass dailyPassSpy = spy(new DailyPass());
        doReturn(today).when(dailyPassSpy).now();

        // Call the method countStreak
        long streak = dailyPassSpy.countStreak(today, mapG, startRoundTime);

        // Verify the result
        assertEquals(DailyPass.STREAK_REQUIRED_DAYS, streak);
    }
}