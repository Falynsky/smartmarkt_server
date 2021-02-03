package com.falynsky.smartmarkt.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseMapUtils {

    public static Map<String, Object> buildResponse(Object data) {
        return new HashMap<String, Object>() {
            {
                put("data", data);
            }
        };
    }

    public static Map<String, Object> buildResponse(Object data, boolean success) {
        return new HashMap<String, Object>() {
            {
                put("data", data);
                put("success", success);
            }
        };
    }
}
