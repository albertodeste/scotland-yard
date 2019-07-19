package it.ziotob.game.scotlandyard.utils;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class FormatUtils {

    public static final DateTimeFormatter DATE_TIME_JSON_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public static String formatString(Long number) {
        return Optional.ofNullable(number).map(n -> "\"" + n.toString() + "\"").orElse("null");
    }
}
