# The Ultimate Spring Boot Course

 ## Getting Started

 ### Reference Documentation
 For further reference, please consider the following sections:
 * [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
 * [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.5/maven-plugin)
 * [Create an OCI image](https://docs.spring.io/spring-boot/3.4.5/maven-plugin/build-image.html)
 * [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.5/reference/data/sql.html#data.sql.jpa-and-spring-data)
 * [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.5/reference/using/devtools.html)
 * [Flyway Migration](https://docs.spring.io/spring-boot/3.4.5/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
 
 ### Guides
 
 The following guides illustrate how to use some features concretely:
 
 * [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
 * [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
 
 ### Maven Parent overrides
 
 Due to Maven's design, elements are inherited from the parent POM to the project POM.
 While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
 parent.
 To prevent this, the project POM contains empty overrides for these elements.
 If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
 
 ### Additional Links
 
 These additional references should also help you:
 
 * [Configure the Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/3.4.5/maven-plugin/reference/htmlsingle/)
 * [Flyway Migration Reference Guide](https://docs.spring.io/spring-boot/3.4.5/reference/data/sql.html#data.sql.migration.flyway)
 * [Spring Data JPA Reference Guide](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
 
 ## Spring Cloud
 
 This project uses Spring Cloud BOM for dependency management.
 Please make sure that you use Spring Cloud dependencies with the same version as Spring Cloud BOM.
 
 For more information on Spring Cloud BOM, please visit [Spring Cloud Release Train](https://spring.io/projects/spring-cloud-release-train).
 
 ## Spring Boot
 
 This project uses Spring Boot BOM for dependency management.
 Please make sure that you use Spring Boot dependencies with the same version as Spring Boot BOM.
 
 For more information on Spring Boot BOM, please visit [Spring Boot Release Notes](https://spring.io/projects/spring-boot).
