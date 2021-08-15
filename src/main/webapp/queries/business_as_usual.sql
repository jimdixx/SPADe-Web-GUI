/*
Anti-pattern name: Business as usual (No sprint retrospective)

Description: Absence of a retrospective after individual
             iterations or after the completion project.

Detection: There will be no activities in the project
           that would indicate that a retrospective is
           taking place (issue with the name of the
           retrospective, issue on which all team members
           log, issue that is repeated periodically,
           issue to which no commit is bound, issue which
           will be marked as administration or something like that).
           There will be no notes in the wiki or other tool called
           retrospectives (%retr%).
*/

/* Init project id */
set @projectId = ?;
/* Number of iterations for given project */
select COUNT(id) as 'numberOfIterations' from iteration where superProjectId = @projectId and name like '%itera%';
/* Select all iteration with detected retrospective activities */
select iterationName as 'iterationName', count(name) as 'numberOfIssues' from workUnitView where projectId = @projectId and (lower(name) like lower('§0§') or lower(name) like lower('§1§') or lower(name) like lower('§2§') or lower(name) like lower('§3§') or lower(name) like lower('§4§') or lower(name) like lower('§5§') or lower(name) like lower('§6§') or lower(name) like lower('§7§') or lower(name) like lower('§8§') or lower(name) like lower('§9§')) group by iterationName;
/* Select all wikipages that were created or updated in iteration and have name with retr or revi*/
select iteration.name as 'iterationName', count(distinct(artifactView.name)) as 'numberOfWikiPages' from artifactView inner join fieldChangeView on artifactView.id = fieldChangeView.itemId inner join iteration on (fieldChangeView.created between iteration.startDate and iteration.endDate) and iteration.superProjectId = @projectId where artifactView.projectId = @projectId and artifactView.artifactClass like 'WIKIPAGE' and (lower(artifactView.name) like lower('§0§') or lower(artifactView.name) like lower('§1§') or lower(artifactView.name) like lower('§2§') or lower(artifactView.name) like lower('§3§') or lower(artifactView.name) like lower('§4§') or lower(artifactView.name) like lower('§5§') or lower(artifactView.name) like lower('§6§') or lower(artifactView.name) like lower('§7§') or lower(artifactView.name) like lower('§8§') or lower(artifactView.name) like lower('§9§')) group by iteration.id order by iteration.name;
