package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.workUnit;

import com.sun.istack.NotNull;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;

import java.util.List;
import java.util.Set;

public interface WorkUnitServiceV2 {
    List<WorkUnit> fetchProjectWorkUnits(long activityId, String category, String type);
    boolean updateWorkUnitsActivity(long activityId, List<Long> wuIds);
    Set<String> parseWorkUnitCategories(@NotNull List<WorkUnit> workUnits);
    Set<String> parseWorkUnitTypes(@NotNull List<WorkUnit> workUnits);

}
