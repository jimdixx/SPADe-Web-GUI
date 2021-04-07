package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

public class LongOrNonExistentFeedbackLoopsDetector extends AntiPatternDetector {

    @Override
    public boolean analyze(Project analyzedProject, DatabaseConnection databaseConnection) {
        /*

        int counter = 0;
        long daysTolerance = 3;
        boolean isFirstIteration = true;
        int numberOfIterations = 0;
        double averageLengthOfIteration = 0;
        Date firstIterationDueDate = null;
        Date secondIterationDueDate = null;

        try {
            ResultSet rs = super.executeQuery(analyzedProject, "./queries/long_or_non_existent_feedback_loops.sql", databaseConnection);
            if (rs != null) {


                while (rs.next()) {
                    if (isFirstIteration) {
                        numberOfIterations = rs.getInt("numberOfIterations");
                        averageLengthOfIteration = rs.getDouble("averageIterationLength");
                        firstIterationDueDate = rs.getDate("dueDate");
                        isFirstIteration = false;
                        continue;
                    }

                    secondIterationDueDate = rs.getDate("dueDate");
                    long numberOfDatesBetween = Utils.daysBetween(firstIterationDueDate, secondIterationDueDate);
                    firstIterationDueDate = secondIterationDueDate;
                    if (Math.abs(numberOfDatesBetween - averageLengthOfIteration) <= daysTolerance) {
                        counter++;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return counter != numberOfIterations;*/
        return false;
    }
}
