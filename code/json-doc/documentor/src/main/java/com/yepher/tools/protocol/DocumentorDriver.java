/**
 *
 */
package com.yepher.tools.protocol;

/**
 * interface for callbacks from Documentor to its driver
 *
 * @author jrenkel
 *
 */
public interface DocumentorDriver {

    /**
     * add a class to the list of classes that are referenced and should be documented
     *
     * @param clazz
     *            the class to add
     */
    public void addPduToDocument(Class<?> clazz);

    public void emit(String text);

    public void emitLine();

    public void emitLine(String text);

    public void printToConsole(String line);

}
