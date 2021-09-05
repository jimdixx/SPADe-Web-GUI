/* !Global variables must be set for proper behavior! Select all iterations with their length without first and last iterations */
select datediff(endDate, startDate) as `iterationLength` from iteration where iteration.superProjectId = @projectId and iteration.id != @idOfFirstIteration and iteration.id != @idOfLastIteration order by iteration.name;
