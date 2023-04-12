package game_event.daily_pass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class DailyPassDiffblueTest {
    /**
     * Method under test: {@link DailyPass#refresh()}
     */
    @Test
    public void testRefresh() {
        // Arrange
        DailyPass dailyPass = new DailyPass();

        // Act
        DailyPass actualRefreshResult = dailyPass.refresh();

        // Assert
        assertSame(dailyPass, actualRefreshResult);
        assertEquals(1681243200000L, actualRefreshResult.startRoundTime);
        assertTrue(actualRefreshResult.mapG.isEmpty());
    }

    /**
     * Method under test: {@link DailyPass#countStreak(long, Map, long)}
     */
    @Test
    public void testCountStreak() {
        // Arrange
        DailyPass              dailyPass       = new DailyPass();
        long                   checkMillisSec  = 3L;
        HashMap<Long, Integer> _mapG           = new HashMap<>();
        long                   _startRoundTime = 3L;

        // Act
        long actualCountStreakResult = dailyPass.countStreak(checkMillisSec, _mapG, _startRoundTime);

        // Assert
        assertEquals(0L, actualCountStreakResult);
    }

    /**
     * Method under test: {@link DailyPass#countStreak(long, Map, long)}
     */
    @Test
    public void testCountStreak2() {
        // Arrange
        DailyPass dailyPass      = new DailyPass();
        long      checkMillisSec = 3L;

        HashMap<Long, Integer> _mapG = new HashMap<>();
        _mapG.put(1L, 1);
        long _startRoundTime = 3L;

        // Act
        long actualCountStreakResult = dailyPass.countStreak(checkMillisSec, _mapG, _startRoundTime);

        // Assert
        assertEquals(0L, actualCountStreakResult);
    }

    /**
     * Method under test: {@link DailyPass#countStreak(long, Map, long)}
     */
    @Test
    public void testCountStreak3() {
        // Arrange
        DailyPass dailyPass      = new DailyPass();
        long      checkMillisSec = 3L;

        HashMap<Long, Integer> _mapG = new HashMap<>();
        _mapG.put(3L, 3);
        _mapG.put(1L, 1);
        long _startRoundTime = 3L;

        // Act
        long actualCountStreakResult = dailyPass.countStreak(checkMillisSec, _mapG, _startRoundTime);

        // Assert
        assertEquals(0L, actualCountStreakResult);
    }

    /**
     * Method under test: {@link DailyPass#countStreak(long, Map, long)}
     */
    @Test
    public void testCountStreak4() {
        // Arrange
        DailyPass dailyPass      = new DailyPass();
        long      checkMillisSec = 3L;

        HashMap<Long, Integer> _mapG = new HashMap<>();
        _mapG.put(3L, 604800000);
        _mapG.put(1L, 1);
        long _startRoundTime = 3L;

        // Act
        long actualCountStreakResult = dailyPass.countStreak(checkMillisSec, _mapG, _startRoundTime);

        // Assert
        assertEquals(1L, actualCountStreakResult);
    }

    /**
     * Method under test: {@link DailyPass#countStreak(long, Map, long)}
     */
    @Test
    public void testCountStreak5() {
        // Arrange
        DailyPass dailyPass      = new DailyPass();
        long      checkMillisSec = Long.MAX_VALUE;

        HashMap<Long, Integer> _mapG = new HashMap<>();
        _mapG.put(3L, 3);
        _mapG.put(1L, 1);
        long _startRoundTime = 3L;

        // Act
        long actualCountStreakResult = dailyPass.countStreak(checkMillisSec, _mapG, _startRoundTime);

        // Assert
        assertEquals(0L, actualCountStreakResult);
    }

    /**
     * Method under test: {@link DailyPass#calcMilestone(long)}
     */
    @Test
    public void testCalcMilestone() {
        // Arrange
        DailyPass dailyPass = new DailyPass();
        long      epochSec  = 1L;

        // Act
        int actualCalcMilestoneResult = dailyPass.calcMilestone(epochSec);

        // Assert
        assertEquals(1, actualCalcMilestoneResult);
    }

    /**
     * Method under test: {@link DailyPass#calcMilestone(long)}
     */
    @Test
    public void testCalcMilestone2() {
        // Arrange
        DailyPass dailyPass = new DailyPass();
        long      epochSec  = Long.MAX_VALUE;

        // Act
        int actualCalcMilestoneResult = dailyPass.calcMilestone(epochSec);

        // Assert
        assertEquals(5, actualCalcMilestoneResult);
    }
}

