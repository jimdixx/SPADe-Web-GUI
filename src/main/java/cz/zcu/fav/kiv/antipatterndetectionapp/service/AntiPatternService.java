package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;

import java.util.List;

public interface AntiPatternService {

    List<AntiPatternDetector> getAllAntiPatterns();

    AntiPatternDetector getAntiPatternById(Long id);

    List<AntiPattern> antiPatternsToModel(List<AntiPatternDetector> antiPatternDetectors);

    AntiPattern antiPatternToModel(AntiPatternDetector antiPatternDetector);

    List<AntiPatternDetector> getAllAntiPatternsForGivenIds(Long[] ids);


}
