package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.SelectedWorkUnitsDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.WorkUnitDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.workUnit.WorkUnitServiceV2;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ClassToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.WorkUnitToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequestMapping("v2/management")
@Controller
public class WorkUnitController {
    @Autowired
    WorkUnitServiceV2 workUnitService;

    @GetMapping("/activity_work_units")
    public ResponseEntity<String> getActivityWorkUnits(Long projectId) {
        if(projectId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<WorkUnit> units = this.workUnitService.fetchProjectWorkUnits(projectId);
        if(units.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ClassToDto<WorkUnit, WorkUnitDto> mapper = new WorkUnitToDto();
        List<WorkUnitDto> dto = mapper.convert(units);
        return ResponseEntity.ok(new Gson().toJson(dto));
    }

    @PutMapping("/activity_work_units")
    public ResponseEntity<String> updateActivityWorkUnits(@RequestBody SelectedWorkUnitsDto selectedWorkUnitsRequest) {
        if(selectedWorkUnitsRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        long activityId = selectedWorkUnitsRequest.getActivityId();
        List<Long> wuIds = selectedWorkUnitsRequest.getWuIds();
        boolean updated = this.workUnitService.updateWorkUnitsActivity(activityId, wuIds);
        if(!updated) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new  ResponseEntity<>(HttpStatus.OK);
    }





}
