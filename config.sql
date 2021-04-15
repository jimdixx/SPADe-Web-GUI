create or replace view artifactView as select wi.name, wi.description, wi.created, wi.url, a.id as id, a.artifactClass, a.mimeType, a.size, p.id as authorId, p.name as authorName, p.projectId from work_item wi join artifact a on a.id = wi.id join personView p on p.id = wi.authorId;
SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
