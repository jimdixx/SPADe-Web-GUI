package cz.zcu.fav.kiv.antipatterndetectionapp.utils;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Utils {
    private final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

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

    public static void sessionRecreate(HttpSession session) {
        session.removeAttribute("activity");
        session.removeAttribute("categoryFilter");
        session.removeAttribute("typeFilter");
    }
}
