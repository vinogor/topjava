package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenTimesHalfOpen(LocalTime lt, LocalTime from, LocalTime to) {
        return lt.compareTo(from) >= 0 && lt.compareTo(to) < 0;
    }

    public static boolean isBetweenTimesAndDatesHalfOpen(LocalDateTime localDateTime, LocalDateTime from, LocalDateTime to) {
        return localDateTime.compareTo(from) >= 0 && localDateTime.compareTo(to) < 0;
    }

    public static <T extends Comparable<T>> boolean isBetweenInclude(T t, T from, T to) {
        return t.compareTo(from) >= 0 && t.compareTo(to) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

