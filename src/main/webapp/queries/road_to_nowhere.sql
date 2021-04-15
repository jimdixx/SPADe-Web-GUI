/*
Anti-pattern name: Road To Nowhere

Description: The project is not sufficiently planned and therefore
             takes place on an ad hoc basis with an uncertain
             outcome and deadline. There is no project plan in the project.

Detection: There is no activity in ALM that would indicate the creation
           of a project plan. There will be no document in the wiki
           called the "Project Plan". Project plan should be created in first or
           second iteration. Also could be detected with field change view. If is
           a lot of changes on issues in the beginning of the iteration so then could
           indicate some planning.
*/
set @projectId = ?;
set @firstIterationStartDate = (select startDate from iteration where superProjectId = @projectId ORDER BY startDate LIMIT 1 offset 0);
set @secondIterationStartDate = (select startDate from iteration where superProjectId = @projectId ORDER BY startDate LIMIT 1 offset 1);
set @numberOfIssuesForProjectPlan = (SELECT count(*) from workunitview where projectId = @projectId  and (workunitview.name like '%plán%projektu%' or workunitview.description like '%plán%projektu%' or workunitview.name like '%project%plan%' or workunitview.description like '%project%plan%' or workunitview.name like '%plan%project%' or workunitview.description like '%plan%project%' or workunitview.name like '%proje%plán%' or workunitview.description like '%proje%plán%') AND (iterationStartDate = @firstIterationStartDate OR iterationStartDate = @secondIterationStartDate));
set @numberOfWikiPagesForProjectPlan = (SELECT count(*) from artifactview where projectId = @projectId AND artifactClass like 'WIKIPAGE' AND (artifactview.name like '%plán%projektu%' or artifactview.description like '%plán%projektu%' or artifactview.name like '%project%plan%' or artifactview.description like '%project%plan%' or artifactview.name like '%plan%project%' or artifactview.description like '%plan%project%' or artifactview.name like '%proje%plán%' or artifactview.description like '%proje%plán%'));
select @projectId as `projectId`, @numberOfIssuesForProjectPlan as `numberOfIssuesForProjectPlan`, @numberOfWikiPagesForProjectPlan as `numberOfWikiPagesForProjectPlan`;
