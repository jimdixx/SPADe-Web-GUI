package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResultItem;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;

import java.util.List;

/**
 * This is base interface for all AP detector. In case you need to implement new AP detector
 * you should implements all method correctly, see example TooLongSprintDetector.java
 */
public interface AntiPatternDetector {
    /**
     * Method for setting anti-pattern to the detector
     *
     * @param antiPattern ap to set
     */
    void setAntiPattern(AntiPattern antiPattern);

    /**
     * Method for getting name of the json file with anti-pattern details
     *
     * @return json file name
     */
    String getJsonFileName();

    /**
     * Method for getting AP like model class for manipulating on UI.
     *
     * @return model class of AP
     */
    AntiPattern getAntiPatternModel();

    /**
     * Method for getting all sql files names due to loading all queries from files.
     *
     * @return list of filenames
     */
    List<String> getSqlFileNames();

    /**
     * Setter for loaded sql queries.
     *
     * @param queries loaded sql queries
     */
    void setSqlQueries(List<String> queries);

    /**
     * Most important method of this class for analyzing AP. In this mode queries are sent to
     * the database and then the results are processed based on the results it is decided
     * on the occurrence of AP.
     *
     * @param project model class for analyzed project
     * @param databaseConnection database connection
     * @return model class for results
     */
    QueryResultItem analyze(Project project, DatabaseConnection databaseConnection);
}
