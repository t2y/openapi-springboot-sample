# openapi-springboot-sample

springboot microserver sample by generating with openapi-generator

* see also [swagger-codegen sample](https://github.com/t2y/swagger-springboot-sample)

## How to build

to generate source code with open-generator from [spec/openapi.yaml](spec/openapi.yaml)

```bash
$ gradle clean openApiGenerate
```

to show help related to openApiGenerate

```bash
$ gradle openApiGenerators
```

generated code is in `build/generated`

```bash
$ tree build/generated/
build/generated/
├── README.md
├── pom.xml
└── src
    └── main
        ├── java
        │   └── org
        │       └── openapitools
        │           ├── api
        │           │   ├── ApiUtil.java
        │           │   ├── BooksApi.java
        │           │   ├── BooksApiController.java
        │           │   ├── PingApi.java
        │           │   ├── PingApiController.java
        │           │   ├── StreamApi.java
        │           │   ├── StreamApiController.java
        │           │   ├── UsersApi.java
        │           │   └── UsersApiController.java
        │           ├── configuration
        │           │   ├── HomeController.java
        │           │   └── OpenAPIDocumentationConfig.java
        │           ├── invoker
        │           │   ├── OpenAPI2SpringBoot.java
        │           │   └── RFC3339DateFormat.java
        │           └── model
        │               ├── Book.java
        │               ├── Format.java
        │               ├── Frequency.java
        │               ├── GenericError.java
        │               ├── InlineResponse200.java
        │               ├── InlineResponse201.java
        │               ├── StreamFormat.java
        │               └── User.java
        └── resources
            └── application.properties

10 directories, 24 files
```

## How to boot

```bash
$ gradle bootRun
```

or

```bash
$ gradle build
$ java -jar build/libs/openapi-springboot-sample.jar
```
(but there is no data, just confirm that generated jar can boot up)

## request to localhost

```bash
$ gradle bootRun
```

### ping

```bash
$ curl -s -v -X GET localhost:8080/ping | jq .
{
  "message": "pong"
}
```

### list

```bash
$ curl -s -v -X GET -H 'Auth-Token: secret' localhost:8080/users/list | jq .
```

### create

```bash
$ curl -s -v -X POST -H 'Auth-Token: passwd' -H 'Content-Type: application/json' localhost:8080/users/create -d '{"id": 4, "username": "sala", "phone": "000-123-456", "birthday": "2010-04-01"}' | jq .
{
  "id": 4
}
```

### retrieve

```bash
$ curl -s -v -X GET -H 'Auth-Token: passwd' localhost:8080/users/4 | jq .
{
  "id": 4,
  "username": "sala",
  "firstName": null,
  "lastName": null,
  "birthday": "2010-04-01",
  "email": null,
  "password": null,
  "phone": "000-123-456",
  "userStatus": null
}
```

### update

```bash
$ curl -s -v -X PUT -H 'Auth-Token: passwd' -H 'Content-Type: application/json' localhost:8080/users/4 -d '{"firstName": "sala", "lastName": "smith"}' | jq .
< HTTP/1.1 200 OK
$ curl -s -v -X GET -H 'Auth-Token: passwd' localhost:8080/users/4 | jq .
{
  "id": 4,
  "username": null,
  "firstName": "sala",
  "lastName": "smith",
  "birthday": null,
  "email": null,
  "password": null,
  "phone": null,
  "userStatus": null
}
```

### delete

```bash
$ curl -s -v -X DELETE -H 'Auth-Token: passwd' localhost:8080/users/4 | jq .
< HTTP/1.1 200 OK
$ curl -s -v -X GET -H 'Auth-Token: passwd' localhost:8080/users/4 | jq .
< HTTP/1.1 404 Not Found
```

### stream

```bash
$ curl -s -v "localhost:8080/stream/byte?format=csv"
$ curl -s -v "localhost:8080/stream/input?format=csv"
```

## References

* https://github.com/OAI/OpenAPI-Specification
* https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/spring.md
* https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-gradle-plugin/README.adoc
* https://speakerdeck.com/akihito_nakano/gunmaweb34
* https://openapi-generator.tech/docs/usage
