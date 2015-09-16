package com.yepher.jsondoc.samples.one;

import com.yepher.jsondoc.annotations.Description;
import com.yepher.jsondoc.annotations.RequestPDU;
import com.yepher.jsondoc.annotations.RequestPDU.Method;

import lombok.Data;

@Data
@RequestPDU(path = { "/createUser" },method={Method.POST})
public class CreateUserRequest {

    @Description("The name, etc. of the user to create")
    public User user;

    @Description(value="The user's requested password",sample={"********"})
    public String password;
}
