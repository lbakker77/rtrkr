package de.lbakker77.retracker.core.interceptor;

import de.lbakker77.retracker.core.UserTimeZoneService;

import java.util.TimeZone;



public class UserTimeZoneServiceImpl implements UserTimeZoneService {
    private TimeZone timeZone;

    void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public TimeZone getUserTimeZone() {
        return timeZone;
    }
}
