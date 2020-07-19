package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static final Logger log = getLogger(SecurityUtil.class);

    // может использоваться только на слое web !!

    private static int userId = 0;

    public static int authUserId() {
//        return 1;
        return userId;
    }

    public static void setUserId(int userId) {
        log.debug("set user id = {}", userId);
        SecurityUtil.userId = userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}