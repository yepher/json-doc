package com.yepher.jsondoc.samples.one;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yepher.jsondoc.ClassListDriverBase;

public class ClassListDriver extends ClassListDriverBase {

    ClassListDriver() {
        super();
    }


    private static final String TITLE = "A Sample REST Protocol";

    /* @formatter:off */
    private List<Class<?>>      pdusToDocument = new ArrayList<Class<?>>(Arrays.asList(
                CreateUserRequest.class,
                CreateUserResponse.class
            ));

    private Set<Class<?>>       pdusToExclude = new HashSet<Class<?>>(Arrays.asList(
                Date.class
            ));
    /* @formatter:on */

    private String              outputPath     = "doc/sample1/protocol.md";

    @Override
    protected String getOutputPath() {
        return outputPath;
    }

    @Override
    protected void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    protected String getTitle() {
        return TITLE;
    }

    @Override
    protected List<Class<?>> getPdusToDocument() {
        return pdusToDocument;
    }

    @Override
    protected Set<Class<?>> getPdusToExclude() {
        return pdusToExclude;
    }

    @Override
    public void addPduToDocument(Class<?> clazz) {
        List<Class<?>> pdusToDocument2 = getPdusToDocument();
        if (!pdusToDocument2.contains(clazz) && !getPdusToExclude().contains(clazz)) {
            pdusToDocument2.add(clazz);
        }
    }

    public static void main(String[] args) throws Exception {
        new ClassListDriver().run(args);
    }

}
