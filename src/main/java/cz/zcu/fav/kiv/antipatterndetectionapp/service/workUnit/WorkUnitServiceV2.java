package cz.zcu.fav.kiv.antipatterndetectionapp.service.workUnit;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.WorkUnit;

import java.util.List;

public interface WorkUnitServiceV2 {
    List<WorkUnit> fetchProjectWorkUnits(long activityId, String category, List <String> type);
    boolean updateWorkUnitsActivity(long activityId, List<Long> wuIds);

    List<String> fetchProjectWorkUnitCategories(Long projectId);
    List<String> fetchProjectWorkUnitTypes(Long projectId);



}
