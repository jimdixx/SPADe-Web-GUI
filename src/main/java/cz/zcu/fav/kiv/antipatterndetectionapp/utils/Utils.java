package cz.zcu.fav.kiv.antipatterndetectionapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String bindValues(String query, List<String> parameters) {
        for (String parameter : parameters) {
            query = query.replace("?", parameter);
        }
        return query;
    }

    public static List<String> loadQueryFromFile(String fileName) {
        List<String> queries = new ArrayList<>();
        InputStream is = Utils.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            while (reader.ready()) {

                String line = reader.readLine();
                if (line.startsWith("select") || line.startsWith("set")) {
                    queries.add(line);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queries;
    }

    public static long daysBetween(Date firstDate, Date secondDate) {
        //24-May-2017, change this to your desired Start Date
        LocalDate dateBefore = firstDate.toLocalDate();
        //29-July-2017, change this to your desired End Date
        LocalDate dateAfter = secondDate.toLocalDate();
        return ChronoUnit.DAYS.between(dateBefore, dateAfter);
    }
}
