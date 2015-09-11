/**
 * 
 */

package com.mfluent.tools.protocol.helpers;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * a factory for GSON TypeAdapters for base classes with the EnumeratedSubclasses interface.
 * 
 * @author jrenkel
 */
public class EnumeratedSubclassAdapterFactory implements TypeAdapterFactory {

    /**
     * the factory will be called once for each base class AND once for each subclass of the base class.
     * </br>
     * but the same type adapter will work for each base class and all of its subclasses.
     * </br>
     * this map holds the type adapters for each base class. the same adapter will be used for the base class and all of its subclasses.
     * </br></br>
     * it may not be necessary for this to be a ConcurrentHashMap, but safety first.
     */
    private Map<Class<?>, EnumeratedSubclassAdapter<?>> adapters = new ConcurrentHashMap<Class<?>, EnumeratedSubclassAdapter<?>>();

    /*
     * (non-Javadoc)
     * @see com.google.gson.TypeAdapterFactory#create(com.google.gson.Gson, com.google.gson.reflect.TypeToken)
     */
    @SuppressWarnings("unchecked")
    @Override
    /*
     * this method is only invoked once for each type, so synchronization does not (significantly) affect performance.
     * it may not be necessary, but safety first.
     */
    public synchronized <T>TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        /*
         * get the class for which GSON is looking for an adapter
         */
        Class<? super T> clazz = type.getRawType();
        /*
         * is this a primitive type?
         */
        if (clazz.isPrimitive()) {
            /*
             * yes, then it can't be for us
             */
            return null;
        }
        /*
         * does this class or one of its superclasses have our marker interface?
         */
        boolean hasOurInterface = false;
        while (clazz != null) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                Class<?> iface = interfaces[i];
                if (iface.equals(EnumeratedSubclasses.class)) {
                    hasOurInterface = true;
                    break;
                }
            }
            if (hasOurInterface) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        if (!hasOurInterface) {
            /*
             * no, return saying we can't handle it
             */
            return null;
        }
        /*
         * does this (super-)class also implement our static method?
         */
        Method staticMethod = null;
        try {
            staticMethod = clazz.getDeclaredMethod(EnumeratedSubclasses.staticMethodName, (Class<?>[]) null);
        } catch (Exception e) {
            return null;
        }

        /*
         * get the enumeration values that identify the subclasses
         */
        Enum<? extends SubclassEnumeration<?>>[] enumerationValues = null;
        try {
            enumerationValues = (Enum<? extends SubclassEnumeration<?>>[]) staticMethod.invoke(null, (Object[]) null);
        } catch (Exception e) {
            return null;
        }
        if (enumerationValues.length == 0) {
            return null;
        }
        if (!(enumerationValues[0] instanceof SubclassEnumeration<?>)) {
            return null;
        }
        /*
         * OK, do we already have an adapter for this (base) class?
         */
        EnumeratedSubclassAdapter<T> adapter = (EnumeratedSubclassAdapter<T>) this.adapters.get(clazz);
        if (adapter == null) {
            /*
             * no, create and return the adapter
             */
            TypeAdapter<T>[] delegates = (TypeAdapter<T>[]) new TypeAdapter<?>[enumerationValues.length];
            for (int i = 0; i < enumerationValues.length; i++) {
                Enum<? extends SubclassEnumeration<?>> enumerationValue = enumerationValues[i];
                delegates[i] = (TypeAdapter<T>) gson.getDelegateAdapter(this, TypeToken.get(((SubclassEnumeration<?>) enumerationValue).getSubclass()));
            }
            adapter = new EnumeratedSubclassAdapter<T>(enumerationValues, delegates);
            this.adapters.put(clazz, adapter);
        }
        return adapter;
    }

    /**
     * a GSON type adapter for a base class with the EnumeratedSubclasses interface, and all of its subclasses.
     * </br></br>
     * instances of this class are (supposed to be) immutable and threadsafe.
     * 
     * @author jrenkel
     * @param <T>
     *            the base class
     */
    private static class EnumeratedSubclassAdapter<T> extends TypeAdapter<T> {

        //        private enum TagType {
        //            ORDINAL,
        //            NAME,
        //            TAG;
        //        };
        //
        //        /**
        //         * if ORDINAL, subclasses will be identified by the ordinal of the enumeration value that identifies them; produces smallest and least readable JSON.
        //         * </br>
        //         * if NAME, subclasses will be identified by the name of the enumeration value that identifies them; produces largest but most readable JSON;
        //         * </br>
        //         * if TAG, subclasses will be identified by the "tag" of the enumeration value that identifies them; produces slightly smaller but more readable JSON.
        //         */
        //        private static final TagType tagType = TagType.TAG;

        /**
         * the enumeration values that identify subclasses of the base class
         */
        private final Enum<? extends SubclassEnumeration<?>>[] enumerationValues;

        /**
         * the GSON type adapters that would otherwise process each subclass of the base class.
         * </br>
         * these are in 1-1, in-order correspondence with the enumeration values, above.
         */
        private final TypeAdapter<T>[] delegates;

        /**
         * construct a EnumeratedSubclassAdapter given its subclass identifying enumeration values and the delegates for them.
         * 
         * @param enumerationValues
         *            the enumeration values that identify the subclasses
         * @param delegates
         *            the GSON delegate type adapters for the subclasses
         */
        private EnumeratedSubclassAdapter(Enum<? extends SubclassEnumeration<?>>[] enumerationValues, TypeAdapter<T>[] delegates) {
            super();
            this.enumerationValues = enumerationValues;
            this.delegates = delegates;
        }

        /*
         * (non-Javadoc)
         * @see com.google.gson.TypeAdapter#write(com.google.gson.stream.JsonWriter, java.lang.Object)
         */
        @Override
        public void write(JsonWriter writer, T value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.beginObject();
            @SuppressWarnings("unchecked")
            Enum<? extends SubclassEnumeration<?>> valueType = (Enum<? extends SubclassEnumeration<?>>) ((EnumeratedSubclasses<?>) value).getSubclass();
            int index = -1;
            String string = null;
            //            switch (tagType) {
            //                case ORDINAL:
            //                    index = valueType.ordinal();
            //                    string = Integer.toString(index);
            //                    break;
            //                case NAME:
            //                    string = valueType.name();
            //                    index = getIndexFromName(string);
            //                    break;
            //                case TAG:
            string = ((SubclassEnumeration<?>) valueType).getTag();
            index = getIndexFromTag(string);
            //                    break;
            //            }
            if (index < 0 || index >= this.enumerationValues.length) {
                throw new RuntimeException("invalid sub-class [" + string + "]");
            }

            writer.name(string);
            TypeAdapter<T> delegate = this.delegates[index];
            delegate.write(writer, value);
            writer.endObject();
        }

        /*
         * (non-Javadoc)
         * @see com.google.gson.TypeAdapter#read(com.google.gson.stream.JsonReader)
         */
        @Override
        public T read(JsonReader reader) throws IOException {
            TypeAdapter<T> delegate = null;
            T result = null;

            JsonToken nextTokenType = checkNextToken(reader, JsonToken.NULL, JsonToken.BEGIN_OBJECT);
            if (nextTokenType == JsonToken.NULL) {
                return null;
            }

            //            nextTokenType = checkNextToken(reader, JsonToken.NAME);
            checkNextToken(reader, JsonToken.NAME);
            String string = reader.nextName();

            int index = -1;
            //            switch (tagType) {
            //                case ORDINAL:
            //                    index = Integer.parseInt(string);
            //                    break;
            //                case NAME:
            //                    index = getIndexFromName(string);
            //                    break;
            //                case TAG:
            index = getIndexFromTag(string);
            //                    break;
            //            }
            if (index < 0 || index >= this.enumerationValues.length) {
                throw new RuntimeException("invalid sub-class [" + string + "]");
            }

            delegate = this.delegates[index];
            result = delegate.read(reader);
            //            nextTokenType = checkNextToken(reader, JsonToken.END_OBJECT);
            checkNextToken(reader, JsonToken.END_OBJECT);
            return result;
        }

        //        /**
        //         * get the ordinal of the enumeration value that identifies a subclass given the name of the enumeration value
        //         * 
        //         * @param name
        //         *            the name of the enumeration value whose ordinal is to be determined
        //         * @return the ordinal of the enumeration value whose name was given; an invalid ordinal will be returned for an invalid name.
        //         */
        //        private int getIndexFromName(String name) {
        //            int index;
        //            for (index = 0; index < this.enumerationValues.length; index++) {
        //                Enum<? extends SubclassEnumeration<?>> enumerationValue = this.enumerationValues[index];
        //                if (enumerationValue.name().contentEquals(name)) {
        //                    break;
        //                }
        //            }
        //            return index;
        //        }

        /**
         * get the ordinal of the enumeration value that identifies a subclass given the "tag" of the enumeration value
         * 
         * @param tag
         *            the "tag" of the enumeration value whose ordinal is to be determined
         * @return the ordinal of the enumeration value whose "tag" was given; an invalid ordinal will be returned for an invalid name.
         */
        private int getIndexFromTag(String tag) {
            int index;
            for (index = 0; index < this.enumerationValues.length; index++) {
                SubclassEnumeration<?> enumerationValue = (SubclassEnumeration<?>) this.enumerationValues[index];
                if (enumerationValue.getTag().contentEquals(tag)) {
                    break;
                }
            }
            return index;
        }

        /**
         * @param reader
         * @param tokens
         * @return
         * @throws IOException
         */
        private JsonToken checkNextToken(JsonReader reader, JsonToken... tokens) throws IOException {
            JsonToken nextToken = reader.peek();
            if (tokens != null && !Arrays.asList(tokens).contains(nextToken)) {
                StringBuilder builder = new StringBuilder(" expected: ");
                for (int i = 0; i < tokens.length; i++) {
                    JsonToken token = tokens[i];
                    if (i > 0) {
                        builder.append(", ");
                    }
                    builder.append(token.name());
                }
                builder.append("; got ").append(nextToken.name());
                throw new RuntimeException(builder.toString());
            }

            if (nextToken == JsonToken.NULL) {
                reader.nextNull();
            } else if (nextToken == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            } else if (nextToken == JsonToken.END_OBJECT) {
                reader.endObject();
            }

            return nextToken;

        }

    }

}
