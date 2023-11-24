package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.workUnit;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;

import java.util.List;

public interface WorkUnitServiceV2 {
    List<WorkUnit> fetchProjectWorkUnits(long activityId);
}
