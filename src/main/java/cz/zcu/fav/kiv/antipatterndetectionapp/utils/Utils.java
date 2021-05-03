package cz.zcu.fav.kiv.antipatterndetectionapp.utils;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    private final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static Long[] arrayOfStringsToArrayOfLongs(String[] strings) {
        Long[] longs = new Long[strings.length];
        for (int i = 0; i < strings.length; i++) {
            longs[i] = Long.parseLong(strings[i]);
        }
        return longs;
    }

    public static long daysBetween(Date firstDate, Date secondDate) {
        //24-May-2017, change this to your desired Start Date
        LocalDate dateBefore = firstDate.toLocalDate();
        //29-July-2017, change this to your desired End Date
        LocalDate dateAfter = secondDate.toLocalDate();
        return ChronoUnit.DAYS.between(dateBefore, dateAfter);
    }

    public static List<ResultDetail> createResultDetailsList(ResultDetail... resultDetails) {
        return new ArrayList<>(Arrays.asList(resultDetails));
    }
}
