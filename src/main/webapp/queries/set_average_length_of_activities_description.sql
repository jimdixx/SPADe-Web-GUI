/* Count average length of issues description */
set @averageLengthOfIssueDescription = (select AVG(CHAR_LENGTH(workUnitView.description)) from workUnitView where workUnitView.projectId = @projectId);
