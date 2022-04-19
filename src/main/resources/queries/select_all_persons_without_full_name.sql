/* Select all persons without full name */
select pv.id from personView pv where pv.projectId = @projectId and pv.name not like '% %';