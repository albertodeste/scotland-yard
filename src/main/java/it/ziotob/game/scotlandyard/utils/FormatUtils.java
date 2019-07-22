package it.ziotob.game.scotlandyard.utils;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormatUtils {

    public static final DateTimeFormatter DATE_TIME_JSON_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public static String formatString(Long number) {
        return Optional.ofNullable(number).map(n -> "\"" + n.toString() + "\"").orElse("null");
    }

    public static <E> String formatList(Collection<E> list, Function<E, String> mappingFunction) {
        return Optional.ofNullable(list).map(Collection::stream).orElse(Stream.empty()).map(mappingFunction).collect(Collectors.joining(","));
    }
}
