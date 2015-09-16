package com.yepher.jsondoc.samples.one;

import com.yepher.jsondoc.annotations.Description;
import com.yepher.jsondoc.annotations.ResponsePDU;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@ResponsePDU(request={CreateUserRequest.class})
public class CreateUserResponse extends ResponseBase {

    @Description(value="The user's authorizationtoken (in Base64)",sample={"0123456789abcdefghi","zyxwvutsr9876543210"})
    public String authorizationToken;

}
