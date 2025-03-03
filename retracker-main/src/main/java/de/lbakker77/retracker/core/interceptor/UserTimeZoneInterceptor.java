package de.lbakker77.retracker.core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.TimeZone;

@AllArgsConstructor
public class UserTimeZoneInterceptor implements HandlerInterceptor {
    private final UserTimeZoneServiceImpl userTimeZoneService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userTimeZoneString = request.getHeader("X-User-Timezone");
        if (userTimeZoneString == null) {
            userTimeZoneString = TimeZone.getDefault().getID();
        }
        var userTimeZone = TimeZone.getTimeZone(userTimeZoneString);
        userTimeZoneService.setTimeZone(userTimeZone);
        return true;
    }
}
// constructor