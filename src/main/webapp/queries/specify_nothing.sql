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
set @numberOfWikiPages = (select count(name) from artifactview where projectId = @projectId and (name like '%DSP%' or name like '%specifikace%' or name like '%specification%' or description like '%DSP%' or description like '%specifikace%' or description like '%specification%'));
/* Find activities for creating DSP or project specification */
set @numberOfActivitiesForSpecification = (SELECT count(id) from workunitview where projectId = @projectId and (name like '%DSP%' or name like '%specifikace%' or name like '%specification%'));
/* Count average length of issues description */
set @averageLengthOfIssueDescription = (select AVG(CHAR_LENGTH(workunitview.description)) from workunitview where workunitview.projectId = @projectId);
/* Show all statistics */
select @projectId as `projectId`, @numberOfWikiPages as `numberOfWikiPages`, @numberOfActivitiesForSpecification as `numberOfActivitiesForSpecification`, @averageLengthOfIssueDescription as `averageLengthOfIssueDescription`;
