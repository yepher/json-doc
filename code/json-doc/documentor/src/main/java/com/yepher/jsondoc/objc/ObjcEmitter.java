package com.yepher.jsondoc.objc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.yepher.jsondoc.annotations.Description;
import com.yepher.jsondoc.helpers.GsonHelper;

public class ObjcEmitter {

	private ObjcEmitterDriver driver;

	private GsonHelper gson = GsonHelper.getInstance();

	{
		gson.setPrettyPrinting(true);
	}

	/* @formatter:off */
	//private Map<Class<? extends Object>, List<? extends Object>> samples = new HashMap<Class<? extends Object>, List<? extends Object>>();
	/* @formatter:on */

	private Map<Class<? extends Object>, TypeParser<?>> specialTypes = new HashMap<Class<? extends Object>, TypeParser<?>>();
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

	public ObjcEmitter() throws Exception {
		super();
	}

	/**
	 * set the documentor driver
	 *
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(ObjcEmitterDriver driver) {
		this.driver = driver;
	}

//	public void documentPDU(Class<?> pdu) {
//		String pduName = pdu.getSimpleName();
//
//		driver.printToConsole("documenting: " + pduName);
//
//		emitLine("## " + pduName);
//		emitLine();
//
//		{
//			Description descriptionAnnotation = pdu.getAnnotation(Description.class);
//			if (descriptionAnnotation != null) {
//				String description = descriptionAnnotation.value();
//				if (description != null) {
//					emitLine(description);
//					emitLine();
//				}
//			}
//		}
//
//		RequestPDU requestAnnotation = pdu.getAnnotation(RequestPDU.class);
//		if (requestAnnotation != null) {
//
//			Method[] methods = requestAnnotation.method();
//			if (methods != null && methods.length > 0) {
//				if (methods.length == 1) {
//					emitLine("HTTP method: " + methods[0].name());
//				} else {
//					emit("HTTP methods: " + methods[0].name());
//					for (int i = 1; i < methods.length; i += 1) {
//						emit(", " + methods[i].name());
//					}
//					emitLine();
//				}
//				emitLine();
//			}
//
//			String[] paths = requestAnnotation.path();
//			if (paths != null) {
//				int pathsLength = paths.length;
//				if (pathsLength > 0) {
//					if (pathsLength == 1) {
//						emitLine("Path: " + paths[0]);
//					} else {
//						emit("Paths: " + paths[0]);
//						for (int i = 0; i < pathsLength; i += 1) {
//							emit(", " + paths[i]);
//						}
//						emitLine();
//					}
//					emitLine();
//				}
//			}
//
//			{
//				String[] pathParameters = requestAnnotation.pathParameters();
//				if (pathParameters != null) {
//					int pathParametersLength = pathParameters.length;
//					if (pathParametersLength % 2 != 0) {
//						driver.printToConsole("pathParameters length is odd, will reduce by 1, for class: " + pduName);
//						pathParametersLength -= 1;
//					}
//					if (pathParametersLength > 0) {
//						emitLine("Path Parameters:");
//						emitLine();
//						emitLine("Parameter|Description");
//						emitLine(":--------|:----------");
//						for (int i = 0; i < pathParameters.length; i += 2) {
//							emitLine(pathParameters[i] + "|" + pathParameters[i + 1]);
//						}
//						emitLine();
//					}
//				}
//			}
//
//			{
//				String[] requestParameters = requestAnnotation.requestParameters();
//				if (requestParameters != null) {
//					int requestParametersLength = requestParameters.length;
//					if (requestParametersLength % 2 != 0) {
//						driver.printToConsole(
//								"requestParameters length is odd, will reduce by 1, for class: " + pduName);
//						requestParametersLength -= 1;
//					}
//					if (requestParametersLength > 0) {
//						emitLine("Request Parameters:");
//						emitLine();
//						emitLine("Parameter|Description");
//						emitLine(":--------|:----------");
//						for (int i = 0; i < requestParameters.length; i += 2) {
//							emitLine(requestParameters[i] + "|" + requestParameters[i + 1]);
//						}
//						emitLine();
//					}
//				}
//			}
//
//			{
//				String[] postParts = requestAnnotation.multipartPostParts();
//				if (postParts != null) {
//					int postPartsLength = postParts.length;
//					if (postPartsLength % 2 != 0) {
//						driver.printToConsole(
//								"multipartPostParts length is odd, will reduce by 1, for class: " + pduName);
//						postPartsLength -= 1;
//					}
//					if (postPartsLength > 0) {
//						emitLine("Multi-part POST parts:");
//						emitLine();
//						emitLine("Part Name|Description");
//						emitLine(":---|:----------");
//						for (int i = 0; i < postParts.length; i += 2) {
//							emitLine(postParts[i] + "|" + postParts[i + 1]);
//						}
//						emitLine();
//					}
//				}
//			}
//
//			Class<?>[] responses = requestAnnotation.response();
//			if (responses != null) {
//				int responsesLength = responses.length;
//				if (responsesLength > 0) {
//					Class<?> response = responses[0];
//					addToList(response);
//					if (responsesLength == 1) {
//						emitLine("Response: " + response.getSimpleName());
//					} else {
//						emit("Responses: " + response.getSimpleName());
//						for (int i = 1; i < responsesLength; i += 1) {
//							response = responses[i];
//							addToList(response);
//							emit(", " + response.getSimpleName());
//						}
//						emitLine();
//					}
//					emitLine();
//				}
//			}
//		}
//
//		ResponsePDU responseAnnotation = pdu.getAnnotation(ResponsePDU.class);
//		if (responseAnnotation != null) {
//			Class<?>[] requests = responseAnnotation.request();
//			int requestsLength = requests.length;
//			if (requests != null && requestsLength > 0) {
//				Class<?> request = requests[0];
//				addToList(request);
//				if (requestsLength == 1) {
//					emitLine("Request: " + request.getSimpleName());
//				} else {
//					emit("Requests: " + request.getSimpleName());
//					for (int i = 1; i < requestsLength; i += 1) {
//						request = requests[i];
//						addToList(request);
//						emit(", " + request.getSimpleName());
//					}
//					emitLine();
//				}
//				emitLine();
//			}
//
//		}
//
//		List<Field> fields = getAllFields(pdu);
//
//		{
//			boolean headerEmitted = false;
//			if (fields.size() > 0) {
//				for (Field field : fields) {
//					if (fieldIsStatic(field)) {
//						continue;
//					}
//					if (!headerEmitted) {
//						emitLine("Fields:");
//						emitLine();
//						emitLine("Type|Name|Serialized Name|Sample|Description");
//						emitLine(":---|:---|:-------|:-----|:----------");
//						headerEmitted = true;
//					}
//					String fieldName = field.getName();
//					Class<?> fieldType = field.getType();
//					String typeName = fieldType.getSimpleName();
//					if (!isSpecialType(fieldType)) {
//						Class<?> listType = getListType(field);
//						if (listType == null) {
//							addToList(fieldType);
//						} else {
//							typeName = typeName + "<" + listType.getSimpleName() + ">";
//							addToList(listType);
//						}
//					}
//					String serializedName = fieldName;
//					String sample = "";
//					String description = "";
//					SerializedName serializedAnnotation = field.getAnnotation(SerializedName.class);
//					if (serializedAnnotation != null) {
//						serializedName = serializedAnnotation.value();
//					}
//					Description descriptionAnnotation = field.getAnnotation(Description.class);
//					if (descriptionAnnotation != null) {
//						String[] samples = descriptionAnnotation.sample();
//						if (samples != null && samples.length > 0) {
//							sample = samples[0];
//						}
//						description = descriptionAnnotation.value();
//					}
//					emitLine(typeName + "|" + fieldName + "|" + serializedName + "|" + sample + "|" + description);
//				}
//				emitLine();
//			}
//		}
//
//		{
//			boolean headerEmitted = false;
//			if (fields.size() > 0) {
//				for (Field field : fields) {
//					if (!fieldIsStatic(field)) {
//						continue;
//					}
//					if (!headerEmitted) {
//						emitLine("Constants:");
//						emitLine();
//						emitLine("Type|Name|Value|Description");
//						emitLine(":---|:---|:----|:----------");
//						headerEmitted = true;
//					}
//					String fieldName = field.getName();
//					Class<?> fieldType = field.getType();
//					String typeName = fieldType.getSimpleName();
//					if (!isSpecialType(fieldType)) {
//						Class<?> listType = getListType(field);
//						if (listType == null) {
//							addToList(fieldType);
//						} else {
//							typeName = typeName + "<" + listType.getSimpleName() + ">";
//							addToList(listType);
//						}
//					}
//					String description = "";
//					Description descriptionAnnotation = field.getAnnotation(Description.class);
//					if (descriptionAnnotation != null) {
//						description = descriptionAnnotation.value();
//					}
//					field.setAccessible(true);
//					Object value;
//					try {
//						value = field.get(null);
//					} catch (IllegalArgumentException | IllegalAccessException e) {
//						driver.printToConsole(
//								"exception accessing value of static field " + fieldName + ": " + e.getMessage());
//						continue;
//					}
//					if (value == null) {
//						value = "[null]";
//					}
//					emitLine(typeName + "|" + fieldName + "|" + value.toString() + "|" + description);
//				}
//				emitLine();
//			}
//		}
//
//		if ((requestAnnotation != null || responseAnnotation != null)
//				&& (requestAnnotation == null || !requestAnnotation.noSample())) {
//			Object sample = getSample(pdu);
//			String json = gson.toJson(sample);
//			emitLine("Sample:");
//			emitLine();
//			emitLine("```");
//			emitLine(json);
//			emitLine("```");
//		}
//
//	}

	public void addToList(Class<?> clazz) {
		if (isSpecialType(clazz) || clazz.isEnum() || (clazz.getModifiers() & Modifier.ABSTRACT) != 0) {
			return;
		}
		driver.addPduToDocument(clazz);
	}

//	private <T> T getSample(Class<T> clazz) {
//		return getSample(clazz, 0);
//	}

//	private <T extends Object> T getSample(Class<T> clazz, int index) {
//		T sample = null;
//		@SuppressWarnings("unchecked")
//		List<T> sampleList = (List<T>) samples.get(clazz);
//		if (sampleList == null) {
//			sampleList = new ArrayList<T>();
//			samples.put(clazz, sampleList);
//		}
//
//		while (index >= sampleList.size()) {
//			sampleList.add(makeSample(clazz, index));
//		}
//		sample = sampleList.get(index);
//
//		return sample;
//	}

//	private <T> T makeSample(Class<T> clazz, int index) {
//		T sample = null;
//		try {
//			sample = clazz.newInstance();
//		} catch (InstantiationException | IllegalAccessException e) {
//			driver.printToConsole("exception creating sample: " + e.getMessage());
//			return sample;
//		}
//
//		List<Field> fields = getAllFields(clazz);
//		for (Field field : fields) {
//			if (fieldIsStatic(field)) {
//				continue;
//			}
//			Object value = null;
//			Description descriptionAnnotation = field.getAnnotation(Description.class);
//			Class<?> listType = getListType(field);
//			if (listType != null) {
//				Integer numberOfItems = 0;
//				if (descriptionAnnotation != null) {
//					numberOfItems = descriptionAnnotation.numberOfSamplesInList();
//				}
//				ArrayList<Object> arrayList = new ArrayList<Object>(numberOfItems);
//				for (int i = 0; i < numberOfItems; i += 1) {
//					Object listItem = getFieldSample(listType, descriptionAnnotation, i);
//					arrayList.add(listItem);
//				}
//				value = arrayList;
//			} else {
//				Class<?> fieldType = field.getType();
//				value = getFieldSample(fieldType, descriptionAnnotation, index);
//			}
//			try {
//				field.setAccessible(true);
//				field.set(sample, value);
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				driver.printToConsole("exception assigning value to sample: " + e.getMessage());
//			}
//		}
//
//		return sample;
//	}

//	private boolean fieldIsStatic(Field field) {
//		int modifiers = field.getModifiers();
//		boolean isStatic = (modifiers & Modifier.STATIC) != 0;
//		return isStatic;
//	}

//	private Object getFieldSample(Class<?> fieldType, Description descriptionAnnotation, int index) {
//		Object value = null;
//		if (isSpecialType(fieldType)) {
//			if (descriptionAnnotation != null) {
//				String sampleString = getSampleString(descriptionAnnotation, index);
//				if (sampleString != null) {
//					TypeParser<?> typeParser = specialTypes.get(fieldType);
//					try {
//						value = typeParser.parse(sampleString);
//					} catch (NumberFormatException e) {
//						System.err.println("NumberFormatException parsing sample: " + sampleString + " for "
//								+ fieldType.getCanonicalName() + " : " + descriptionAnnotation);
//						throw e;
//					}
//				}
//			}
//		} else if (fieldType.isEnum()) {
//			String enumName = getSampleString(descriptionAnnotation, index);
//			if (enumName != null) {
//				Enum<?>[] enumConstants = (Enum[]) fieldType.getEnumConstants();
//				for (int i = 0; i < enumConstants.length; i++) {
//					Enum<?> enumConstant = enumConstants[i];
//					if (enumConstant.name().equals(enumName)) {
//						value = enumConstant;
//						break;
//					}
//				}
//			}
//		} else {
//			Class<?> sampleClass = fieldType;
//			if (descriptionAnnotation != null) {
//				Class<?>[] sampleClasses = descriptionAnnotation.sampleClasses();
//				int sampleClassesLength = sampleClasses.length;
//				if (sampleClasses != null && sampleClassesLength > 0) {
//					int sampleIndex = index;
//					if (sampleIndex >= sampleClassesLength) {
//						sampleIndex = sampleClassesLength - 1;
//					}
//					sampleClass = sampleClasses[sampleIndex];
//					addToList(sampleClass);
//				}
//			}
//			value = getSample(sampleClass, index);
//		}
//		return value;
//	}

//	private String getSampleString(Description descriptionAnnotation, int sampleIndex) {
//		String sampleString = null;
//		String[] samples = descriptionAnnotation.sample();
//		if (samples != null) {
//			if (sampleIndex >= samples.length) {
//				sampleIndex = samples.length - 1;
//			}
//			if (sampleIndex >= 0) {
//				sampleString = samples[sampleIndex];
//			}
//		}
//		return sampleString;
//	}

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
	 * get the item type, T, of a field whose class implements List<T>, or an
	 * extension thereof.
	 *
	 * @param field
	 *            the field whose class (potentially) implements List<T> or an
	 *            extension thereof.
	 * @return T, or null if the class of the field does not implement List
	 *         <T> or an extension thereof
	 */
//	private Class<?> getListType(Field field) {
//		Type typeOfField = field.getGenericType();
//		Class<?> listType = getListType(typeOfField);
//		return listType;
//	}

//	private Class<?> getListType(Type type) {
//		Class<?> listType = null;
//
//		/*
//		 * if this isn't a parameterized type, it can't be List<T>
//		 */
//		if (!(type instanceof ParameterizedType)) {
//			return null;
//		}
//		ParameterizedType parameterizedType = (ParameterizedType) type;
//		Type rawType = parameterizedType.getRawType();
//		if (rawType == null) {
//			return null;
//		}
//		/*
//		 * is this List<T>?
//		 */
//		if (rawType.equals(List.class)) {
//			/*
//			 * yes, get and return T
//			 */
//			Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//			if (actualTypeArguments == null || actualTypeArguments.length != 1) {
//				return null;
//			}
//			Type actualTypeArgument = actualTypeArguments[0];
//			if (actualTypeArgument == null || !(actualTypeArgument instanceof Class<?>)) {
//				return null;
//			}
//			listType = (Class<?>) actualTypeArgument;
//			return listType;
//		}
//
//		/*
//		 * no, maybe a superclass is or implements List<T>; go looking for it
//		 * first the type has to be a class
//		 */
//		if (!(type instanceof Class)) {
//			return null;
//		}
//		Class<?> clazz = (Class<?>) type;
//		Type genericSuperclazz = clazz.getGenericSuperclass();
//		while (genericSuperclazz != null) {
//			listType = getListType(genericSuperclazz);
//			/*
//			 * did we find T?
//			 */
//			if (listType != null) {
//				/*
//				 * Yes, return it
//				 */
//				return listType;
//			}
//			/*
//			 * try another superclass
//			 */
//			if (!(genericSuperclazz instanceof Class)) {
//				break;
//			}
//			clazz = (Class<?>) genericSuperclazz;
//			genericSuperclazz = clazz.getGenericSuperclass();
//		}
//
//		Type[] genericInterfaces = clazz.getGenericInterfaces();
//		for (int i = 0; i < genericInterfaces.length; i++) {
//			Type interfaze = genericInterfaces[i];
//			listType = getListType(interfaze);
//			/*
//			 * did we find T?
//			 */
//			if (listType != null) {
//				/*
//				 * Yes, return it
//				 */
//				return listType;
//			}
//		}
//
//		/*
//		 * all out of possibilities for this type, return null
//		 */
//		return null;
//	}

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

	public void writeHeaderFile(Class<?> cls) {
		if (cls.getSuperclass().getClass().isEnum()) {
			return;
		}

		StringBuilder output = new StringBuilder();
		output.append(driver.getBanner());

		String prefix = driver.getClassPrefix();
		String className = prefix + cls.getSimpleName();
		driver.printToConsole("documenting: " + driver.getClassPrefix() + className);

		emitLine(driver.getBanner());

		String mySuperClass = "";
		Class<?> superclass = cls.getSuperclass();
		if (!superclass.getSimpleName().equals("Object")) {
			mySuperClass = prefix + superclass.getSimpleName();
		}

		emitLine("#import <Foundation/Foundation.h>\n" + "#import \"" + prefix + "SendableMessage.h\"\n"
				+ "#import \"SPReceivableMessage.h\"");

		if (!mySuperClass.equals("")) {
			emitLine("#import \"" + mySuperClass + ".h\"\n");
		}

		/**
		 * Emit Other Imports
		 **/
		List<Field> fields = getAllFields(cls);
		for (Field field : fields) {
			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}
			if (!field.getType().isPrimitive()) {
				if (field.getType().isEnum()) {
					continue;
				} else if (driver.isInPackage(field.getType().getName())) {
					emitLine("#import \"" + prefix + field.getType().getSimpleName() + ".h\"");
				}
			}
		}
		
		/**
		 * Emit class level comment if there is one
		 */
		{
			Description descriptionAnnotation = cls.getAnnotation(Description.class);
			if (descriptionAnnotation != null) {
				String description = descriptionAnnotation.value();
				if (description != null) {
					emitLine("\n/*\n" + description + "\n*/");
					emitLine();
				}
			}
		}
		
		/**
		 * Emit Class Wrapper
		 */
		emit("\n" + "@interface " + className);
		if (mySuperClass != null && mySuperClass.length() > 0) {
			emit(" : " + mySuperClass);
		} else {
			emit(" : NSObject ");
		}

		emitLine("  <SPSendableMessage, SPReceivableMessage>\n");

		for (Field field : fields) {
			System.out.println("Field: " + field + " mods: " + field.getModifiers());
			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}  else if (!field.getDeclaringClass().equals(cls)) {
				// Ignore fields not directly in this class
				continue;
			}

			String serializedName = null;
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof SerializedName) {
					serializedName = ((SerializedName) annotation).value();
				}
			}

			/**
			 * Member Comment
			 */
			String description = "";
			Description descriptionAnnotation = field.getAnnotation(Description.class);
			if (descriptionAnnotation != null) {
				description = descriptionAnnotation.value();
				emitLine("/*");
				emitLine(" " + description);
				emitLine("*/");
			}

			// ObjC reserves the variable/method description so it must be renamed if it exists
			String fieldName = field.getName();
			if (fieldName.equals("description")) {
				fieldName = "desc";
				if (serializedName == null || serializedName.length() == 0) {
					serializedName = "description";
				}
			} else if (fieldName.equals("id")) {
				fieldName = cls.getSimpleName().toLowerCase() + "Id";
				if (serializedName == null || serializedName.length() == 0) {
					serializedName = "id";
				}
			}

			if (serializedName != null) {
				emitLine("// @SerializedName(\"" + serializedName + "\")");
			}
			
			if (field.getType().isPrimitive()) {
				if (field.getType() == int.class) {
					emitLine("@property NSInteger " + fieldName + ";" + "\n");
				} else if (field.getType() == long.class) {
					// This is 64 bits on a 64 bit platform so it should be fine
					emitLine("@property NSInteger " + fieldName + ";" + "\n");
				} else if (field.getType() == boolean.class) {
					emitLine("@property BOOL " + fieldName + ";" + "\n");
				} else {
					throw new IllegalStateException("Unhandled primitive type: " + field.getType() + " for class " + cls.getName());
				}
			} else {
				if (field.getType().isEnum()) {
					emitLine("// TODO @property Enum* " + fieldName + ";" + "\n");
				} else if (field.getType().equals(java.lang.String.class)) {
					emitLine("@property NSString* " + fieldName + ";" + "\n");
				} else if (field.getType() == Boolean.class) {
					emitLine("@property Boolean " + fieldName + ";" + "\n");
				} else if (field.getType() == Integer.class) {
					emitLine("@property NSNumber* " + fieldName + ";" + "\n");
				} else if (field.getType() == Long.class) {
					emitLine("@property NSNumber* " + fieldName + ";" + "\n");
				} else if (field.getType() == java.util.List.class) {
					emitLine("@property NSArray* " + fieldName + ";" + "\n");
				} else if (driver.isInPackage(field.getType().getName())) {
					emitLine("@property " + prefix + field.getType().getSimpleName() + "* " + fieldName + ";" + "\n");
				} else if (field.getType() == Object.class) {
					emitLine("// TODO: @property Object* " + fieldName + ";" + "\n");
				} else if (field.getType() == Map.class) {
					emitLine("@property NSDictionary* " + fieldName + ";" + "\n");
				} else {
					throw new IllegalStateException("Unknown object Type: " + field.getType() + " in class " + cls.getName());
				}
			}
		}

		emitLine("@end");
	}

	public void writeClassFile(Class<?> cls) {
		if (cls.getSuperclass().getClass().isEnum()) {
			return;
		}
		
		try {
			String prefix = driver.getClassPrefix();
			String className = prefix + cls.getSimpleName();
			String mySuperClass = "";
			Class<?> superclass = cls.getSuperclass();
			if (!superclass.getSimpleName().equals("Object")) {
				mySuperClass = prefix + superclass.getSimpleName();
			}

			emitLine(driver.getBanner() + 
					"#import \"" + className + ".h\"\n" + 
					"\n" + 
					"@implementation " + className + "\n" + 
					"\n" + 
					"\n" + 
					"- (id)init\n" + 
					"{\n" + 
					"    self = [super init];\n" + 
					"    if (self) {\n" + 
					"        \n" + 
					"    }\n" + 
					"    return self;\n" + 
					"}\n\n");
			
			
			// Init with Dictionary
			emit("- (id) initWithDictionary :(NSDictionary*) dictionary {\n" + 
					"    self = [self init];\n" + 
					"    \n");
			
			//		"    self.username = [dictionary objectForKey:@\"un\"];\n" +
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				String serializedName = null;
				// System.out.print(field);
				Annotation[] annotations = field.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation instanceof SerializedName) {
						serializedName = ((SerializedName) annotation).value();
					}
				}
 
				// System.out.println(" = " + serializedName);
				String fieldName = field.getName();
				String key = serializedName != null ? serializedName : fieldName;
				
				if (fieldName.equals("description")) {
					fieldName = "desc";
					if (serializedName == null || serializedName.length() == 0) {
						serializedName = "description";
					}
				} else if (fieldName.equals("id")) {
					fieldName = cls.getSimpleName().toLowerCase() + "Id";
					if (serializedName == null || serializedName.length() == 0) {
						serializedName = "id";
					}
				}
				
//				String description = "";
//				Description descriptionAnnotation = field.getAnnotation(Description.class);
//				if (descriptionAnnotation != null) {
//					description = descriptionAnnotation.value();
//					emitLine("    /*");
//					emitLine("     " + description);
//					emitLine("    */");
//				}
				
				if (field.getType().isPrimitive()) {
					if (field.getType() == int.class) {
						emitLine("    self." + fieldName + " = [[dictionary objectForKey:@\"" + key + "\"] integerValue];\n");
					} else if (field.getType() == long.class) {
						emitLine("    self." + fieldName + " = [[dictionary objectForKey:@\"" + key + "\"] integerValue];\n");
					} else if (field.getType() == boolean.class) {
						emitLine("    self." + fieldName + " = [[dictionary objectForKey:@\"" + key + "\"] boolValue];\n");
					} else {
						throw new IllegalStateException("Unhandled primitive type: " + field.getType());
					}
				} else {
					if (field.getType() == String.class) {
						emitLine("    self." + fieldName + " = [dictionary objectForKey:@\"" + key + "\"];\n");
					} else if (field.getType() == Boolean.class) {
						emitLine("    self." + fieldName + " = [[dictionary objectForKey:@\"" + key + "\"] boolValue];\n");
					} else if (field.getType() == Integer.class) {
						emitLine("    self." + fieldName + " = [NSNumber numberWithFloat:[[dictionary objectForKey:@\"" + key + "\"] integerValue]];\n");
//						emitLine("    self." + fieldName + " = [[dictionary objectForKey:@\"" + key + "\"] integerValue];\n");
					} else if (field.getType() == Long.class) {
						emitLine("    self." + fieldName + " = [[dictionary objectForKey:@\"" + key + "\"] LongValue];\n");
					} else if (field.getType() == Map.class) {
						emitLine("    self." + fieldName + " = [dictionary objectForKey:@\"" + key + "\"];\n");
					} else if (field.getType() == List.class) {
						/** 
					    self = [self init];

					    NSMutableArray* mfpContacts = [NSMutableArray array];
					    
					    NSArray* rawContactData = [dictionary objectForKey:@"contacts"];
					    for (NSDictionary* dict in rawContactData) {
					        MFPContact* contact = [[MFPContact alloc] initWithDictionary:dict];
					        [mfpContacts addObject:contact];
					    }
					    
					    self.contacts = mfpContacts;
					    return [super initWithDictionary:dictionary];
						 */
						emitLine("    self." + fieldName + " = [dictionary objectForKey:@\"" + key + "\"];\n");
					} else if (driver.isInPackage(field.getType().getName())) {
;						String fieldClassName = prefix + field.getType().getSimpleName();
						emitLine("    self." + fieldName + " = [[" + fieldClassName +  " alloc] initWithDictionary:[dictionary objectForKey:@\"" + key + "\"]];\n");
					} else if (field.getType() == Object.class) {
						emitLine("    // TODO: 'Object' self." + fieldName + " = [dictionary objectForKey:@\"" + key + "\"];\n");
					}  else {
						throw new IllegalStateException("Unknown Type: " + field.getType().getName() + " in class " + cls.getName());
					}
				}
			}
			
			if (mySuperClass.length() > 0) {
				emitLine("    return [super initWithDictionary:dictionary];\n");
			} else {
			
				emitLine("    return self;\n");
			}
			
			emitLine(
					"}\n" + 
					"\n" + 
					"");
			
			emitLine(
					"- (NSData*) asJson {\n" + 
					"    NSDictionary* data = [self asDictionary];\n" + 
					"\n" + 
					"    NSError *error;\n" + 
					"    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data\n" + 
					"                                                       options:NSJSONWritingPrettyPrinted\n" + 
					"                                                         error:&error];\n" + 
					"    \n" + 
					"    if (error || !jsonData) {\n" + 
					"        NSLog(@\"Got an error: %@\", error);\n" + 
					"        return nil;\n" + 
					"    }\n" + 
					"    \n" + 
					"#ifdef DEBUG\n" + 
					"        NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];\n" + 
					"        NSLog(@\"%@\",jsonString);\n" + 
					"#endif\n" + 
					"    \n" + 
					"    return jsonData;\n" + 
					"    \n" + 
					"}");

			
			emitLine("- (NSDictionary*) asDictionary {\n" + 
					"    NSDictionary* data = @{\n");
			
			fields = cls.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				String serializedName = null;
				Annotation[] annotations = field.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation instanceof SerializedName) {
						serializedName = ((SerializedName) annotation).value();
					}
				}
				

				String fieldName = field.getName();
				String key = serializedName != null ? serializedName : fieldName;
				if (fieldName.equals("description")) {
					fieldName = "desc";
					if (serializedName == null || serializedName.length() == 0) {
						serializedName = "description";
					}
				} else if (fieldName.equals("id")) {
					fieldName = cls.getSimpleName().toLowerCase() + "Id";
					if (serializedName == null || serializedName.length() == 0) {
						serializedName = "id";
					}
				}
 
				if (field.getType().isPrimitive()) {
					if (field.getType() == int.class) {
						emitLine("       @\"" + key + "\":[NSNumber numberWithInteger:self." + fieldName + "],");
					} else if (field.getType() == boolean.class) {
						emitLine("       @\"" + key + "\":[NSNumber numberWithInteger:(self." +fieldName + "? 1 : 0)],");
					} else if (field.getType() == long.class) {
						emitLine("       @\"" + key + "\":[NSNumber numberWithInteger:self." + fieldName + "],");
					} else {
						throw new IllegalStateException("Unhandled primitive type: " + fieldName + " for class " + cls.getName());
					}
				} else {
					if (field.getType().isEnum()) {
						emitLine("       // handle enum @\"" + key + "\":[self " + fieldName + "],");
						continue;
					} else if (field.getType() == String.class) {
						emitLine("       @\"" + key + "\":[self " + fieldName + "],");
					} else if (field.getType() == Integer.class) {
						emitLine("       @\"" + key + "\":[self " + fieldName + "],");
					} else if (field.getType() == Long.class) {
						emitLine("       @\"" + key + "\":[self " + fieldName + "],");
					} else if (field.getType() == Boolean.class) {
						emitLine("       @\"" + key + "\":[NSNumber numberWithInteger:(self." + fieldName + ")],");
					} else if (field.getType() == List.class) {
						emitLine("       @\"" + key + "\":[self " + fieldName + "],");
					} else if (field.getType() == Map.class) {
						emitLine("       @\"" + key + "\":[self " + fieldName + "],");
					} else if (field.getType().getName().contains("com.sparkpost")) {
						emitLine("       @\"" + key + "\":[[self " + fieldName + "] asDictionary],");
					} else {
						System.err.println("ERROR: Serializer unhandled field: " + field);
					}
				}
			}
			
			emitLine("    };\n");
			
			
			if (mySuperClass.length() > 0) {
				emitLine("    NSMutableDictionary* request = [NSMutableDictionary dictionaryWithDictionary:[super asDictionary]];\n");
				emitLine("    [request addEntriesFromDictionary:data];\n\n" + 
						"    return request;\n"); 
			} else {
				emitLine("    return data;\n");
			}
			
			emitLine("}");

			emitLine("@end");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			
		}
		
	}

}
