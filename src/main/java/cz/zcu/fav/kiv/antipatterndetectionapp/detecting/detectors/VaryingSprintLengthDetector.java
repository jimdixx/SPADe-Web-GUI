package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;



import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VaryingSprintLengthDetector extends AntiPatternDetector {
    @Override
    public boolean analyze(Project analyzedProject, DatabaseConnection databaseConnection) {

        // Settings
        // Maximum difference between two iterations
        int maximumDaysDifference = 5;
        // How many times the length of the iteration can change
        int maximumIterationChange = 2;

        int counter = 0;

        try {
            ResultSet rs = super.executeQuery(analyzedProject, "./queries/varying_sprint_length.sql", databaseConnection);
            if (rs != null) {
                int firstIterationLength = Integer.MIN_VALUE;
                int secondIterationLength;
                while (rs.next()) {
                    int iterationLength = rs.getInt("iterationLength");
                    if (firstIterationLength == Integer.MIN_VALUE) {
                        firstIterationLength = iterationLength;
                        continue;
                    } else {
                        secondIterationLength = iterationLength;
                    }

                    if (Math.abs(firstIterationLength - secondIterationLength) >= maximumDaysDifference) {
                        counter = counter + 1;
                    }
                    firstIterationLength = secondIterationLength;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter >= maximumIterationChange;
    }
}
