package com.liuzhenli.common.gson;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by linjizong on 15/7/20.
 */
public class GsonUtils {

    public static Gson gson;


    /**
     * 自定义TypeAdapter ,null对象将被解析成空字符串
     */
    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        public String read(JsonReader reader) {
            try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return "";//原先是返回Null，这里改为返回空字符串
                }
                return reader.nextString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public void write(JsonWriter writer, String value) {
            try {
                if (value == null) {
                    writer.nullValue();
                    return;
                }
                writer.value(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 自定义adapter，解决由于数据类型为Int,实际传过来的值为Float，导致解析出错的问题
     * 目前的解决方案为将所有Int类型当成Double解析，再强制转换为Int
     */
    public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() {
        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0;
            }
            try {
                double i = in.nextDouble();
                return (int) i;
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    };

    static {
        GsonBuilder gsonBulder = new GsonBuilder();
        gsonBulder.registerTypeAdapter(String.class, STRING);   //所有String类型null替换为字符串“”
        gsonBulder.registerTypeAdapter(int.class, INTEGER); //int类型对float做兼容

        //通过反射获取instanceCreators属性
        try {
            Class builder = (Class) gsonBulder.getClass();
            Field f = builder.getDeclaredField("instanceCreators");
            f.setAccessible(true);
            Map<Type, InstanceCreator<?>> val = (Map<Type, InstanceCreator<?>>) f.get(gsonBulder);//得到此属性的值
            //注册数组的处理器
            gsonBulder.registerTypeAdapterFactory(new CollectionTypeAdapterFactory(new ConstructorConstructor(val)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        gson = gsonBulder.create();
    }

    /**
     * Json字符串 转为指定对象
     *
     * @param json json字符串
     * @param type 对象类型
     * @param <T>  对象类型
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T toBean(String json, Class<T> type) throws JsonSyntaxException {
        T obj = gson.fromJson(json, type);
        return obj;
    }

    /**
     * 将Json数据解析成相应的映射对象
     */
    public static <T> T parseJObject(String jsonData, Class<T> type) {
        T result = null;
        if (!TextUtils.isEmpty(jsonData)) {
            Gson gson = new GsonBuilder().create();
            try {
                result = gson.fromJson(jsonData, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将Json数组解析成相应的映射对象List
     */
    public static <T> List<T> parseJArray(String jsonData, Class<T> type) {
        List<T> result = null;
        if (!TextUtils.isEmpty(jsonData)) {
            Gson gson = new GsonBuilder().create();
            try {
                JsonParser parser = new JsonParser();
                JsonArray JArray = parser.parse(jsonData).getAsJsonArray();
                if (JArray != null) {
                    result = new ArrayList<>();
                    for (JsonElement obj : JArray) {
                        try {
                            T cse = gson.fromJson(obj, type);
                            result.add(cse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将对象转换成Json
     */
    public static <T> String toJsonWithSerializeNulls(T entity) {
        entity.getClass();
        Gson gson = new GsonBuilder().serializeNulls().create();
        String result = "";
        try {
            result = gson.toJson(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将list排除值为null的字段转换成Json数组
     */
    public static <T> String toJsonArrayWithSerializeNulls(List<T> list) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String result = "";
        try {
            result = gson.toJson(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将list中将Expose注解的字段转换成Json数组
     */
    public static <T> String toJsonArrayWithExpose(List<T> list) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String result = "";
        try {
            result = gson.toJson(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}