# A Sample REST Protocol

## CreateUserRequest

HTTP method: POST

Path: /createUser

Fields:

Type|Name|Serialized Name|Sample|Description
:---|:---|:-------|:-----|:----------
User|user|user||The name, etc. of the user to create
String|password|password|********|The user's requested password


Sample:

```
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
## CreateUserResponse

Request: CreateUserRequest

Fields:

Type|Name|Serialized Name|Sample|Description
:---|:---|:-------|:-----|:----------
String|authorizationToken|authorizationToken|0123456789abcdefghi|The user's authorizationtoken (in Base64)
Long|timeStamp|ts|1391114519675|The time the server received the request that generated this response (milliseconds since The Epoch)
Long|responseTime|rt|137|The response time of the server to the request that generated this response (milliseconds)
int|statusCode|status|0|The status of the request. See BaseResponse.Status for values.


Sample:

```
{
  "authorizationToken": "0123456789abcdefghi",
  "ts": 1391114519675,
  "rt": 137,
  "status": 0
}
```
## User

Fields:

Type|Name|Serialized Name|Sample|Description
:---|:---|:-------|:-----|:----------
String|userName|userName|chris|The user's userName
String|firstName|firstName|Chris|The user's first name
String|lastName|lastName|Wilson|The user's last name
Date|userSince|userSince|August 1, 2015|The user's enrollment date


