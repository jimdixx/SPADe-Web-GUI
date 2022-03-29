package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Override
    public List<String> getAllConfigurationNames() {
        List<String> configList = new ArrayList<String>();
        File folder = new File(String.valueOf(Paths.get(new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\webapp\\configurations")));
        for (final File fileEntry : folder.listFiles()) {
            configList.add(fileEntry.getName().split("\\.")[0]);
        }
        return configList;
    }

    @Override
    public List<String> getDefaultConfigurationNames() {
        List<String> configList = new ArrayList<String>();
        configList.add("Default");
        return configList;
    }
}
