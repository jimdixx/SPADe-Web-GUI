package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import java.util.List;

/**
 * Model class for cacheables values.
 */
public class CacheablesValues {

    // these values ​​store information about currently processed projects and anti-patterns
    private String[] analyzedProjects;
    private String[] analyzedAntiPatterns;

    // this value indicate if configuration values have changed
    private boolean configurationChanged = false;
    private List<QueryResult> results;

    public String[] getAnalyzedProjects() {
        return analyzedProjects;
    }

    public void setAnalyzedProjects(String[] analyzedProjects) {
        this.analyzedProjects = analyzedProjects;
    }

    public String[] getAnalyzedAntiPatterns() {
        return analyzedAntiPatterns;
    }

    public void setAnalyzedAntiPatterns(String[] analyzedAntiPatterns) {
        this.analyzedAntiPatterns = analyzedAntiPatterns;
    }

    public boolean isConfigurationChanged() {
        return configurationChanged;
    }

    public void setConfigurationChanged(boolean configurationChanged) {
        this.configurationChanged = configurationChanged;
    }

    public List<QueryResult> getResults() {
        return results;
    }

    public void setResults(List<QueryResult> results) {
        this.results = results;
    }
}
