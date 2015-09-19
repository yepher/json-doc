/**
 *
 */
package com.yepher.jsondoc.objc;

/**
 * interface for callbacks from Documentor to its driver
 *
 */
public interface ObjcEmitterDriver {

    /**
     * add a class to the list of classes that are referenced and should be documented
     *
     * @param clazz
     *            the class to add
     */
	public String getOutputPath();
	
	public String getClassPrefix();
	
	public void addPduToDocument(Class<?> clazz);
	
    public void emit(String text);

    public void emitLine();

    public void emitLine(String text);

    public void printToConsole(String line);
    
    public String getBanner();
    
    public boolean isInPackage(String packageName);
    
}
