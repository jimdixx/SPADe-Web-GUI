package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import java.util.List;

/**
 * Model class for query.
 */
public class Query {
    private List<Project> projects;
    private List<AntiPattern> antiPatterns;

    public Query(List<Project> projects, List<AntiPattern> antiPatterns) {
        this.projects = projects;
        this.antiPatterns = antiPatterns;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<AntiPattern> getAntiPatterns() {
        return antiPatterns;
    }

    public void setAntiPatterns(List<AntiPattern> antiPatterns) {
        this.antiPatterns = antiPatterns;
    }
}
