package game_event.daily_pass;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

public class DailyPass {
    public static final HashMap<String, HashMap<String, Integer>> gifts = new HashMap<>(); // gift round --> gift definition
    public static final ZoneOffset                                ZONE  = ZonedDateTime.now().getOffset();
    public static       Clock                                     CLOCK = Clock.systemDefaultZone();
    private static      String                                    config;
    public static final int                                       HOUR_NEW_DAY         = 3;
    public static final int                                       STREAK_REQUIRED_DAYS = 5;
    public static final int                                       REQUIRED_G           = 100;
    public static final HashMap<String, Integer>                  MAP_REQUIRED_PRICE   = new HashMap<String, Integer>(); // country --> required amount (in that country)
    public static final int                                       PERIOD_DAY           = 7;
    public static final long MILLIS_IN_DAY = PERIOD_DAY * 24 * 60 * 60 * 1000L;
    public static final long PERIOD_MILLIS = MILLIS_IN_DAY;

    protected TreeMap<Long, Integer> mapG        = new TreeMap<>(); // epoch millisec(day) --> G. use tree map to ensure ordering

    private   HashSet<Long>          markClaimed = new HashSet<>(); // epoch millisec(day) --> G

    protected long lastClaimedStreak = 0;

    protected long startRoundTime; // epoch sec - start day

    public static void loadConfig() throws Exception {
        // todo: load from file
    }

    public DailyPass refresh() {
        long now = now();
        if (now - startRoundTime >= PERIOD_MILLIS) {
            startRoundTime = snapTime(now);
            mapG.keySet().removeIf(v -> v < startRoundTime);
            markClaimed.removeIf(v -> v < startRoundTime);
        }
        return this;
    }

    protected long resetTimeToday() {
        return snapTime(now());
    }

    protected long now() {
        return CLOCK.millis();
    }

    protected long snapTime(long timeMillis) {
        return (snapToTime(timeMillis, HOUR_NEW_DAY, 0, 0));
    }


    protected long countStreak(long checkMillisSec, Map<Long, Integer> _mapG, long _startRoundTime) {
        long filter = 0L;
        for (Map.Entry<Long, Integer> v : _mapG.entrySet()) {
            long keyMillis     = v.getKey();
            int  amountG = v.getValue();
            if (keyMillis >= _startRoundTime
                    && checkMillisSec - keyMillis <= PERIOD_MILLIS && amountG >= REQUIRED_G) {
                filter++;
            }
        }
        return filter;
    }


    public boolean checkStreak(long epochMillis) {

        if (lastClaimedStreak == startRoundTime)
            return false;
        if (!_checkStreak(epochMillis)) {
            return false;
        }
        return true;
    }
    protected boolean _checkStreak(long epochMillis) {
        long today  = snapTime(epochMillis);
        long filter = countStreak(today, mapG, startRoundTime);
        return filter >= STREAK_REQUIRED_DAYS;
    }


    protected int calcMilestone(long epochSec) {
        long   deltaTime = snapTime(epochSec) - startRoundTime;
        long millisInDay         = MILLIS_IN_DAY;
        long   day       = deltaTime / millisInDay;
        if (day >= 7)
            day = day % 7;
        return (int) (day + 1);
    }

    protected List<Integer> mapMilestone(Collection<Long> longs){
        List<Integer> result = new ArrayList<Integer>();
        for (long l: longs){
            result.add(calcMilestone(l));
        }
        return result;
    }

    public void record(long time, long amount) {
        this.mapG.merge(snapTime(time), (int) amount, Integer::sum);
    }

    public Map<Long, Integer> mapG() {
        return Collections.unmodifiableMap(mapG);
    }

    public long startRoundTime() {
        return startRoundTime;
    }

    public long snapToTime(long currentTime, int hour, int minute, int second) {
        return LocalDateTime.ofEpochSecond(currentTime / 1000, 0, ZONE)
                .withHour(hour)
                .withMinute(minute)
                .withSecond(second)
                .withNano(0)
                .toEpochSecond(ZONE) * 1000L;
    }
}
