/* set second iteration start date to the global values named @firstIterationStartDate */
set @secondIterationStartDate = (select startDate from iteration where superProjectId = @projectId ORDER BY startDate LIMIT 1 offset 1);
