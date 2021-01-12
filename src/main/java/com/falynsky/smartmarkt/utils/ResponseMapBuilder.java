package com.falynsky.smartmarkt.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseMapBuilder<T> {

    public Map<String, Object> buildResponse(T data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        return response;
    }
}
