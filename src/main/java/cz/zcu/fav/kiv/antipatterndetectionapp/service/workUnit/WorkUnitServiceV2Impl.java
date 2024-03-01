package cz.zcu.fav.kiv.antipatterndetectionapp.service.workUnit;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.project.managment.WorkUnitRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.WorkUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class WorkUnitServiceV2Impl implements WorkUnitServiceV2 {

    @Autowired
    WorkUnitRepository workUnitRepository;

    @Autowired
    WorkUnitService workUnitService;

    @Override
    public List<WorkUnit> fetchProjectWorkUnits(long projectId, String category, List <String> type) {
        //no filter applied - return everything
        if(category == null && type == null)
            return this.workUnitRepository.fetchAllProjectWorkUnits(projectId);
        if (category != null){
            //necessary to parse the category string a transform it into a list
            List<String> categories = Arrays.asList(category.split(";"));
            return type == null ?
                    this.workUnitRepository.fetchActivityWorkUnitsFilteredByCategory(projectId, categories) :
                    this.workUnitRepository.fetchActivityWorkUnitsFilteredByTypeAndCategory(projectId, categories, type);
        }
        return this.workUnitRepository.fetchActivityWorkUnitsFilteredByType(projectId, type);
    }
    @Override
    public boolean updateWorkUnitsActivity(long activityId, List<Long> wuIds) {
        int wuCount=  wuIds.size();
        int updatedRows = this.workUnitRepository.updateWuActivity(activityId,wuIds);
        return updatedRows == wuCount;
    }

    @Override
    public List<String> fetchProjectWorkUnitCategories(Long projectId) {

        return this.workUnitRepository.fetchProjectWorkUnitsCategories(projectId);
    }

    @Override
    public List<String> fetchProjectWorkUnitTypes(Long projectId) {
        return this.workUnitRepository.fetchProjectWorkUnitsTypes(projectId);
    }


}
