package com.aurine.cloudx.open.common.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * @author : Qiu
 * @date : 2021 08 27 15:37
 */
public class JSONConvertUtils {

	public static JSONObject getJSONObject(String key, String value) {
		JSONObject object = new JSONObject();
		if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
			object.put(key, value);
		}
		return object;
	}

	/**
	 * String转JSONObject
	 *
	 * @param string
	 * @return
	 */
	public static JSONObject stringToJSONObject(String string) {
		if (StringUtils.isEmpty(string)) return new JSONObject();
		return JSONObject.parseObject(string);
	}

	/**
	 * JSONObject转对象
	 *
	 * @param jsonObject
	 * @param clazz
	 * @return
	 */
	public static <T> T jsonObjectToObject(JSONObject jsonObject, Class<T> clazz) {
		if (jsonObject == null || clazz == null) return null;
		try {
			return jsonObject.toJavaObject(clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对象转String
	 *
	 * @param object
	 * @return
	 */
	public static String objectToString(Object object) {
		if (object == null) return null;
		try {
			return JSONObject.toJSONString(object);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 对象转String
	 *
	 * @param string
	 * @param clazz
	 * @return
	 */
	public static <T> T stringToObject(String string, Class<T> clazz) {
		if (StringUtils.isEmpty(string)) return null;
		return JSONObject.parseObject(string, clazz);
	}


	/**
	 * 对象转JSON对象
	 *
	 * @param object
	 * @return
	 */
	public static JSONObject objectToJSONObject(Object object) {
		if (object == null) return null;
		try {
			return stringToJSONObject(objectToString(object));
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 对象转对象
	 *
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T objectToObject(Object object, Class<T> clazz) {
		if (object == null || clazz == null) return null;
		try {
			return stringToObject(objectToString(object), clazz);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Map转对象
	 *
	 * @param map
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) throws Exception {
		if (map == null) return null;
		String jsonString = objectToString(map);
		return JSONObject.parseObject(jsonString, clazz);
	}

	/**
	 * 对象转Map
	 *
	 * @param object
	 * @return
	 */
	public static Map<String, Object> objectToMap(Object object) {
		if (object == null) return new HashMap<>();
		try {
			String jsonString = objectToString(object);
			return JSONObject.parseObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}

	/**
	 * 对象转Map
	 *
	 * @param object
	 * @return
	 */
	public static MultiValueMap<String, String> objectToMapOfString(Object object) {
		if (object == null) return null;
		try {
			MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
			Map<String, Object> objectMap = objectToMap(object);
			for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
				String string = String.valueOf(entry.getValue());
				if ("null" == string) string = null;
				multiValueMap.put(entry.getKey(), Collections.singletonList(string));
			}
			return multiValueMap;
		} catch (Exception e) {
			e.printStackTrace();
			return new LinkedMultiValueMap<>();
		}
	}

	/**
	 * String转List
	 *
	 * @param string
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> stringToList(String string, Class<T> clazz) {
		if (StringUtils.isEmpty(string)) return new ArrayList<T>();
		try {
			return JSONArray.parseArray(string, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * List转JSONArray
	 *
	 * @param string
	 * @return
	 */
	public static JSONArray stringToJSONArray(String string) {
		if (StringUtils.isEmpty(string)) return new JSONArray();
		try {
			return JSONArray.parseArray(string);
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONArray();
		}
	}

	/**
	 * List转JSONArray
	 *
	 * @param list
	 * @return
	 */
	public static <T> JSONArray listToJSONArray(List<T> list) {
		if (list == null) return new JSONArray();
		try {
			return JSONArray.parseArray(objectToString(list));
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONArray();
		}
	}

	/**
	 * List转JSONString
	 *
	 * @param array
	 * @return
	 */
	public static <T> String arrayToJSONString(T[] array) {
		if (array == null) return new JSONArray().toJSONString();
		return listToJSONString(Arrays.asList(array));
	}

	/**
	 * List转JSONString
	 *
	 * @param list
	 * @return
	 */
	public static <T> String listToJSONString(List<T> list) {
		if (list == null) return new JSONArray().toJSONString();
		try {
			return listToJSONArray(list).toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONArray().toJSONString();
		}
	}

	/**
	 * List转JSONArray
	 *
	 * @param jsonArray
	 * @return
	 */
	public static Object[] jsonArrayToArray(JSONArray jsonArray) {
		if (jsonArray == null) return new Object[0];
		try {
			return jsonArray.toArray();
		} catch (Exception e) {
			e.printStackTrace();
			return new Object[0];
		}
	}

	/**
	 * List转JSONArray
	 *
	 * @param jsonArray
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> jsonArrayToList(JSONArray jsonArray, Class<T> clazz) {
		if (jsonArray == null) return new ArrayList<>();
		try {
			return JSONArray.parseArray(jsonArray.toJSONString(), clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * List转JSONArray
	 *
	 * @param jsonArray
	 * @param clazz
	 * @return
	 */
	public static <T> LinkedList<T> jsonArrayToLinkedList(JSONArray jsonArray, Class<T> clazz) {
		if (jsonArray == null) return new LinkedList<>();
		try {
			return new LinkedList<>(JSONArray.parseArray(jsonArray.toJSONString(), clazz));
		} catch (Exception e) {
			e.printStackTrace();
			return new LinkedList<>();
		}
	}

	/**
	 * String转LinkedList
	 *
	 * @param string
	 * @param clazz
	 * @return
	 */
	public static <T> LinkedList<T> stringToLinkedList(String string, Class<T> clazz) {
		if (StringUtils.isEmpty(string)) return new LinkedList<>();
		try {
			return (LinkedList<T>) JSONArray.parseArray(string, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return new LinkedList<>();
		}
	}
}
