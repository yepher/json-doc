package com.yepher.tools.protocol.samples.one;

import java.util.Date;

import com.yepher.tools.protocol.annotations.Description;

import lombok.Data;

@Data
public class User {

    @Description(value="The user's userName",sample={"chris","jim"})
    public String userName;

    public transient int userId;

    @Description(value="The user's first name",sample={"Chris","Jim"})
    public String firstName;

    @Description(value="The user's last name",sample={"Wilson","Renkel"})
    public String lastName;

    @Description(value = "The user's enrollment date",sample={"August 1, 2015","August 2, 2015"})
    public Date userSince;

}
