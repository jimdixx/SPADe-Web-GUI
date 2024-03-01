package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.enums.ResolutionClassification;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.interfaces.Classification;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.project.managment.enums.ResolutionClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ResolutionClassificationServiceImpl implements ResolutionClassificationService {

    @Autowired
    private ResolutionClassificationRepository resolutionClassificationRepository;

    @Override
    public List<Classification> getAllClasses() {
        List<ResolutionClassification> resolutions = getAllResolutionClasses();
        return new ArrayList<>(resolutions);
    }

    @Override
    public List<ResolutionClassification> getAllResolutionClasses() {
        List<ResolutionClassification> resolutions = resolutionClassificationRepository.findAll();
        Collections.sort(resolutions, Comparator.comparing(ResolutionClassification::getId));
        return resolutions;
    }

    @Override
    public Classification getClassById(Long id) {
        return resolutionClassificationRepository.getOne(id);
    }
}
