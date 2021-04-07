package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Component
public class AntiPatternRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(AntiPatternRepository.class);

    private Map<Long ,AntiPatternDetector> antiPatternDetectors = init();

    private Map<Long ,AntiPatternDetector> init() {
        Map<Long ,AntiPatternDetector> antiPatterns = new HashMap<>();
        try {
            Reflections reflections = new Reflections("cz.zcu.fav.kiv.antipatterndetectionapp");
            Set<Class<? extends AntiPatternDetector>> subTypes = reflections.getSubTypesOf(AntiPatternDetector.class);
            for (Class<? extends AntiPatternDetector> subType : subTypes) {
                AntiPatternDetector antiPatternDetector = subType.getDeclaredConstructor().newInstance();
                antiPatterns.putIfAbsent(antiPatternDetector.getAntiPatternId(), antiPatternDetector);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            LOGGER.error("Cannot get all detectors with reflection", e);
        }
        return antiPatterns;
    }

    public List<AntiPatternDetector> getAllAntiPatterns() {
        return new ArrayList<>(this.antiPatternDetectors.values());
    }

    public AntiPatternDetector getAntiPatternById(Long id) {
        return this.antiPatternDetectors.getOrDefault(id, null);
    }
}
