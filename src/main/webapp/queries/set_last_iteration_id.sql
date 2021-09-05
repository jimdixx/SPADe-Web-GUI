/* Id of last iteration */
set @idOfLastIteration = (select id from iteration where iteration.superProjectId = @projectId order by name desc limit 1);
