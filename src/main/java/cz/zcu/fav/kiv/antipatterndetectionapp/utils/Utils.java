package cz.zcu.fav.kiv.antipatterndetectionapp.utils;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    public static List<Map<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }

        return list;
    }

    public static List<String> fillQueryWithSearchSubstrings(List<String> queries, List<String> substrings) {
        List<String> preparedQueries = new ArrayList<>();
        for (String query : queries) {
            // check if query contains sequence of substrings to fill
            if (query.contains("§0§")) {
                int counter = 0;

                //insert all substrings in the query
                for (String substring : substrings) {
                    query = query.replaceAll("§" + counter + "§", substring);
                    counter++;
                }

                // replacement of unused places for substrings
                query = query.replaceAll("§[0-9]§", "^@");

            }
            preparedQueries.add(query);
        }

        return preparedQueries;
    }
}
