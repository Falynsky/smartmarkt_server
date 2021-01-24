package com.falynsky.smartmarkt.utils;

public class PriceUtils {

    public static double roundPrice(double price) {
        return Math.round(price * 100.0) / 100.0;
    }
}
