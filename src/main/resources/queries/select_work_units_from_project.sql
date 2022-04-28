/* Select ids of work units of a project */
select wuv.id from work_unit wuv where wuv.projectId = @projectId;