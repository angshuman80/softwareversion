# softwareversion
MiraDry Software version

## Before you deploy your application to Azure Function, let's first test it locally

First you need to package your application into a Jar file:
* Install maven - Downlaod binary and install - https://maven.apache.org/install.html
* From the current directory run following command

```
mvn package -Dmaven.test.skip=true
```
NOTE - After running the command you will able to see - softwareversion-0.0.1-SNAPSHOT.jar has been created in /target folder

* Now that the application is packaged, you can run it using the azure-functions Maven plugin:
```
mvn azure-functions:run
```

## Deploy the Function to Azure Functions
Now you're going to publish the Azure Function to production. Remember that the <functionAppName>, <functionAppRegion>, and <functionResourceGroup> properties you've defined in your pom.xml file will be used to configure your function.

* Note *

The Maven plugin needs to authenticate with Azure. If you have Azure CLI installed, use az login before continuing. For more authentication options, see Authentication in the azure-maven-plugins repository.

Run Maven to deploy your function automatically:
```
mvn azure-functions:deploy
```

