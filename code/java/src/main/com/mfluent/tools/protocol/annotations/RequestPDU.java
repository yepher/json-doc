
package com.mfluent.tools.protocol.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mfluent.tools.protocol.BaseResponse;

/**
 * this annotation is used to identify a request PDU.
 * 
 * @author jrenkel
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestPDU {

    public enum Method {
        GET,
        POST,
        PUT
    }

    Method[] method() default {Method.POST};

    String[] path();

    String[] pathParameters() default {};  // pairs of: path parameter name; description

    String[] requestParameters() default {}; // pairs of: request parameter; description

    String[] multipartPostParts() default {};   // pairs of: part name, description of part

    Class<? extends BaseResponse>[] response() default {};

    boolean noSample() default false; // set to true if no sample should be documented

}
