/* set first iteration start date to the global values named @firstIterationStartDate */
set @firstIterationStartDate = (select startDate from iteration where superProjectId = @projectId ORDER BY startDate LIMIT 1 offset 0);
