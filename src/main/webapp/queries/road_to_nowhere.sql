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
set @numberOfIssuesForProjectPlan = (SELECT count(*) from workUnitView where projectId = @projectId  and (lower(workUnitView.name) like lower('%plán projektu%') or lower(workUnitView.description) like lower('%plán projektu%') or lower(workUnitView.name) like lower('%project plan%') or lower(workUnitView.description) like lower('%project plan%') or lower(workUnitView.name) like lower('%plan project%') or lower(workUnitView.description) like lower('%plan project%') or lower(workUnitView.name) like lower('%projektový plán%') or lower(workUnitView.description) like lower('%projektový plán%')) AND (iterationStartDate = @firstIterationStartDate OR iterationStartDate = @secondIterationStartDate));
set @numberOfWikiPagesForProjectPlan = (SELECT count(*) from artifactView where projectId = @projectId AND artifactClass like 'WIKIPAGE' AND (lower(artifactView.name) like lower('%plán projektu%') or lower(artifactView.description) like lower('%plán projektu%') or lower(artifactView.name) like lower('%plán projektu%') or lower(artifactView.description) like lower('%plán projektu%') or lower(artifactView.name) like lower('%project plan%') or lower(artifactView.description) like lower('%project plan%') or lower(artifactView.name) like lower('%plan project%') or lower(artifactView.description) like lower('%plan project%') or lower(artifactView.name) like lower('%projekotvý plán%') or lower(artifactView.description) like lower('%projektový plán%')));
select @projectId as `projectId`, @numberOfIssuesForProjectPlan as `numberOfIssuesForProjectPlan`, @numberOfWikiPagesForProjectPlan as `numberOfWikiPagesForProjectPlan`;
