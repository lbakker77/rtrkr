package de.lbakker77.retracker.core.interceptor;

import java.util.TimeZone;



public class UserTimeZoneService {
    private TimeZone timeZone;

    void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public TimeZone getUserTimeZone() {
        return timeZone;
    }
}
