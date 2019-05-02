# openapi-springboot-sample

springboot microserver sample by generating with openapi-generator

## How to build

to generate source code with open-generator from [spec/openapi.yaml](spec/openapi.yaml)

```bash
$ gradle generateSwaggerCode
```

to show help related to generateSwaggerCode

```bash
$ gradle generateSwaggerCodeOpenapiHelp
```

generated code is in `build/swagger-code-openapi`

```bash
$ tree build/swagger-code-openapi/
build/swagger-code-openapi/
├── README.md
├── pom.xml
└── src
    └── main
        └── java
            └── org
                └── openapitools
                    ├── api
                    │   ├── ApiUtil.java
                    │   ├── BooksApi.java
                    │   ├── PingApi.java
                    │   └── UsersApi.java
                    └── model
                        ├── Book.java
                        ├── InlineResponse200.java
                        ├── InlineResponse201.java
                        └── User.java

7 directories, 10 files
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

## References

* https://swagger.io/specification/
* https://github.com/int128/gradle-swagger-generator-plugin
* https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/spring.md
* https://speakerdeck.com/akihito_nakano/gunmaweb34
