package taskbook.v1.platform.utility;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import taskbook.v1.platform.annotations.GuardedBy;

/**
 * 
 * @author vio
 * Utility class to serialize and deserialize objects
 */
public final class JSON {
	
	
	@GuardedBy(guardian = "this")
	private static final Map<Class<?>, List<String>> cache
		= new ConcurrentHashMap<>();
	
	
	public static <T> JsonArray serializeList(List<T> list) {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for(T type : list) {
			Map<String, String> string = serialize_(type);
			JsonObjectBuilder obj = Json.createObjectBuilder();
			for(Entry<String, String> entries : string.entrySet()) {
				obj.add(entries.getKey(), entries.getValue());
			}
			builder.add(obj);
		}
		return builder.build();
	}
	
	public static <T> JsonObject serialize(T type) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		Map<String, String> map_ = serialize_(type);
		for(Entry<String, String> entries : map_.entrySet()) {
			builder.add(entries.getKey(), entries.getValue());
		}
		return builder.build();
	}
	
	private static <T> Map<String, String> serialize_(T b) {
		Class<?> targetClass = b.getClass();
		List<String> fieldNames;
		if(cache.containsKey(targetClass)) {
			fieldNames = cache.get(targetClass);
		} else {
			Field[] fields = targetClass.getDeclaredFields();
			fieldNames = new ArrayList<>();
			for(int i = 0; i < fields.length; ++i) {
				Field f = fields[i];
				f.setAccessible(true);
				if(!f.isAnnotationPresent(JSONSkip.class)) {
					fieldNames.add(f.getName());
				}
			}
			cache.put(targetClass, fieldNames);
		}
		Map<String, String> values = new HashMap<>();
		fieldNames
			.forEach(field -> {
				try {
					Field f = b.getClass().getDeclaredField(field);
					
					if(Modifier.isPrivate(f.getModifiers())) {
						f.setAccessible(true);
					}
					values.put(field, String.valueOf(f.get(b)));
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
			});
		return values;
	}
	public static <T> T deserialize(final InputStream inputStream, Class<T> clasz) {
		try(JsonReader reader = Json.createReader(inputStream)) {
			JsonObject jsonObject = reader.readObject();
			Field[] fields = clasz.getDeclaredFields();
			Class<?>[] classes = new Class<?>[fields.length];
			
			int i = 0;
			for(Field f : fields) {
				f.setAccessible(true);
				classes[i++] = f.getType();
			}
			Constructor<T> constructor;
			try {
				constructor = clasz.getConstructor(classes);
				return constructor.newInstance(getValues(jsonObject, toClass(fields)));
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
	public static <T> T deserialize(final InputStream inputStream, Class<T> clasz, Class<?>[] constructor) {
		try(JsonReader reader = Json.createReader(inputStream)) {
			JsonObject jsonObject = reader.readObject();
			Constructor<T> c;
			try {
				c = clasz.getConstructor(constructor);
				return c.newInstance(getValues(jsonObject, c.getParameterTypes()));
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	private static Object[] getValues(JsonObject object, Class<?>[] fields) throws IllegalArgumentException, IllegalAccessException {
		int index = 0;
		Object[] val = new Object[object.values().size()];
		for(String key : object.keySet()) {
			switch(fields[index].getSimpleName()) {
				case "String":
					val[index] = object.getString(key, "");
					break;
				case "int":
					val[index] = object.getInt(key, 0);
					break;
				case "Integer":
					val[index] = Integer.valueOf(object.getInt(key, 0));
					break;
				case "Long":
					val[index] = Long.valueOf(object.getInt(key, 0));
					break;
				case "boolean":
					val[index] = object.getBoolean(key);
					break;
			}
			++index;
		}
		return val;
	}
	
	private static Class<?>[] toClass(Field[] fields) {
		Class<?>[] c = new Class<?>[fields.length];
		int i = 0;
		for(Field f : fields) {
			c[i++] = f.getType();
		}
		
		return c;
	}
	
}
