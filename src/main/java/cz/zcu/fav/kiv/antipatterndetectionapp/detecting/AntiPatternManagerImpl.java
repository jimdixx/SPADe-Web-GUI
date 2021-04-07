package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AntiPatternManagerImpl implements AntiPatternManager {

    public List<QueryResult> analyze(Query query) {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        List<QueryResult> queryResults = new ArrayList<>();


        for (Project project : query.getProjects()) {
            QueryResult queryResult = new QueryResult();
            queryResult.setProject(project);
            List<QueryResultItem> queryResultItems = new ArrayList<>();
            for (AntiPattern antiPattern : query.getAntiPatterns()) {
                QueryResultItem queryResultItem = new QueryResultItem();
                queryResultItem.setAntiPattern(antiPattern);
                queryResultItem.setDetected(getAntiPatternService(antiPattern).analyze(project, databaseConnection));
                queryResultItems.add(queryResultItem);
            }
            queryResult.setQueryResultItems(queryResultItems);
            queryResults.add(queryResult);

        }

        databaseConnection.closeConnection();

        return queryResults;
    }

    private AntiPatternDetector getAntiPatternService(AntiPattern antiPattern) {
        switch (antiPattern.getName()) {
            case "TooLongSprint":
                return new TooLongSprintDetectorDetector();
            case "BusinessAsUsual":
                return new BusinessAsUsualDetector();
            case "VaryingSprintLength":
                return new VaryingSprintLengthDetector();
            case "IndifferentSpecialist":
                return new IndifferentSpecialistDetector();
            case "LongOrNonExistentFeedbackLoops":
                return new LongOrNonExistentFeedbackLoopsDetector();
            case "RoadToNowhere":
                return new RoadToNowhereDetector();
            case "SpecifyNothing":
                return new SpecifyNothingDetector();
        }
        return null;
    }
}
