
package com.mfluent.tools.protocol.helpers;

/**
 * interface for enum's that identify subclasses for use with EnumeratedSubclassAdapterFactory (which is used with GSON)
 * 
 * @author jrenkel
 * @param <T>
 *            the base type of the subclasses
 */
public interface SubclassEnumeration<T> {

    /**
     * get the class identified by the enumeration value
     * 
     * @return the class identified by the enumeration value
     */
    public Class<? extends T> getSubclass();

    /**
     * get the tag that will identify this sub-class in JSON
     * 
     * @return the tag that will identify this sub-class in JSON
     */
    public String getTag();
}
