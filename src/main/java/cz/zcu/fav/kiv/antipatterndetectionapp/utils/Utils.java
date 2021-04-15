package cz.zcu.fav.kiv.antipatterndetectionapp.utils;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.ResultDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    private final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    @Autowired
    ServletContext context;

    public void getJsonResource() {

        try {
            URL test = context.getResource(
                    "/queries/too_long_sprint.sql");
            LOGGER.error("URL: " + test.toString());
            BufferedReader read = new BufferedReader(
                    new InputStreamReader(test.openStream()));
            String i;
            while ((i = read.readLine()) != null) {
                LOGGER.error("SQL: " + i);
            }
            read.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String bindValues(String query, List<String> parameters) {
        for (String parameter : parameters) {
            query = query.replace("?", parameter);
        }
        return query;
    }

    public static List<String> loadQueryFromFile(String fileName) {
        List<String> queries = new ArrayList<>();
        InputStream is = Utils.class.getClassLoader().getResourceAsStream("/queries/" + fileName);
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
