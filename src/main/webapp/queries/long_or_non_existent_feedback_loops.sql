/*
Anti-pattern name: Long Or Non-Existant Feedback Loops (No Customer feedback)

Description: Long spacings between customer feedback or no feedback. The customer
             enters the project and sees the final result. In the end, the customer
             may not get what he really wanted. With long intervals of feedback,
             some misunderstood functionality can be created and we have to spend
             a lot of effort and time to redo it.


Detection: How to choose what is the optimal spacing between feedbacks? In ASWI,
           it was mostly after each iteration, ie 2-3 weeks apart. Check if there
           is an activity that is repeated periodically, all team members or
           leaders log time on it (essentially a similar issue as in the anti-Business
           as usual model). Search for an activity named "DEMO", "CUSTOMER", etc.
           Search for some records from the demo in the wiki. Similar to Business as usual.
*/

/* Init project id */
set @projectId = ?;
/* Number of iterations for given project */
select COUNT(id) as 'numberOfIterations' from iteration where superProjectId = @projectId;
/* Average iteration length */
select avg(abs(dateDiff(iteration.endDate, iteration.startDate))) as 'averageIterationLength' from iteration where superProjectId = @projectId;
/* Select number of iterations which contains at least one feedback activity */
select count(*) over () as 'totalCountOfIterationsWithFeedbackActivity' from workUnitView as wuv where wuv.projectId = @projectId and (wuv.name like "§0§" or wuv.name like "§1§" or wuv.name like "§2§" or wuv.name like "§3§" or wuv.name like "§4§" or wuv.name like "§5§" or wuv.name like "§6§" or wuv.name like "§7§" or wuv.name like "§8§" or wuv.name like "§9§") group by wuv.iterationName order by wuv.activityEndDate;
/* Select all activities for feedback loop with last modified date as end date */
select wuv.id, wuv.iterationName, wuv.name, cast(max(fieldChangeView.created) as date) as 'endDate' from workUnitView as wuv inner join fieldChangeView on wuv.id = fieldChangeView.itemId where wuv.projectId = @projectId and (wuv.name like "§0§" or wuv.name like "§1§" or wuv.name like "§2§" or wuv.name like "§3§" or wuv.name like "§4§" or wuv.name like "§5§" or wuv.name like "§6§" or wuv.name like "§7§" or wuv.name like "§8§" or wuv.name like "§9§") GROUP by id order by fieldChangeView.created;
/* Get project start date */
select startDate as 'projectStartDate' from iteration where superProjectId = @projectId order by startDate limit 1;
/* Get project end date */
select endDate as 'projectEndDate' from iteration where superProjectId = @projectId order by endDate desc limit 1;
/* Select all iterations that contains wiki pages which were created or updated in iteration and have name or description that mentions some key words for customer demo*/
select iteration.name as 'iterationWithCustomerFeedback', cast(max(fieldChangeView.created) as date) as 'appointmentDate' from artifactView inner join fieldChangeView on artifactView.id = fieldChangeView.itemId inner join iteration on (fieldChangeView.created between iteration.startDate and iteration.endDate) and iteration.superProjectId = @projectId where artifactView.artifactClass like "WIKIPAGE" and artifactView.projectId = @projectId and length(fieldChangeView.newValue) > length(fieldChangeView.oldValue) and (artifactView.name like "§0§" or artifactView.name like "§1§" or artifactView.name like "§2§" or artifactView.name like "§3§" or artifactView.name like "§4§" or artifactView.name like "§5§" or artifactView.name like "§6§" or artifactView.name like "§7§" or artifactView.name like "§8§" or artifactView.name like "§9§") group by iteration.name order by iteration.name;
