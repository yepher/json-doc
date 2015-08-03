package com.mfluent.tools.protocol.samples.one;

import com.mfluent.tools.protocol.annotations.Description;
import com.mfluent.tools.protocol.annotations.RequestPDU;
import com.mfluent.tools.protocol.annotations.RequestPDU.Method;

import lombok.Data;

@Data
@RequestPDU(path = { "/createUser" },method={Method.POST})
public class CreateUserRequest {

    @Description("The name, etc. of the user to create")
    public User user;

    @Description(value="The user's requested password",sample={"********"})
    public String password;
}
