package game_event.daily_pass;

import org.apache.kafka.common.utils.Time;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Clock;
import java.time.Duration;

import static game_event.daily_pass.DailyPass.PERIOD_DAY;
import static org.junit.Assert.fail;

@Ignore
public class DailyPassHumanVersionTest {

    @Test
    @Ignore
    public void loadConfig() throws Exception {
        DailyPass.loadConfig();
    }

    @Test
    @Ignore
    public void refresh() {
        DailyPass dailyPass = new DailyPass();
        dailyPass.record(dailyPass.now(), 100);
        dailyPass.refresh();
        DailyPass.CLOCK = Clock.offset(DailyPass.CLOCK, Duration.ofDays(PERIOD_DAY));
        dailyPass.refresh();
        assert  dailyPass.mapG.isEmpty();
        assert dailyPass.startRoundTime == dailyPass.now();

    }





    @Test
    @Ignore
    public void checkStreak() {
        DailyPass dailyPass = new DailyPass();
        // basic case - in period - all days in period
        for (int day = 1; day <= PERIOD_DAY; ++day) {
            dailyPass.refresh();
            dailyPass.record(dailyPass.now(), DailyPass.REQUIRED_G);
            if (day >= DailyPass.STREAK_REQUIRED_DAYS) {
                if (!dailyPass.checkStreak(dailyPass.now())) {
                    fail();
                }
            } else {
                if (dailyPass.checkStreak(dailyPass.now())) {
                    fail();
                }
            }
            DailyPass.CLOCK = Clock.offset(DailyPass.CLOCK, Duration.ofHours(24));
        }

        dailyPass = new DailyPass();
        // basic case - in period - payment only the first @STREAK_REQUIRED_DAYS@ days
        for (int day = 1; day <= PERIOD_DAY; ++day) {
            dailyPass.refresh();
            if (day <= DailyPass.STREAK_REQUIRED_DAYS)
                dailyPass.record(dailyPass.now(), DailyPass.REQUIRED_G);
            if (day >= DailyPass.STREAK_REQUIRED_DAYS) {
                if (!dailyPass.checkStreak(dailyPass.now())) {
                    fail();
                }
            } else {
                if (dailyPass.checkStreak(dailyPass.now())) {
                    fail();
                }
            }
            DailyPass.CLOCK = Clock.offset(DailyPass.CLOCK, Duration.ofHours(24));
        }

        dailyPass = new DailyPass();
        // basic case - one period - less than @STREAK_REQUIRED_DAYS@ days - expect no streak
        for (int day = 1; day <= PERIOD_DAY; ++day) {
            dailyPass.refresh();
            if (day < DailyPass.STREAK_REQUIRED_DAYS - 1)
                dailyPass.record(dailyPass.now(), DailyPass.REQUIRED_G);
            if (day >= DailyPass.STREAK_REQUIRED_DAYS) {
                if (dailyPass.checkStreak(dailyPass.now())) {
                    fail();
                }
            }

            DailyPass.CLOCK = Clock.offset(DailyPass.CLOCK, Duration.ofHours(24));
        }
        dailyPass = new DailyPass();
        // basic case - one period - non-continuous payment - streak expected
        for (int day = 1; day <= PERIOD_DAY; ++day) {
            dailyPass.refresh();
            if (day != 1)
                dailyPass.record(dailyPass.now(), DailyPass.REQUIRED_G);
            if (day >= DailyPass.STREAK_REQUIRED_DAYS + 1) {
                if (!dailyPass.checkStreak(dailyPass.now())) {
                    fail();
                }
            } else {
                if (dailyPass.checkStreak(dailyPass.now())) {
                    fail();
                }
            }
            DailyPass.CLOCK = Clock.offset(DailyPass.CLOCK, Duration.ofHours(24));
        }


        dailyPass = new DailyPass();
        // basic case - two period
        for (int time = 0; time < 3; ++time) {
            for (int day = 1; day <= PERIOD_DAY; ++day) {
                dailyPass.refresh();
                dailyPass.record(dailyPass.now(), DailyPass.REQUIRED_G);
                if (day >= DailyPass.STREAK_REQUIRED_DAYS) {
                    if (!dailyPass.checkStreak(dailyPass.now())) {
                        fail();
                    }
                } else if (dailyPass.checkStreak(dailyPass.now())) {
                    fail();
                }
                DailyPass.CLOCK = Clock.offset(DailyPass.CLOCK, Duration.ofHours(24));
            }
        }
    }

    @Test
    @Ignore
    public void testCountStreak(){
        DailyPass dailyPass = new DailyPass();
        dailyPass.refresh();
        long      now = dailyPass.now();
        dailyPass.record(now,DailyPass.REQUIRED_G); // first time today
        assert  dailyPass.countStreak(now, dailyPass.mapG(), dailyPass.startRoundTime()) == 1;
        dailyPass.record(now,DailyPass.REQUIRED_G); // second time today
        assert  dailyPass.countStreak(now, dailyPass.mapG(), dailyPass.startRoundTime()) == 1;

        dailyPass.record(now+DailyPass.MILLIS_IN_DAY,DailyPass.REQUIRED_G); // first time tomorrow
        assert  dailyPass.countStreak(now, dailyPass.mapG(), dailyPass.startRoundTime()) == 2;

        dailyPass.record(now - DailyPass.MILLIS_IN_DAY,DailyPass.REQUIRED_G); // first time yesterday - shouldn't count
        assert  dailyPass.countStreak(now, dailyPass.mapG(), dailyPass.startRoundTime()) == 2;

    }

}