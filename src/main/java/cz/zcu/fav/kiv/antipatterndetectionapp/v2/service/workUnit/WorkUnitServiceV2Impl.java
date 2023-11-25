package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.workUnit;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.WorkUnitRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.WorkUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkUnitServiceV2Impl implements WorkUnitServiceV2 {

    @Autowired
    WorkUnitRepository workUnitRepository;

    @Autowired
    WorkUnitService workUnitService;

    @Override
    public List<WorkUnit> fetchProjectWorkUnits(long projectId) {
        //List<WorkUnit> units = this.workUnitRepository.fetchActivityWorkUnits(activityId);
        List<WorkUnit> units = this.workUnitRepository.fetchAllProjectWorkUnits(projectId);
        return units;
    }
    @Override
    public boolean updateWorkUnitsActivity(long activityId, List<Long> wuIds) {
        int wuCount=  wuIds.size();
        int updatedRows = this.workUnitRepository.updateWuActivity(activityId,wuIds);
        return updatedRows == wuCount;
    }

}
