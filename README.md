# JsonDoc

This tools make documenting your GSON api easier.

## Build Instructions

This project requires Java and Maven.

`````
cd code/json-doc/
mvn clean install

`````



## TODO

* Must fix Document.jave ln 44. We can't hard-cdoe list of classes. They will need to be passed in.
* How should we handle BaseResponse? We should not require projects to use our version of a BaseResponse

## Goals

* Seperate emmiter code user could generate other types of output besides just MD. Like:
    * PDF
    * HTML doc
    * Objective-C (Mac or iOS)


