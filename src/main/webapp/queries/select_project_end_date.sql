/* Get project end date */
select endDate as 'projectEndDate' from iteration where superProjectId = @projectId order by endDate desc limit 1;
