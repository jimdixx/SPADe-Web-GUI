# AntiPatternDetectionApp
This application was created as part of the thesis. It is used for analysis and detection of anti-patterns in project data.

## Thesis abstract
The goal of this thesis is to analyze and automatically detect anti-patterns in the data of project management tools using SPADe tools. The SPADe tool is used to collect data from ALM tools and search for bad practices (anti-patterns) in project data. In order to develop this thesis, an analysis of the available anti-patterns was performed and then a subset was selected for further investigation. In the next part, the detection of the analyzed anti-patterns was implemented using SQL queries and Java programming language was used to process the results of these queries. As an extra feature of this thesis, going beyond the scope of the original assignment, a support web application for running the detection process of implemented anti-patters and results presentation was also developed. Furthermore, an experiment was performed on a selected sets of anti-patterns and project data. The success of the detection was verified by comparing the results to those from a manual anti-pattern detection in project data from the source ALM tools. Detection success was successful at 93.65 % compared to manual control.

## Analyzed Anti-Patterns
In version 1.0.0 of this application, the detection of the following Anti-Patterns is implemented:
* Business As Usual
* Long Or Non-Existant Feedback Loops
* Ninety-NinetyRule
* RoadToNowhere
* SpecifyNothing
* Too Long Sprint
* Varying Sprint Length

## Run
### Tools to run
It will need the following tools to run the application:
* Docker
* Docker Compose
### Run application
The following list describes the steps to run (you will need):
1. Open terminal.
2. Move to the root folder of this project (AntiPatternDetectionApp).
3. Build docker images with command `docker-compose build`.
4. Create and run all containers with command `docker-compose run`.
5. Open phpMyAdmin on docker address on port 8082.
6. Create database with name spade using command `CREATE DATABASE spade;`.
7. Restore database from file `db_dump.sql` located in project root folder.
8. Run all commands from file `config.sql` located in project root folder.

