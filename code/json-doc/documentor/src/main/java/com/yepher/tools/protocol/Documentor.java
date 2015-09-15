package com.yepher.tools.protocol;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.yepher.tools.protocol.annotations.Description;
import com.yepher.tools.protocol.annotations.RequestPDU;
import com.yepher.tools.protocol.annotations.ResponsePDU;
import com.yepher.tools.protocol.annotations.RequestPDU.Method;
import com.yepher.tools.protocol.helpers.GsonHelper;

public class Documentor {


    private DocumentorDriver driver;

    private GsonHelper gson = GsonHelper.getInstance();
    {
        gson.setPrettyPrinting(true);
    }

    /* @formatter:off */
    private Map<Class<? extends Object>, List<? extends Object>> samples = new HashMap<Class<? extends Object>, List<? extends Object>>();
    /* @formatter:on */

    private Map<Class<? extends Object>, TypeParser<?>>          specialTypes   = new HashMap<Class<? extends Object>, TypeParser<?>>();
    {
        TypeParser<?> typeParser = new BooleanParser();
        specialTypes.put(boolean.class, typeParser);
        specialTypes.put(Boolean.class, typeParser);

        typeParser = new CharacterParser();
        specialTypes.put(char.class, typeParser);
        specialTypes.put(Character.class, typeParser);

        typeParser = new ByteParser();
        specialTypes.put(byte.class, typeParser);
        specialTypes.put(Byte.class, typeParser);

        typeParser = new ShortParser();
        specialTypes.put(short.class, typeParser);
        specialTypes.put(Short.class, typeParser);

        typeParser = new IntegerParser();
        specialTypes.put(int.class, typeParser);
        specialTypes.put(Integer.class, typeParser);

        typeParser = new LongParser();
        specialTypes.put(long.class, typeParser);
        specialTypes.put(Long.class, typeParser);

        typeParser = new FloatParser();
        specialTypes.put(float.class, typeParser);
        specialTypes.put(Float.class, typeParser);

        typeParser = new DoubleParser();
        specialTypes.put(double.class, typeParser);
        specialTypes.put(Double.class, typeParser);

        typeParser = new VoidParser();
        specialTypes.put(void.class, typeParser);
        specialTypes.put(Void.class, typeParser);

        typeParser = new StringParser();
        specialTypes.put(String.class, typeParser);
    }

    public Documentor() throws Exception {
        super();

    }

    /**
     * set the documentor driver
     *
     * @param driver
     *            the driver to set
     */
    public void setDriver(DocumentorDriver driver) {
        this.driver = driver;
    }

    public void documentPDU(Class<?> pdu) {
        String pduName = pdu.getSimpleName();

        driver.printToConsole("documenting: " + pduName);

        emitLine("## " + pduName);
        emitLine();

        {
            Description descriptionAnnotation = pdu.getAnnotation(Description.class);
            if (descriptionAnnotation != null) {
                String description = descriptionAnnotation.value();
                if (description != null) {
                    emitLine(description);
                    emitLine();
                }
            }
        }

        RequestPDU requestAnnotation = pdu.getAnnotation(RequestPDU.class);
        if (requestAnnotation != null) {

            Method[] methods = requestAnnotation.method();
            if (methods != null && methods.length > 0) {
                if (methods.length == 1) {
                    emitLine("HTTP method: " + methods[0].name());
                } else {
                    emit("HTTP methods: " + methods[0].name());
                    for (int i = 1; i < methods.length; i += 1) {
                        emit(", " + methods[i].name());
                    }
                    emitLine();
                }
                emitLine();
            }

            String[] paths = requestAnnotation.path();
            if (paths != null) {
                int pathsLength = paths.length;
                if (pathsLength > 0) {
                    if (pathsLength == 1) {
                        emitLine("Path: " + paths[0]);
                    } else {
                        emit("Paths: " + paths[0]);
                        for (int i = 0; i < pathsLength; i += 1) {
                            emit(", " + paths[i]);
                        }
                        emitLine();
                    }
                    emitLine();
                }
            }

            {
                String[] pathParameters = requestAnnotation.pathParameters();
                if (pathParameters != null) {
                    int pathParametersLength = pathParameters.length;
                    if (pathParametersLength % 2 != 0) {
                        driver.printToConsole("pathParameters length is odd, will reduce by 1, for class: " + pduName);
                        pathParametersLength -= 1;
                    }
                    if (pathParametersLength > 0) {
                        emitLine("Path Parameters:");
                        emitLine();
                        emitLine("Parameter|Description");
                        emitLine(":--------|:----------");
                        for (int i = 0; i < pathParameters.length; i += 2) {
                            emitLine(pathParameters[i] + "|" + pathParameters[i + 1]);
                        }
                        emitLine();
                    }
                }
            }

            {
                String[] requestParameters = requestAnnotation.requestParameters();
                if (requestParameters != null) {
                    int requestParametersLength = requestParameters.length;
                    if (requestParametersLength % 2 != 0) {
                        driver.printToConsole("requestParameters length is odd, will reduce by 1, for class: " + pduName);
                        requestParametersLength -= 1;
                    }
                    if (requestParametersLength > 0) {
                        emitLine("Request Parameters:");
                        emitLine();
                        emitLine("Parameter|Description");
                        emitLine(":--------|:----------");
                        for (int i = 0; i < requestParameters.length; i += 2) {
                            emitLine(requestParameters[i] + "|" + requestParameters[i + 1]);
                        }
                        emitLine();
                    }
                }
            }

            {
                String[] postParts = requestAnnotation.multipartPostParts();
                if (postParts != null) {
                    int postPartsLength = postParts.length;
                    if (postPartsLength % 2 != 0) {
                        driver.printToConsole("multipartPostParts length is odd, will reduce by 1, for class: " + pduName);
                        postPartsLength -= 1;
                    }
                    if (postPartsLength > 0) {
                        emitLine("Multi-part POST parts:");
                        emitLine();
                        emitLine("Part Name|Description");
                        emitLine(":---|:----------");
                        for (int i = 0; i < postParts.length; i += 2) {
                            emitLine(postParts[i] + "|" + postParts[i + 1]);
                        }
                        emitLine();
                    }
                }
            }

            Class<?>[] responses = requestAnnotation.response();
            if (responses != null) {
                int responsesLength = responses.length;
                if (responsesLength > 0) {
                    Class<?> response = responses[0];
                    addToList(response);
                    if (responsesLength == 1) {
                        emitLine("Response: " + response.getSimpleName());
                    } else {
                        emit("Responses: " + response.getSimpleName());
                        for (int i = 1; i < responsesLength; i += 1) {
                            response = responses[i];
                            addToList(response);
                            emit(", " + response.getSimpleName());
                        }
                        emitLine();
                    }
                    emitLine();
                }
            }
        }

        ResponsePDU responseAnnotation = pdu.getAnnotation(ResponsePDU.class);
        if (responseAnnotation != null) {
            Class<?>[] requests = responseAnnotation.request();
            int requestsLength = requests.length;
            if (requests != null && requestsLength > 0) {
                Class<?> request = requests[0];
                addToList(request);
                if (requestsLength == 1) {
                    emitLine("Request: " + request.getSimpleName());
                } else {
                    emit("Requests: " + request.getSimpleName());
                    for (int i = 1; i < requestsLength; i += 1) {
                        request = requests[i];
                        addToList(request);
                        emit(", " + request.getSimpleName());
                    }
                    emitLine();
                }
                emitLine();
            }

        }

        List<Field> fields = getAllFields(pdu);

        {
            boolean headerEmitted = false;
            if (fields.size() > 0) {
                for (Field field : fields) {
                    if (fieldIsStatic(field)) {
                        continue;
                    }
                    if (!headerEmitted) {
                        emitLine("Fields:");
                        emitLine();
                        emitLine("Type|Name|Serialized Name|Sample|Description");
                        emitLine(":---|:---|:-------|:-----|:----------");
                        headerEmitted = true;
                    }
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
                    String typeName = fieldType.getSimpleName();
                    if (!isSpecialType(fieldType)) {
                        Class<?> listType = getListType(field);
                        if (listType == null) {
                            addToList(fieldType);
                        } else {
                            typeName = typeName + "<" + listType.getSimpleName() + ">";
                            addToList(listType);
                        }
                    }
                    String serializedName = fieldName;
                    String sample = "";
                    String description = "";
                    SerializedName serializedAnnotation = field.getAnnotation(SerializedName.class);
                    if (serializedAnnotation != null) {
                        serializedName = serializedAnnotation.value();
                    }
                    Description descriptionAnnotation = field.getAnnotation(Description.class);
                    if (descriptionAnnotation != null) {
                        String[] samples = descriptionAnnotation.sample();
                        if (samples != null && samples.length > 0) {
                            sample = samples[0];
                        }
                        description = descriptionAnnotation.value();
                    }
                    emitLine(typeName + "|" + fieldName + "|" + serializedName + "|" + sample + "|" + description);
                }
                emitLine();
            }
        }

        {
            boolean headerEmitted = false;
            if (fields.size() > 0) {
                for (Field field : fields) {
                    if (!fieldIsStatic(field)) {
                        continue;
                    }
                    if (!headerEmitted) {
                        emitLine("Constants:");
                        emitLine();
                        emitLine("Type|Name|Value|Description");
                        emitLine(":---|:---|:----|:----------");
                        headerEmitted = true;
                    }
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
                    String typeName = fieldType.getSimpleName();
                    if (!isSpecialType(fieldType)) {
                        Class<?> listType = getListType(field);
                        if (listType == null) {
                            addToList(fieldType);
                        } else {
                            typeName = typeName + "<" + listType.getSimpleName() + ">";
                            addToList(listType);
                        }
                    }
                    String description = "";
                    Description descriptionAnnotation = field.getAnnotation(Description.class);
                    if (descriptionAnnotation != null) {
                        description = descriptionAnnotation.value();
                    }
                    field.setAccessible(true);
                    Object value;
                    try {
                        value = field.get(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        driver.printToConsole("exception accessing value of static field " + fieldName + ": " + e.getMessage());
                        continue;
                    }
                    if (value == null) {
                        value = "[null]";
                    }
                    emitLine(typeName + "|" + fieldName + "|" + value.toString() + "|" + description);
                }
                emitLine();
            }
        }

        if ((requestAnnotation != null || responseAnnotation != null) && (requestAnnotation == null || !requestAnnotation.noSample())) {
            Object sample = getSample(pdu);
            String json = gson.toJson(sample);
            emitLine("Sample:");
            emitLine();
            emitLine("```");
            emitLine(json);
            emitLine("```");
        }

    }

    public void addToList(Class<?> clazz) {
        if (isSpecialType(clazz) || clazz.isEnum() || (clazz.getModifiers() & Modifier.ABSTRACT) != 0) {
            return;
        }
        driver.addPduToDocument(clazz);
    }

    private <T> T getSample(Class<T> clazz) {
        return getSample(clazz, 0);
    }

    private <T extends Object> T getSample(Class<T> clazz, int index) {
        T sample = null;
        @SuppressWarnings("unchecked")
        List<T> sampleList = (List<T>) samples.get(clazz);
        if (sampleList == null) {
            sampleList = new ArrayList<T>();
            samples.put(clazz, sampleList);
        }

        while (index >= sampleList.size()) {
            sampleList.add(makeSample(clazz, index));
        }
        sample = sampleList.get(index);

        return sample;
    }

    private <T> T makeSample(Class<T> clazz, int index) {
        T sample = null;
        try {
            sample = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            driver.printToConsole("exception creating sample: " + e.getMessage());
            return sample;
        }

        List<Field> fields = getAllFields(clazz);
        for (Field field : fields) {
            if (fieldIsStatic(field)) {
                continue;
            }
            Object value = null;
            Description descriptionAnnotation = field.getAnnotation(Description.class);
            Class<?> listType = getListType(field);
            if (listType != null) {
                Integer numberOfItems = 0;
                if (descriptionAnnotation != null) {
                    numberOfItems = descriptionAnnotation.numberOfSamplesInList();
                }
                ArrayList<Object> arrayList = new ArrayList<Object>(numberOfItems);
                for (int i = 0; i < numberOfItems; i += 1) {
                    Object listItem = getFieldSample(listType, descriptionAnnotation, i);
                    arrayList.add(listItem);
                }
                value = arrayList;
            } else {
                Class<?> fieldType = field.getType();
                value = getFieldSample(fieldType, descriptionAnnotation, index);
            }
            try {
                field.setAccessible(true);
                field.set(sample, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                driver.printToConsole("exception assigning value to sample: " + e.getMessage());
            }
        }

        return sample;
    }

    private boolean fieldIsStatic(Field field) {
        int modifiers = field.getModifiers();
        boolean isStatic = (modifiers & Modifier.STATIC) != 0;
        return isStatic;
    }

    private Object getFieldSample(Class<?> fieldType, Description descriptionAnnotation, int index) {
        Object value = null;
        if (isSpecialType(fieldType)) {
            if (descriptionAnnotation != null) {
                String sampleString = getSampleString(descriptionAnnotation, index);
                if (sampleString != null) {
                    TypeParser<?> typeParser = specialTypes.get(fieldType);
                    try {
                        value = typeParser.parse(sampleString);
                    } catch (NumberFormatException e) {
                        System.err.println("NumberFormatException parsing sample: " + sampleString + " for " + fieldType.getCanonicalName() + " : " + descriptionAnnotation);
                        throw e;
                    }
                }
            }
        } else if (fieldType.isEnum()) {
            String enumName = getSampleString(descriptionAnnotation, index);
            if (enumName != null) {
                Enum<?>[] enumConstants = (Enum[]) fieldType.getEnumConstants();
                for (int i = 0; i < enumConstants.length; i++) {
                    Enum<?> enumConstant = enumConstants[i];
                    if (enumConstant.name().equals(enumName)) {
                        value = enumConstant;
                        break;
                    }
                }
            }
        } else {
            Class<?> sampleClass = fieldType;
            if (descriptionAnnotation != null) {
                Class<?>[] sampleClasses = descriptionAnnotation.sampleClasses();
                int sampleClassesLength = sampleClasses.length;
                if (sampleClasses != null && sampleClassesLength > 0) {
                    int sampleIndex = index;
                    if (sampleIndex >= sampleClassesLength) {
                        sampleIndex = sampleClassesLength - 1;
                    }
                    sampleClass = sampleClasses[sampleIndex];
                    addToList(sampleClass);
                }
            }
            value = getSample(sampleClass, index);
        }
        return value;
    }

    private String getSampleString(Description descriptionAnnotation, int sampleIndex) {
        String sampleString = null;
        String[] samples = descriptionAnnotation.sample();
        if (samples != null) {
            if (sampleIndex >= samples.length) {
                sampleIndex = samples.length - 1;
            }
            if (sampleIndex >= 0) {
                sampleString = samples[sampleIndex];
            }
        }
        return sampleString;
    }

    private boolean isSpecialType(Class<?> clazz) {
        if (specialTypes.containsKey(clazz)) {
            return true;
        }
        return false;
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        fields.addAll(Arrays.asList(declaredFields));
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null && !Object.class.equals(superclass)) {
            declaredFields = superclass.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            superclass = superclass.getSuperclass();
        }

        for (int i = 0; i < fields.size(); i += 1) {
            Field field = fields.get(i);
            // String fieldName = field.getName();
            if (field.getAnnotation(Deprecated.class) != null) {
                // driver.printToConsole(fieldName + " is deprecated");
                fields.remove(i);
                i -= 1;
                continue;
            }
            int modifiers = field.getModifiers();
            if ((modifiers & Modifier.TRANSIENT) != 0) {
                // driver.printToConsole(fieldName + " is transient");
                fields.remove(i);
                i -= 1;
                continue;
            }
        }

        return fields;
    }

    /**
     * get the item type, T, of a field whose class implements List<T>, or an extension thereof.
     *
     * @param field
     *            the field whose class (potentially) implements List<T> or an extension thereof.
     * @return T, or null if the class of the field does not implement List<T> or an extension thereof
     */
    private Class<?> getListType(Field field) {
        Type typeOfField = field.getGenericType();
        Class<?> listType = getListType(typeOfField);
        return listType;
    }

    private Class<?> getListType(Type type) {
        Class<?> listType = null;

        /*
         * if this isn't a parameterized type, it can't be List<T>
         */
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type rawType = parameterizedType.getRawType();
        if (rawType == null) {
            return null;
        }
        /*
         * is this List<T>?
         */
        if (rawType.equals(List.class)) {
            /*
             * yes, get and return T
             */
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments == null || actualTypeArguments.length != 1) {
                return null;
            }
            Type actualTypeArgument = actualTypeArguments[0];
            if (actualTypeArgument == null || !(actualTypeArgument instanceof Class<?>)) {
                return null;
            }
            listType = (Class<?>) actualTypeArgument;
            return listType;
        }

        /*
         * no, maybe a superclass is or implements List<T>; go looking for it first the type has to be a class
         */
        if (!(type instanceof Class)) {
            return null;
        }
        Class<?> clazz = (Class<?>) type;
        Type genericSuperclazz = clazz.getGenericSuperclass();
        while (genericSuperclazz != null) {
            listType = getListType(genericSuperclazz);
            /*
             * did we find T?
             */
            if (listType != null) {
                /*
                 * Yes, return it
                 */
                return listType;
            }
            /*
             * try another superclass
             */
            if (!(genericSuperclazz instanceof Class)) {
                break;
            }
            clazz = (Class<?>) genericSuperclazz;
            genericSuperclazz = clazz.getGenericSuperclass();
        }

        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (int i = 0; i < genericInterfaces.length; i++) {
            Type interfaze = genericInterfaces[i];
            listType = getListType(interfaze);
            /*
             * did we find T?
             */
            if (listType != null) {
                /*
                 * Yes, return it
                 */
                return listType;
            }
        }

        /*
         * all out of possibilities for this type, return null
         */
        return null;
    }

    private void emit(String text) {
        driver.emit(text);
    }

    private void emitLine() {
        driver.emitLine();
    }

    private void emitLine(String text) {
        driver.emitLine(text);
    }

    private static interface TypeParser<T> {

        public T parse(String string);
    }

    private static class BooleanParser implements TypeParser<Boolean> {

        @Override
        public Boolean parse(String string) {
            return Boolean.valueOf(string);
        }
    }

    private static class CharacterParser implements TypeParser<Character> {

        @Override
        public Character parse(String string) {
            if (string == null || string.length() == 0) {
                return null;
            }
            return string.charAt(0);
        }
    }

    private static class ByteParser implements TypeParser<Byte> {

        @Override
        public Byte parse(String string) {
            return Byte.valueOf(string);
        }
    }

    private static class ShortParser implements TypeParser<Short> {

        @Override
        public Short parse(String string) {
            return Short.valueOf(string);
        }
    }

    private static class IntegerParser implements TypeParser<Integer> {

        @Override
        public Integer parse(String string) {
            return Integer.valueOf(string);
        }
    }

    private static class LongParser implements TypeParser<Long> {

        @Override
        public Long parse(String string) {
            return Long.valueOf(string);
        }
    }

    private static class FloatParser implements TypeParser<Float> {

        @Override
        public Float parse(String string) {
            return Float.valueOf(string);
        }
    }

    private static class DoubleParser implements TypeParser<Double> {

        @Override
        public Double parse(String string) {
            return Double.valueOf(string);
        }
    }

    private static class VoidParser implements TypeParser<Void> {

        @Override
        public Void parse(String string) {
            return null;
        }
    }

    private static class StringParser implements TypeParser<String> {

        @Override
        public String parse(String string) {
            return string;
        }
    }

}
