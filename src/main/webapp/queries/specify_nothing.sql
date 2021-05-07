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
set @numberOfWikiPages = (select count(name) from artifactView where projectId = @projectId and artifactClass = 'WIKIPAGE' and (lower(name) like lower('%dsp%') or lower(name) like lower('%specifikace%') or lower(name) like lower('%specification%') or lower(name) like lower('%vize%proj%') or lower(name) like lower('%vize%produ%') or lower(description) like lower('%DSP%') or lower(description) like lower('%specifikace%') or lower(description) like lower('%specification%')));
/* Find activities for creating DSP or project specification */
set @numberOfActivitiesForSpecification = (SELECT count(id) from workUnitView where projectId = @projectId and (lower(name) like lower('%DSP%') or lower(name) like lower('%specifikace%') or lower(name) like lower('%specification%') or lower(name) like lower('%vize%proj%')));
/* Count average length of issues description */
set @averageLengthOfIssueDescription = (select AVG(CHAR_LENGTH(workUnitView.description)) from workUnitView where workUnitView.projectId = @projectId);
/* Show all statistics */
select @projectId as `projectId`, @numberOfWikiPages as `numberOfWikiPages`, @numberOfActivitiesForSpecification as `numberOfActivitiesForSpecification`, @averageLengthOfIssueDescription as `averageLengthOfIssueDescription`;
