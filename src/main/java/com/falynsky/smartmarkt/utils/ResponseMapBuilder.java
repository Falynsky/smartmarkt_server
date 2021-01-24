package com.falynsky.smartmarkt.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseMapBuilder {

    public static Map<String, Object> buildResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        return response;
    }

    public static HashMap<String, Object> buildResponse(Object data, boolean success) {
        return new HashMap<String, Object>() {
            {
                put("data", data);
                put("success", success);
            }
        };
    }
}
