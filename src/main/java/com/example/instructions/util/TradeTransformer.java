package com.example.instructions.util;
import com.example.instructions.controller.TradeController;

import java.time.Instant;
import java.util.logging.Logger;


public class TradeTransformer {

    public static String maskPII(String data)
    {
        //This function mask all characters except last 4
        if (data.length() <= 4) {
            return data;
        }
        String toMask = data.substring(0, data.length() - 4);
        String keepPlain = data.substring(data.length() - 4);
        return toMask.replaceAll(".", "*") + keepPlain;
    }

    public static double convertToDouble(String data)
    {
        try {
            return Double.parseDouble(data);
        }
        catch (Exception e) {
            return -1.0;
        }
    }

    public static String normalizeType(String data)
    {
        if ( "Buy".equalsIgnoreCase(data))
            return "B";
        else if ("Sell".equalsIgnoreCase(data))
            return "S";
        else if ("SellShort".equalsIgnoreCase(data))
            return "SS";
        return data;
    }

    public static Instant DateTimeConverter(String data)
    {
        Instant instant = Instant.parse(data);
        return instant;
    }
}
