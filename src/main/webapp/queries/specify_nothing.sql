/*
Anti-pattern name: Specify nothing

Description: The specification is not done intentionally. Programmers are
             expected to work better without written specifications.

Detection: No specification artifact. There is no issue that will have something
           like "DSP, SPECIFICATIONS, ETC." in the title. Initially, meetings
           with the customer should be more frequent to clarify the project framework.
            No entry in the wiki with the project specification.
*/

/* Init project id */
set @projectId = ?;
/* Find number of wikipages with some project specification */
set @numberOfWikiPages = (select count(name) from artifactView where projectId = @projectId and artifactClass = 'WIKIPAGE' and (lower(name) like lower('§0§') or lower(description) like lower('§0§') or lower(name) like lower('§1§') or lower(description) like lower('§1§') or lower(name) like lower('§2§') or lower(description) like lower('§2§') or lower(name) like lower('§3§') or lower(description) like lower('§3§') or lower(name) like lower('§4§') or lower(description) like lower('§4§') or lower(name) like lower('§5§') or lower(description) like lower('§5§') or lower(name) like lower('§6§') or lower(description) like lower('§6§') or lower(name) like lower('§7§') or lower(description) like lower('§7§') or lower(name) like lower('§8§') or lower(description) like lower('§8§') or lower(name) like lower('§9§') or lower(description) like lower('§9§')));
/* Find activities for creating DSP or project specification */
set @numberOfActivitiesForSpecification = (SELECT count(id) from workUnitView where projectId = @projectId and (lower(name) like lower('§0§') or lower(name) like lower('§1§') or lower(name) like lower('§2§') or lower(name) like lower('§3§') or lower(name) like lower('§4§') or lower(name) like lower('§5§') or lower(name) like lower('§6§') or lower(name) like lower('§7§') or lower(name) like lower('§8§') or lower(name) like lower('§9§')));
/* Count average length of issues description */
set @averageLengthOfIssueDescription = (select AVG(CHAR_LENGTH(workUnitView.description)) from workUnitView where workUnitView.projectId = @projectId);
/* Show all statistics */
select @projectId as `projectId`, @numberOfWikiPages as `numberOfWikiPages`, @numberOfActivitiesForSpecification as `numberOfActivitiesForSpecification`, @averageLengthOfIssueDescription as `averageLengthOfIssueDescription`;
