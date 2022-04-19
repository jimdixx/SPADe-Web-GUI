/* Select ids of work units of a project */
select wuv.id, wuv.authorId from workUnitView wuv where wuv.projectId = @projectId;