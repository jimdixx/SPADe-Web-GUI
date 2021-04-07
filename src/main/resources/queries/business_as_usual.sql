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

/* Init global variables */
set @projectId = ?;
/* Retrospective substring */
set @restrospectiveSubstring = '%retr%';
/* Number of developers in project */
set @numberOfPeople =  (select count(DISTINCT assigneeId) from workunitview where projectId = @projectId and assigneeName != 'unknown');
/* Number of iterations for given project */
set @numberOfIterations = (select COUNT(*) from iteration where superProjectId = @projectId);
/* Number of wikipages with substring retr */
set @numberOfWikipageWithRetr = (select count(*) from artifactview where projectId = @projectId AND artifactClass like 'WIKIPAGE' AND (name like @restrospectiveSubstring OR description like @restrospectiveSubstring));
/* Number of issues with retr root ends same day like iteration and all members of team are logging  time on this issue */
set @numberOfRestrospectiveActivities = (select COUNT(distinct activityEndDate) from (select workunitview.id, workunitview.activityEndDate from workunitview INNER JOIN fieldchangeview on workunitview.id = fieldchangeview.itemId where workunitview.projectId = @projectId AND fieldchangeview.changeName LIKE 'LOGTIME' AND (abs(datediff(workunitview.activityEndDate, workunitview.iterationEndDate) = 0)) AND (workunitview.name like @restrospectiveSubstring OR workunitview.description LIKE @restrospectiveSubstring) GROUP by workunitview.id HAVING COUNT(DISTINCT fieldchangeview.authorId) = @numberOfPeople) as test);
/* Show all statistics */
select @projectId as `projectId`, @numberOfPeople as `numberOfPeople`, @numberOfIterations as `numberOfIterations`, @numberOfWikipageWithRetr as `numberOfWikipageWithRetr`, @numberOfRestrospectiveActivities as `numberOfRestrospectiveActivities`;
