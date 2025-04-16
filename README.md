# springbootplusdbunit
a simple test project with spring boot and dbunit

![Code scanning - action](https://github.com/ZeeD/springbootplusdbunit/workflows/Code%20scanning%20-%20action/badge.svg)

![Java CI with Maven](https://github.com/ZeeD/springbootplusdbunit/workflows/Java%20CI%20with%20Maven/badge.svg)

* depends on `spring-boot-starter-data-jpa` and `spring-boot-starter-web`
* uses a pg db "on prod" and h2 for the tests
* (additionally) relies on springdoc and lombok for niceties
* and uses dbunit to set up scenarios

## Usage

* write your "normal" sql db + jpa stuff + rest endpoint
* set your "normal" connecion url in main `application.properties`
* set you test connection url in test `application.properties` to point to h2

