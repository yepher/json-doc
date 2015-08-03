
package com.mfluent.tools.protocol.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation is used to describe a PDU field.
 * 
 * @author jrenkel
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Description {

    String value(); // this is the description of the annotated field

    String[] sample() default {};

    int numberOfSamplesInList() default 0;

    //    boolean valueIsJSON() default false;

    Class<?>[] sampleClasses() default {};

}
