/* Id of first iteration */
set @idOfFirstIteration = (select id from iteration where iteration.superProjectId = @projectId order by name limit 1);
