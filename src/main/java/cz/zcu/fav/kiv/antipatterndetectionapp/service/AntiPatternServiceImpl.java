package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.AntiPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
public class AntiPatternServiceImpl implements AntiPatternService {

    @Autowired
    private AntiPatternRepository antiPatternRepository;

    @Override
    public List<AntiPatternDetector> getAllAntiPatterns() {
        return antiPatternRepository.getAllAntiPatterns();
    }

    @Override
    public AntiPatternDetector getAntiPatternById(Long id) {
        return antiPatternRepository.getAntiPatternById(id);
    }

    @Override
    public List<AntiPattern> antiPatternsToModel(List<AntiPatternDetector> antiPatternDetectors) {
        List<AntiPattern> antiPatterns = new LinkedList<>();
        for (AntiPatternDetector antiPatternDetector : antiPatternDetectors) {
            antiPatterns.add(antiPatternDetector.getAntiPatternModel());
        }
        return antiPatterns;
    }

    @Override
    public AntiPattern antiPatternToModel(AntiPatternDetector antiPatternDetector) {
        return antiPatternDetector.getAntiPatternModel();
    }

    @Override
    public List<AntiPatternDetector> getAllAntiPatternsForGivenIds(Long[] ids) {
        List<AntiPatternDetector> antiPatternDetectors = new ArrayList<>();

        for (Long id : ids) {
            antiPatternDetectors.add(antiPatternRepository.getAntiPatternById(id));
        }
        return antiPatternDetectors;
    }

    @Override
    public boolean saveNewConfiguration(String[] configNames, String[] configValues) {
        List<AntiPatternDetector> antiPatternDetectors = antiPatternRepository.getAllAntiPatterns();

        for (AntiPatternDetector antiPatternDetector : antiPatternDetectors) {
            // not every anti-pattern should have configuration
            if (antiPatternDetector.getAntiPatternModel().getConfigurations() == null) {
                continue;
            }
            for (int i = 0; i < configNames.length; i++) {
                if (antiPatternDetector.getAntiPatternModel().getConfigurations().containsKey(configNames[i])) {

                    if (antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).getValue().getClass() == Integer.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getConfigurations().get(configNames[i]).setValue((Integer.parseInt(configValues[i])));
                        } catch (NumberFormatException e) {
                            return false;
                        }

                    }


                }
            }
        }
        return true;
    }
}
