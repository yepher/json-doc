
package com.yepher.tools.protocol.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation is used to identify a response PDU.
 *
 * @author jrenkel
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ResponsePDU {

    Class<?>[] request();
}
