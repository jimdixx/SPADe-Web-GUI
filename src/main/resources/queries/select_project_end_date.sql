/* Get project end date */
select ifnull(endDate, date_format(now(), "%Y-%m-%d")) as "endDate" FROM project  where id = @projectId;