package it.ziotob.game.scotlandyard.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class ServletUtils {

    public static Map<String, Object> parseBody(HttpServletRequest request) throws IOException {
        return new ObjectMapper().reader().forType(Map.class).readValue(request.getReader());
    }
}
