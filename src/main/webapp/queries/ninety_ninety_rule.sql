/*
Anti-pattern name: Ninety-Ninety Rule

Description: The first 90 percent of the code represents the first 90 percent of development time. The
             remaining 10 percent of the code represents another 90 percent of development time.
             Then decide on a long delay of the project compared to the original estimate.
             The functionality is almost done, some number is already closed and is only waiting
             for one activity to close, but it has been open for a long time.

Detection: In each iteration, find out how much time the activities were estimated and how much time
           they actually took. During the iterations, the estimates and time spent on activities should equal.
*/

/* Init project id */
set @projectId = ?;
/* Select all activities for each iteration and sum estimated time and spent time */
select iterationName, sum(estimatedTime) as 'estimatedTime', sum(spentTime) as 'spentTime', sum(spentTime)/sum(estimatedTime) as 'timeDivision' from workUnitView where projectid = @projectId group by iterationName order by iterationName;
