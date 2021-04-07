package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import java.util.List;

/**
 * Model class for store results of one specific projects.
 *
 * project: analyzed project
 * queryResultItems: all analyzed anti-patterns with results and results details
 */
public class QueryResult {
    private Project project;
    private List<QueryResultItem> queryResultItems;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<QueryResultItem> getQueryResultItems() {
        return queryResultItems;
    }

    public void setQueryResultItems(List<QueryResultItem> queryResultItems) {
        this.queryResultItems = queryResultItems;
    }

    public QueryResult() {
    }

    public QueryResult(Project project, List<QueryResultItem> queryResultItems) {
        this.project = project;
        this.queryResultItems = queryResultItems;
    }
}
