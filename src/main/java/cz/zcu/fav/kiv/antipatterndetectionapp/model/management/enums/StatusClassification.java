package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.Classification;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for status classes. This is Entity class that is loaded from db.
 */
@Entity
@Table(name = "status_classification")
public class StatusClassification implements Classification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="class")
    private String name;

    @Column(name="superclass")
    private String superClass;

    @OneToMany(mappedBy = "classId", orphanRemoval = true)
    private List<Status> statuses = new ArrayList<>();

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

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
