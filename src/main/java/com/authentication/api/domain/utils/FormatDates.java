package com.authentication.api.domain.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


/**
 * The type Format dates.
 */
public class FormatDates {

    private FormatDates(){}

    /**
     * Instant to string string.
     *
     * @param instant the instant
     * @return the string
     */
    public static String instantToString(Instant instant){
        Date date = Date.from(instant);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }
}
