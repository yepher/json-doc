package com.yepher.jsondoc.objc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

public abstract class ObjcClassListDriverBase implements ObjcEmitterDriver {

	private PrintStream console;
	
	private PrintStream out;

	protected ObjcClassListDriverBase() {
		super();
	}


	/***
	 *** methods required in subclasses
	 ***/

	protected abstract Set<Class<?>> getPdusToExclude();

	protected abstract List<Class<?>> getPdusToDocument();

	protected abstract String getTitle();

	public abstract String getOutputPath();

	/***
	 *** end of methods required in subclasses
	 ***/

	/***
	 *** methods from the DocumentorDriver interface
	 ***/

	@Override
	public void emit(String text) {
		out.print(sanitize(text));
	}

	@Override
	public void emitLine() {
		out.println();
	}

	@Override
	public void emitLine(String text) {
		out.println(sanitize(text));
	}

	@Override
	public void printToConsole(String line) {
		console.println(line);
	}

	/***
	 *** end of methods from the DocumentorDriver interface
	 ***/

	
	protected void run(String[] args) throws Exception {
		console = System.out;
		
		// Make sure output path exists
		new File(getOutputPath()).mkdirs();
		
		ObjcEmitter documentor = new ObjcEmitter();
		documentor.setDriver(this);

		List<Class<?>> pdusToDocument = getPdusToDocument();
		for (int i = 0; i < pdusToDocument.size(); i += 1) {
			Class<?> clazz = pdusToDocument.get(i);
			if (getPdusToExclude() == null || !getPdusToExclude().contains(clazz)) {
				
				String outputFile = getOutputPath() + "/" + getClassPrefix() + clazz.getSimpleName();
				FileOutputStream outStream = new FileOutputStream(outputFile + ".h");
		        out = new PrintStream(outStream);
				documentor.writeHeaderFile(clazz);

				
				outStream = new FileOutputStream(outputFile + ".m");
		        out = new PrintStream(outStream);
				documentor.writeClassFile(clazz);
			}
		}
		
		if (out != null) {
			out.close();
		}
	}

	private String sanitize(String string) {
		String result = string;
		//result = result.replaceAll("\\<", "&lt;");
		return result;
	}

}