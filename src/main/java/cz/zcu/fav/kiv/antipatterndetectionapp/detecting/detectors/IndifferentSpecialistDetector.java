package cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors;


import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.DatabaseConnection;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

public class IndifferentSpecialistDetector extends AntiPatternDetector {
    @Override
    public boolean analyze(Project analyzedProject, DatabaseConnection databaseConnection) {
        return false;
    }
}
