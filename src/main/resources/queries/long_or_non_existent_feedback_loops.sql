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

           POZNÁMKY:
                     1) naléz aktivity, které mohou odpovídat schůzce se zákazníkem
                     2) nalezené aktivity zgrupovat podle dueDate
                     3) následně udělat časový rozdíl těchto aktivit (Python)
                     4) a porovnat s průměrnou délkou iterace




*/

/* Init project id */
set @projectId = ?;
/* Number of iterations for project */
set @numberOfIterations = (SELECT count(id) FROM `iteration` WHERE superProjectId = @projectId);
/* Average iteration length */
set @averageIterationLength = (SELECT AVG(abs(datediff(iteration.endDate, iteration.startDate))) FROM `iteration` WHERE superProjectId = @projectId);
/* Look for issues that should indicate customer demo or customer feedback */
select workunitview.duedate as `dueDate`, @numberOfIterations as `numberOfIterations`, @averageIterationLength as `averageIterationLength` FROM `workunitview` where workunitview.projectId = @projectId and (workunitview.name like '%demo%' or workunitview.description like '%demo%' or workunitview.name like '%zákazník%' or workunitview.description like '%zákazník%' or workunitview.name like '%cust%' or workunitview.description like '%cust%' or workunitview.name like '%zadavatel%' or workunitview.description like '%zadavatel%' or workunitview.name like '%předvedení%' or workunitview.description like '%předvedení%' or workunitview.name like '%presentation%' or workunitview.description like '%presentation%') group by workunitview.duedate;



