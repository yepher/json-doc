<img src="icon.png" width="75"/>

# json-doc

Json-doc make documenting and maintaining your REST based JSON API easier.

## Project Discussions

In additions to the GitHub issues project discussions JsonDoc are on Slack. The group at [ChiDev](http://chidev.org/) was nice enough to allow us to use a channel in their Slack group. Joining the conversation:

* Browse to http://chidev.org/
* Select [Slack Botton](https://chidev.herokuapp.com/) on the page
* Join the #github_gsondoc channel and contribute to the conversation

## Example Output

Json-doc will scan through a list of classes and create documentation based on included annotations. This way the code can be used as the source for your JSON REST API documentation. This is part of an actual output from the [sample](https://github.com/yepher/GsonDoc/tree/develop/code/json-doc/samples) application.

++++++++++++++++

### A Sample REST Protocol

#### CreateUserRequest

HTTP method: POST

Path: /createUser

Fields:

Type|Name|Serialized Name|Sample|Description
:---|:---|:-------|:-----|:----------
User|user|user||The name, etc. of the user to create
String|password|password|********|The user's requested password


Sample:

```json
{
  "user": {
    "userName": "chris",
    "firstName": "Chris",
    "lastName": "Wilson",
    "userSince": "Aug 7, 2015 1:36:35 PM"
  },
  "password": "********"
}
```

#### User

Fields:

Type|Name|Serialized Name|Sample|Description
:---|:---|:-------|:-----|:----------
String|userName|userName|chris|The user's userName
String|firstName|firstName|Chris|The user's first name
String|lastName|lastName|Wilson|The user's last name
Date|userSince|userSince|August 1, 2015|The user's enrollment date


++++++++++++++++


## Build Instructions

### Prerequisites

You need the following installed and available in your $PATH:

* Java 7 (http://java.oracle.com)
* Apache maven 3.3 or greater (http://maven.apache.org/)

### Building

This project requires Java and Maven.

`````bash
cd code/json-doc/
mvn clean install

`````



## Usage

For usage example see the [sample](https://github.com/yepher/GsonDoc/tree/develop/code/json-doc/samples) project in this repository.

The basic idea is:

1. Include this in your projects pom.xml:

```xml
<dependency>
	<groupId>com.yepher.jsondoc</groupId>
	<artifactId>annotations</artifactId>
	<version>0.0.2</version>
</dependency>
<dependency>
	<groupId>com.yepher.jsondoc</groupId>
	<artifactId>documentor</artifactId>
	<version>0.0.2</version>
</dependency>
```

2. Annotate your classes with these annotations `RequestPDU`, `ResponsePDU` and `Description`

**Example:**

```java
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@ResponsePDU(request={CreateUserRequest.class})
public class CreateUserResponse extends ResponseBase {

    @Description(value="The user's authorizationtoken (in Base64)",sample={"0123456789abcdefghi","zyxwvutsr9876543210"})
    public String authorizationToken;

}
```

3. Create a class that extends `ClassListDriverBase`. This class tells Documentor which classes should be documented:

**Example:**

```java
public class ClassListDriver extends ClassListDriverBase {

    ClassListDriver() {
        super();
    }


    private static final String TITLE = "A Sample REST Protocol";

    /* @formatter:off */
    private List<Class<?>>      pdusToDocument = new ArrayList<Class<?>>(Arrays.asList(
                CreateUserRequest.class,
                CreateUserResponse.class
            ));

    private Set<Class<?>>       pdusToExclude = new HashSet<Class<?>>(Arrays.asList(
                Date.class
            ));
    /* @formatter:on */

    private String              outputPath     = "doc/sample1/protocol.md";

    @Override
    protected String getOutputPath() {
        return outputPath;
    }

    @Override
    protected void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    protected String getTitle() {
        return TITLE;
    }

    @Override
    protected List<Class<?>> getPdusToDocument() {
        return pdusToDocument;
    }

    @Override
    protected Set<Class<?>> getPdusToExclude() {
        return pdusToExclude;
    }

    @Override
    public void addPduToDocument(Class<?> clazz) {
        List<Class<?>> pdusToDocument2 = getPdusToDocument();
        if (!pdusToDocument2.contains(clazz) && !getPdusToExclude().contains(clazz)) {
            pdusToDocument2.add(clazz);
        }
    }

    public static void main(String[] args) throws Exception {
        new ClassListDriver().run(args);
    }

}

```


## Goals

* Separate emitter code so users can generate other types of output besides _Markdown_. Expect outputs are:
    * PDF
    * HTML doc
    * Objective-C (Mac or iOS)
* Allow message sequence markup to generate sequence diagrams as part of documentation: Something like these but generated by this tool directly:
	*  https://bramp.github.io/js-sequence-diagrams/
	*  https://www.websequencediagrams.com/
	*  http://www.itu.int/rec/T-REC-Z.120-201102-I/en

## Release Stuff:

* mvn clean install source:jar javadoc:jar package gpg:sign deploy -Dgpg.passphrase=PASSPHRASE
* https://oss.sonatype.org/#stagingRepositories
	* close
	* If verification is successful
	* release


## Related Projects

* [Swagger](https://github.com/swagger-api/swagger-core)
