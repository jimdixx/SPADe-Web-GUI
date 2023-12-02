package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.workUnit;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.WorkUnitRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.WorkUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public Set<String> parseWorkUnitCategories(List<WorkUnit> workUnits) {
        Set<String> workUnitCategories = new HashSet<>();
        for(WorkUnit unit : workUnits) {
            for(Category categ : unit.getCategories())
                workUnitCategories.add(categ.getName());
        }
        return workUnitCategories;
    }

    @Override
    public Set<String> parseWorkUnitTypes(List<WorkUnit> workUnits) {
        Set<String> workUnitTypes = new HashSet<>();
        for(WorkUnit unit : workUnits) {
            workUnitTypes.add(unit.getType().getName());
        }
        return workUnitTypes;
    }

}
