package cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.interfaces.DatabaseObject;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for iteration. This is Entity class that is loaded from db.
 */
@Entity
@Table(name = "iteration")
public class Iteration implements DatabaseObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="externalid")
    private String externalId;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="enddate")
    private Date endDate;

    @Column(name="startdate")
    private Date startDate;

    @Column(name="created")
    private Timestamp created;

    @ManyToOne
    @JoinColumn(name = "superprojectid")
    private Project superProjectId;

    @OneToMany(mappedBy = "iteration", orphanRemoval = true)
    private List<WorkUnit> workUnits = new ArrayList<>();

    /**
     * Default constructor for Iteration
     */
    public Iteration() {}

    /**
     * Constructor for Iteration
     * @param externalId    External ID from ALM tool
     * @param name          Name of the Iteration
     * @param description   Description of the Iteration
     */
    public Iteration(String externalId, String name, String description) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor for Iteration with project relation
     * @param externalId        External ID from ALM tool
     * @param name              Name of the Iteration
     * @param description       Description of the Iteration
     * @param superProjectId    Project belonging to Iteration
     */
    public Iteration(String externalId, String name, String description, Project superProjectId) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        this.superProjectId = superProjectId;
    }

    /**
     * Constructor for Iteration with all attributes
     * @param externalId        External ID from ALM tool
     * @param name              Name of the Iteration
     * @param description       Description of the Iteration
     * @param endDate           End date of Iteration
     * @param startDate         Start date of Iteration
     * @param created           Created date of Iteration
     * @param superProjectId    Project belonging to Iteration
     */
    public Iteration(String externalId, String name, String description, Date endDate,
                     Date startDate, Timestamp created, Project superProjectId) {
        this.externalId = externalId;
        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.startDate = startDate;
        this.created = created;
        this.superProjectId = superProjectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Project getSuperProjectId() {
        return superProjectId;
    }

    public void setSuperProjectId(Project superProjectId) {
        this.superProjectId = superProjectId;
    }

    public List<WorkUnit> getWorkUnits() {
        return workUnits;
    }

    public void setWorkUnits(List<WorkUnit> workUnits) {
        this.workUnits = workUnits;
    }

    @Override
    public String getObjectName() {
        return "iteration";
    }

    @Override
    public String getAttributeValue(String attr) {
        if(attr.compareTo("id") == 0) {
            return getId().toString();
        } else if(attr.compareTo("name") == 0) {
            return getName();
        } else if (attr.compareTo("description") == 0) {
            return getDescription();
        } else if (attr.compareTo("externalId") == 0) {
            return getExternalId();
        } else if (attr.compareTo("endDate") == 0) {
            return getEndDate().toString();
        } else if (attr.compareTo("startDate") == 0) {
            return getStartDate().toString();
        } else if (attr.compareTo("created") == 0) {
            return getCreated().toString();
        } else {
            return "";
        }
    }
}
