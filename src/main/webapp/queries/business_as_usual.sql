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
/* Retrospective substring */
set @restrospectiveSubstring = '%retr%';
/* Revision substring */
set @revisionSubstring = '%revi%';
/* Number of iterations for given project */
select COUNT(id) as 'numberOfIterations' from iteration where superProjectId = @projectId;
/* Select all iteration with detected retrospective activities */
select iterationName as 'iterationName', count(name) as 'numberOfIssues' from workUnitView where projectId = @projectId and (name like @restrospectiveSubstring or name like @revisionSubstring or name like '%week%scrum%') group by iterationName;
/* Select all wikipages that were created or updated in iteration and have name with retr or revi*/
select iteration.name as 'iterationName', count(distinct(artifactView.name)) as 'numberOfWikiPages' from artifactView inner join fieldChangeView on artifactView.id = fieldChangeView.itemId inner join iteration on (fieldChangeView.created between iteration.startDate and iteration.endDate) and iteration.superProjectId = @projectId where artifactView.projectId = @projectId and artifactView.artifactClass like 'WIKIPAGE' and (artifactView.name like '%retr%' or artifactView.description like '%retr%') group by iteration.id order by iteration.name;
