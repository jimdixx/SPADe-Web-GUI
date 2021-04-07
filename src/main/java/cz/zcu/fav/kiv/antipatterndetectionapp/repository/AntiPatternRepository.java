package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class AntiPatternRepository {

    private List<AntiPatternDetector> antiPatternDetectors = init();

    private List<AntiPatternDetector> init() {
        List<AntiPatternDetector> antiPatterns = new LinkedList<>();
        try {
            Reflections reflections = new Reflections("cz.zcu.fav.kiv.antipatterndetectionapp");
            Set<Class<? extends AntiPatternDetector>> subTypes = reflections.getSubTypesOf(AntiPatternDetector.class);
            for (Class<? extends AntiPatternDetector> subType : subTypes) {
                antiPatterns.add(subType.getDeclaredConstructor().newInstance());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            // TODO LOG
        }
        return antiPatterns;
    }

    public List<AntiPatternDetector> getAllAntiPatterns() {
        return antiPatternDetectors;
    }

    public AntiPatternDetector getAntiPatternById(Long id) {
        for (AntiPatternDetector antiPatternDetector : antiPatternDetectors) {
            if(antiPatternDetector.getAntiPatternId().equals(id)) {
                return antiPatternDetector;
            }
            return antiPatternDetector;
        }
        return null;
    }
}
