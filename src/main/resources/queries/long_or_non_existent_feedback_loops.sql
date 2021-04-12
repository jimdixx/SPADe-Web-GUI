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
/* Select all iteration with detected retrospective activities */
select  iterationName as 'iterationName', count(name) as 'numberOfIssues' from workunitview where projectId = @projectId and (name like "%schůz%zákazník%" OR name like "%předvedení%zákazník%" OR name LIKE "%zákazn%demo%" OR name like "%schůz%zadavat%" OR name like "%inform%schůz%") group by iterationName;
/* Look up for wiki pages */

