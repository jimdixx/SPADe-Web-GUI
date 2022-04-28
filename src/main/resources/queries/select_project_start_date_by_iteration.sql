/* Get project start date */
select startDate as 'projectStartDate' from iteration where superProjectId = @projectId order by startDate limit 1;
