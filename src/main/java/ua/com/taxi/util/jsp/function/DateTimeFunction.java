package ua.com.taxi.util.jsp.function;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFunction {

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
