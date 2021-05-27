

# Authentication API.

_REST API for user authentication with a JWT, role, and permissions validation_

## Starting 🚀

_These instructions will allow you to get a copy of the project running on your local machine for development and testing purposes._

See **Deployment** to know how to deploy the project.


### Requirements 📋

_What things do you need to install the application?_

```  
Maven  
Eclipse or IntelliJ (I recommend it)  
Database manager  
Postman  
```  

### Installation🔧

_Clone the repository._

```  
git clone https://github.com/EdwinLopez12/authentication-api.git  
```  

_Open the project with the IDE or editor to add the necessary DB credentials in the `application-dev.properties`_

```  
spring.datasource.username= spring.datasource.password=  
```  
_Add mail server credentials._

```  
spring.mail.host= spring.mail.port= spring.mail.username= spring.mail.password= spring.mail.protocol=  
```  

_how to use it?_  
_Go to `src/test/resources/postman request` and import it on Postman._



## Deployment 📦

_Use a [Heroku](https://www.heroku.com/) to deploy the application most simply._

_What things do you need to deploy the application?_


- Create a new application on Heroku.
- Add PostgreSQL and get the credentials to set up in `application-dev.properties`.
- Create a new file on the root folder named `system.properties` and add the java runtime version `java.runtime.version=11`
- Go to deploy and follow the steps.

## Developed with 🛠️

* [Spring boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [JWT](https://jwt.io/)

## Contributing 🖇️

If you have something in mind, make a Pull Requests.

## Versions📌

We use [SemVer](http://semver.org/) for versions. For all available versions, look at the [tags in this repository](https://github.com/EdwinLopez12/authentication-api/tags).

## Authors ✒️

**Edwin López** - *Original author* - [EdwinLopez12](https://github.com/EdwinLopez12).


## License 📄

This project is under the MIT License - see the [LICENSE.md](https://github.com/EdwinLopez12/AuthenticationApi/blob/main/LICENSE) file for details.
  
---  
⌨️ with ❤️ by [Edwin Lopez](https://github.com/EdwinLopez12/) 😊.