/* Average iteration length */
select avg(abs(dateDiff(iteration.endDate, iteration.startDate))) as 'averageIterationLength' from iteration where superProjectId = @projectId;
