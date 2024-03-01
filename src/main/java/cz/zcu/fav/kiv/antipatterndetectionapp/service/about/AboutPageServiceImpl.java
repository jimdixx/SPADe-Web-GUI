package cz.zcu.fav.kiv.antipatterndetectionapp.service.about;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.app.Metadata;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.app.AboutPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of service that processes data required from AboutPageController.java
 *
 * @author Petr Urban
 * @since 2023-04-26
 * @version 1.0.0
 */
@Service
public class AboutPageServiceImpl implements AboutPageService {

    @Autowired
    private AboutPageRepository repository;

    @Override
    public List<Metadata> findAll() {
        return repository.findAll();
    }

}
