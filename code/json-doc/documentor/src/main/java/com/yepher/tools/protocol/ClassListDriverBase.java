package com.yepher.tools.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

public abstract class ClassListDriverBase implements DocumentorDriver {

    private PrintStream console;

    /***
     *** methods required in subclasses
     ***/

    @Override
    public abstract void addPduToDocument(Class<?> clazz);

    protected abstract Set<Class<?>> getPdusToExclude();

    protected abstract List<Class<?>> getPdusToDocument();

    protected abstract String getTitle();

    protected abstract void setOutputPath(String outputPath);

    protected abstract String getOutputPath();

    /***
     *** end of methods required in subclasses
     ***/

    /***
     *** methods from the DocumentorDriver interface
     ***/

    // public abstract void addPduToDocument(Class<?> clazz); // see above

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

    private BufferedReader in;
    private PrintStream    out;

    protected ClassListDriverBase() {
        super();
    }

    protected void run(String[] args) throws Exception {

        console = System.out;
        InputStreamReader inReader = new InputStreamReader(System.in, "utf-8");
        in = new BufferedReader(inReader);

        if (args != null) {
            if (args.length > 0) {
                setOutputPath(args[0]);
            }
        }

        console.println("generating \"" + getTitle() + "\" markdown documentation to: " + getOutputPath());

        File outputFile = new File(getOutputPath());

        if (outputFile.exists()) {
            String response = "";

            while (response != null && !response.equalsIgnoreCase("y")) {
                console.println(getOutputPath() + " exists. overwrite (y/n)? ");
                response = in.readLine();
                if (response != null && response.equalsIgnoreCase("n")) {
                    console.println("protocol documentation will NOT be generated!!!");
                    return;
                }
            }
            console.println(getOutputPath() + " will be overwritten!");
        } else {
            outputFile.createNewFile();
        }

        FileOutputStream outStream = new FileOutputStream(outputFile);
        out = new PrintStream(outStream);

        String text = "# " + getTitle();
        emitLine(text);
        emitLine();

        Documentor documentor = new Documentor();
        documentor.setDriver(this);

        List<Class<?>> pdusToDocument = getPdusToDocument();
        for (int i = 0; i < pdusToDocument.size(); i += 1) {
            Class<?> pdu = pdusToDocument.get(i);
            if (!getPdusToExclude().contains(pdu)) {
                documentor.documentPDU(pdu);
            }
        }

        if (out != null) {
            out.close();
        }
    }

    private String sanitize(String string) {
        String result = string.replaceAll("\\&", "&amp;");
        result = result.replaceAll("\\<", "&lt;");
        return result;
    }

}