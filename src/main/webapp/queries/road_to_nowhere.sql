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
set @numberOfIssuesForProjectPlan = (SELECT count(*) from workUnitView where projectId = @projectId  and (workUnitView.name like '%plán%projektu%' or workUnitView.description like '%plán%projektu%' or workUnitView.name like '%project%plan%' or workUnitView.description like '%project%plan%' or workUnitView.name like '%plan%project%' or workUnitView.description like '%plan%project%' or workUnitView.name like '%proje%plán%' or workUnitView.description like '%proje%plán%') AND (iterationStartDate = @firstIterationStartDate OR iterationStartDate = @secondIterationStartDate));
set @numberOfWikiPagesForProjectPlan = (SELECT count(*) from artifactView where projectId = @projectId AND artifactClass like 'WIKIPAGE' AND (artifactView.name like '%plán%projektu%' or artifactView.description like '%plán%projektu%' or artifactView.name like '%project%plan%' or artifactView.description like '%project%plan%' or artifactView.name like '%plan%project%' or artifactView.description like '%plan%project%' or artifactView.name like '%proje%plán%' or artifactView.description like '%proje%plán%'));
select @projectId as `projectId`, @numberOfIssuesForProjectPlan as `numberOfIssuesForProjectPlan`, @numberOfWikiPagesForProjectPlan as `numberOfWikiPagesForProjectPlan`;
