CREATE TABLE app_metadata (
    id INT AUTO_INCREMENT PRIMARY KEY,
    appDataKey VARCHAR(255),
    appDataValue TEXT
);

INSERT INTO app_metadata (appDataKey, appDataValue) VALUES
    ('basics', '[{
        "version": "1.0.0",
        "authors": [{
            "name": "Ondřej Váně",
            "email": "vaneo@students.zcu.cz"
        }],
        "description": "This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application."
    }, {
        "version": "1.1.0",
        "authors": [{
            "name": "Ondřej Váně",
            "email": "vaneo@students.zcu.cz"
        }],
        "description": "This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application."
    }, {
        "version": "1.2.0",
        "authors": [{
            "name": "Petr Štěpánek",
            "email": "petrs1@students.zcu.cz"
        }],
        "description": "This application is used to detect presence of anti-patterns in project management tools data. Ten selected anti-patterns are implemented in this application."
    }, {
        "version": "2.0.0",
        "authors": [{
            "name": "Petr Štěpánek",
            "email": "petrs1@students.zcu.cz"
        }, {
            "name": "Petr Urban",
            "email": "urbanp@students.zcu.cz"
        }, {
            "name": "Jiří Trefil",
            "email": "trefil@students.zcu.cz"
        }, {
            "name": "Václav Hrabík",
            "email": "hrabikv@students.zcu.cz"
        }],
        "description": "Experience the next evolution of our application with version 2.0.0, featuring a revamped infrastructure, three distinct apps, MS SQL Server integration, a comprehensive user provider system, and contract testing for enhanced reliability."
    }]');
