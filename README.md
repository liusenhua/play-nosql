Play - NoSQL
======
This project is to demonstrate how to user various NoSQL DBs in Scala Play2, as well as document REST APIs with Swagger.

-------
## Play2
[Play 2.3x documentation](https://www.playframework.com/documentation/2.3.x/ScalaActions) 

------
## Couchbase integration
### 0. Reference urls
[Couchbase](http://developer.couchbase.com/documentation/server/4.0/admin/admin-intro.html)  
[Integration](https://github.com/ReactiveCouchbase/ReactiveCouchbase-play/blob/master/README.md)
### 1. Add Dependencies
```
    libraryDependencies += "org.reactivecouchbase" %% "reactivecouchbase-play" % "0.3"
```
### 2. Enable couchbase-play plugin by adding below lines in conf/play.plugins
```
    400:org.reactivecouchbase.play.plugins.CouchbasePlugin
```
### 3. Add couchbase plugin configuration in application.conf
```
    couchbase {
      actorctx {
        timeout = 10000
        execution-context {
          fork-join-executor {
            parallelism-factor = 4.0
            parallelism-max = 40
          }
        }
      }
      buckets = [{
        host = "localhost"
        port = "8091"
        base = "pools"
        bucket = "bank"
        user = ""
        pass = ""
        timeout = "0"
      }]
      failfutures = true
    }
```
### 4. Import libraries
```
    // For bucket API
    import org.reactivecouchbase.client.OpResult
    import org.reactivecouchbase.play.PlayCouchbase

    // For query view
    import com.couchbase.client.protocol.views.{Stale, Query}
```

------
## Swagger integration
### 0. Reference urls
[Swagger](http://swagger.io/)  
[Swagger UI](https://github.com/swagger-api/swagger-ui)  
[Play plugin](https://github.com/swagger-api/swagger-play/tree/master/play-2.3/swagger-play2)  
[Integration](http://blog.knoldus.com/2015/07/17/7435/)

### 1. Install swagger-play2 plugin
```
    libraryDependencies ++= Seq(
    //other dependencies
    "com.wordnik" %% "swagger-play2" % "1.3.8",
    //other dependencies
    )
```
### 2. Install Swagger UI
*   Download Swagger UI and put all these files in __[project_root_dir]/public/swagger__

### 3. Import annotations
```
    import com.wordnik.swagger.annotations._
    import javax.ws.rs._
```
### 4. Decorate REST endpoints
```
    // Decorate controller
    @Api(value = "/bank", description = "Bank operations")
    class BankController(comp: BankServiceComponent) extends Controller

    // Decorate endpoint
    @ApiOperation(value = "Update a account", response = classOf[String], httpMethod = "PUT")
    @ApiImplicitParams(Array(
        new ApiImplicitParam(value = "Account", required = true, dataType = "Account", paramType = "body")
    ))
    def updateAccount(@ApiParam(value = "accountId", required = true) @PathParam("id") accountId: String) = Action.async {}
```
### 5. Modification in routes
```
    GET /api-docs.json controllers.ApiHelpController.getResources
```
### 6. Add application URL for Swagger GUI in application.conf
```
    swagger.api.basepath=http://localhost:9000
```
