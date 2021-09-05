/* Number of iterations for given project */
select COUNT(id) as 'numberOfIterations' from iteration where superProjectId = @projectId and name like '%itera%';
