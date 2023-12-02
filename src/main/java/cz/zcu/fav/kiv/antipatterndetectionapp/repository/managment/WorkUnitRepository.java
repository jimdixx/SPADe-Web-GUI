package cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface WorkUnitRepository extends JpaRepository<WorkUnit, Long> {

    @Query(value = "SELECT * FROM work_unit " +
            "JOIN work_unit_category ON work_unit.id = work_unit_category.workUnitId " +
            "JOIN category ON category.id = work_unit_category.categoryId " +
            "WHERE category.id IN :categories " +
            "AND work_unit.wuTypeId IN :types " +
            "AND work_unit.projectId = :projectId " +
            "GROUP BY work_unit.id " +
            "HAVING COUNT(DISTINCT category.id) = :size", nativeQuery = true)
    List<WorkUnit> getFiltredWorkUnits(Long projectId, Collection<Long> categories, Collection<Long> types, Integer size);

    @Query(value = "SELECT DISTINCT * FROM work_unit " +
            "JOIN work_unit_category ON work_unit.id = work_unit_category.workUnitId " +
            "JOIN category ON category.id = work_unit_category.categoryId " +
            "WHERE category.id IN :categories " +
            "AND work_unit.projectId = :projectId " +
            "GROUP BY work_unit.id " +
            "HAVING COUNT(DISTINCT category.id) = :size", nativeQuery = true)
    List<WorkUnit> getCategoryFiltredWorkUnits(Long projectId, Collection<Long> categories, Integer size);

    @Query(value = "SELECT DISTINCT * FROM work_unit " +
            "WHERE work_unit.wuTypeId IN :types " +
            "AND work_unit.projectId = :projectId", nativeQuery = true)
    List<WorkUnit> getTypeFiltredWorkUnits(Long projectId, Collection<Long> types);
    @Query("SELECT unit FROM WorkUnit unit WHERE unit.activity.id = ?1 ")
    List<WorkUnit> fetchActivityWorkUnits(Long activityId);
    @Query("SELECT unit from WorkUnit unit WHERE unit.project.id = ?1")
    List<WorkUnit> fetchAllProjectWorkUnits(Long projectId);

    @Query("SELECT unit from WorkUnit unit WHERE unit.project.id = ?1 and ?2 IN (SELECT cat.name from unit.categories cat)")
    List<WorkUnit> fetchActivityWorkUnitsFilteredByCategory(Long projectId, String category);

    @Query("SELECT unit from WorkUnit unit WHERE unit.project.id = ?1 and unit.type.name = ?2")
    List<WorkUnit> fetchActivityWorkUnitsFilteredByType(Long projectId, String type);
    @Query("SELECT unit from WorkUnit unit WHERE unit.project.id = ?1 AND ?2 MEMBER OF unit.categories AND unit.type.name = ?3")
    List<WorkUnit> fetchActivityWorkUnitsFilteredByTypeAndCategory(Long projectId,String category, String type);

    @Query("UPDATE WorkUnit unit SET unit.activity.id = :activityId WHERE unit.id in :wuIds")
    @Modifying
    @Transactional
    int updateWuActivity(@Param("activityId")Long activityId, @Param("wuIds") List<Long> wuIds);
}
