use [authspade];
begin transaction create_enviroment
if not exists (select * from sysobjects where name='configurations' and xtype='U')
create table configurations (
	id int identity(1, 1),
	configHash nvarchar(255),
	config nvarchar(max) not null,
	isDefault char(1) not null,
	PRIMARY KEY(id)
);

if not exists (select * from sysobjects where name='users' and xtype='U')
create table users(
id int identity(1,1),
email nvarchar(255) not null,
name nvarchar(255) not null,
password varchar(255) not null
PRIMARY KEY(id)
);


--rozkladova tabulka mapujici uzivatele a k nemu asociovane konfigurace
if not exists (select * from sysobjects where name='user_configurations' and xtype='U')
create table user_configurations (
	userId int not null,
	configId int not null,
	foreign key(userId) references users(id),
	foreign key(configId) references configurations(id),
	primary key(userId,configId)
)

insert into configurations (config, isDefault) values (
	'{
    "configuration": [
        {
            "antiPattern": "TooLongSprint",
            "thresholds": [
                {
                    "thresholdName": "maxIterationLength",
                    "value": "21"
                },
                {
                    "thresholdName": "maxNumberOfTooLongIterations",
                    "value": "0"
                }
            ]
        },
        {
            "antiPattern": "VaryingSprintLength",
            "thresholds": [
                {
                    "thresholdName": "maxDaysDifference",
                    "value": "7"
                },
                {
                    "thresholdName": "maxIterationChanged",
                    "value": "1"
                }
            ]
        },
        {
            "antiPattern": "BusinessAsUsual",
            "thresholds": [
                {
                    "thresholdName": "divisionOfIterationsWithRetrospective",
                    "value": "66.66f"
                },
                {
                    "thresholdName": "searchSubstringsWithRetrospective",
                    "value": "%retr%||%revi%||%week%scrum%"
                }
            ]
        },
        {
            "antiPattern": "SpecifyNothing",
            "thresholds": [
                {
                    "thresholdName": "minNumberOfWikiPagesWithSpecification",
                    "value": "1"
                },
                {
                    "thresholdName": "minNumberOfActivitiesWithSpecification",
                    "value": "1"
                },
                {
                    "thresholdName": "minAvgLengthOfActivityDescription",
                    "value": "150"
                },
                {
                    "thresholdName": "searchSubstringsWithProjectSpecification",
                    "value": "%dsp%||%specifikace%||%specification%||%vize%proj%||%vize%produ%"
                }
            ]
        },
        {
            "antiPattern": "RoadToNowhere",
            "thresholds": [
                {
                    "thresholdName": "minNumberOfWikiPagesWithProjectPlan",
                    "value": "1"
                },
                {
                    "thresholdName": "minNumberOfActivitiesWithProjectPlan",
                    "value": "1"
                },
                {
                    "thresholdName": "searchSubstringsWithProjectPlan",
                    "value": "%plán projektu%||%project plan%||%plan project%||%projektový plán%"
                }
            ]
        },
        {
            "antiPattern": "LongOrNonExistentFeedbackLoops",
            "thresholds": [
                {
                    "thresholdName": "divisionOfIterationsWithFeedbackLoop",
                    "value": "50.00f"
                },
                {
                    "thresholdName": "maxGapBetweenFeedbackLoopRate",
                    "value": "2f"
                },
                {
                    "thresholdName": "searchSubstringsWithFeedbackLoop",
                    "value": "%schùz%zákazník%||%pøedvedení%zákazník%||%zákazn%demo%||%schùz%zadavat%||%inform%schùz%||%zákazn%||%zadavatel%"
                }
            ]
        },
        {
            "antiPattern": "NinetyNinetyRule",
            "thresholds": [
                {
                    "thresholdName": "maxDivisionRange",
                    "value": "1.25f"
                },
                {
                    "thresholdName": "maxBadDivisionLimit",
                    "value": "2"
                }
            ]
        },
        {
            "antiPattern": "UnknownPoster",
            "thresholds": [
                {
                    "thresholdName": "searchSubstringsInvalidNames",
                    "value": "%unknown%||%anonym%"
                }
            ]
        },
        {
            "antiPattern": "BystanderApathy",
            "thresholds": [
                {
                    "thresholdName": "searchSubstringsInvalidContributors",
                    "value": "%dependabot%"
                },
                {
                    "thresholdName": "maximumPercentageOfTasksWithoutTeamwork",
                    "value": "30f"
                }
            ]
        },
        {
            "antiPattern": "YetAnotherProgrammer",
            "thresholds": [
                {
                    "thresholdName": "maxNumberOfNewContributors",
                    "value": "5"
                },
                {
                    "thresholdName": "numberOfFirstMonthsWithoutDetection",
                    "value": "2"
                }
            ]
        }
    ]
}',
	'Y'


)

commit
