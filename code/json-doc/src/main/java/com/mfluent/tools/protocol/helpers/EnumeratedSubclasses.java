
package com.mfluent.tools.protocol.helpers;

/**
 * an interface that must be implemented by base classes to be used with EnumeratedSubclassAdapterFactory (which is used with GSON)
 * 
 * @author jrenkel
 * @param <E>
 *            an enum that identified the subclasses of the base class
 */
public interface EnumeratedSubclasses<E> {

    /*
     * a static method with the following signature is required; it will be accessed (infrequently) via reflection
     * </br>
     * unfortunately, the existence of this method in the base class can't be checked at compile time, but EnumeratedSubclassAdapterFactory will not work with
     * this base class if it is not present
     */
    //    /**
    //     * get the enum values that identify the subclasses of the base class
    //     * 
    //     * @return the enum values that identify the subclasses of the base class
    //     */
    //    public static E[] getEnumerationValues();
    /**
     * the name of the static method that gets the enum values that identify the subclasses of the base class
     */
    public static final String staticMethodName = "getEnumerationValues";

    /**
     * get the enum value that identifies this subclass instance
     * 
     * @return the enum value that identifies this subclass instance
     */
    public E getSubclass();
}
