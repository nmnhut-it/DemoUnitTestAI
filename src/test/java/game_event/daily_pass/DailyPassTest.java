package game_event.daily_pass;

import org.junit.Test;

import static org.junit.Assert.*;

public class DailyPassTest {

    /**
     * Should refresh the DailyPass when the current time is greater than or equal to the period
     */
    @Test
    public void refreshWhenCurrentTimeIsGreaterThanOrEqualToPeriod() {
        DailyPass dailyPass   = new DailyPass();
        long      currentTime = dailyPass.startRoundTime() + DailyPass.PERIOD_MILLIS;
        DailyPass.CLOCK = Clock.fixed(Instant.ofEpochMilli(currentTime), ZoneId.systemDefault());

        dailyPass.refresh();

        assertEquals(dailyPass.startRoundTime(), dailyPass.snapTime(currentTime));
        assertTrue(dailyPass.mapG().isEmpty());
        assertTrue(dailyPass.markClaimed.isEmpty());
    }

    /**
     * Should not refresh the DailyPass when the current time is less than the period
     */
    @Test
    public void refreshWhenCurrentTimeIsLessThanPeriod() {
        DailyPass dailyPass      = new DailyPass();
        long      currentTime    = dailyPass.now();
        long      startRoundTime = dailyPass.startRoundTime();
        long      periodMillis   = DailyPass.PERIOD_MILLIS;

        dailyPass.refresh();

        if (currentTime - startRoundTime < periodMillis) {
            assertEquals(startRoundTime, dailyPass.startRoundTime());
        } else {
            assertNotEquals(startRoundTime, dailyPass.startRoundTime());
        }
    }
}