package contracts

import org.springframework.cloud.contract.spec.Contract

class Author {
    String name = ""
    String email = ""
}

class Version {
    String version = ""
    Author[] array = []
    String description = ""
}

Contract.make {
    description "Get information about app."
    request {
        method GET()
        url("/v2/app/metadata/about")
        headers {
            header("Authorization": "Bearer token")
            contentType applicationJson()
        }
    }
    response {
        body(
               [["version" : '2.0.0',
                 "authors": [["name": "Petr Štěpánek", "email": 'petrs1@students.zcu.cz'],
                             ["name": "Petr Urban", "email": 'urbanp@students.zcu.cz'],
                             ["name": "Jiří Trefil", "email": 'trefil@students.zcu.cz'],
                             ["name": "Václav Hrabík", "email": 'hrabikv@students.zcu.cz']
                            ],
                 "description": 'TODO'],
                ["version": '1.2.0',
                 "authors": [["name": 'Petr Štěpánek', "email": 'petrs1@students.zcu.cz']],
                 "description": 'This application is used to detect presence of anti-patterns in project management tools data. Ten selected anti-patterns are implemented in this application.'
                ],
                ["version": '1.1.0',
                 "authors": ["name": 'Ondřej Váně', "email": 'vaneo@students.zcu.cz'],
                 "description": 'This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application.'
                ],
                ["version": '1.0.0',
                 "authors": ["name": 'Ondřej Váně', "email": 'vaneo@students.zcu.cz'],
                 "description": 'This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application.'
                ]
                ]
        )
        status 200
    }
}