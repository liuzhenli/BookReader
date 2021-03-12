package com.liuzhenli.common;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;


import com.tencent.mmkv.MMKV;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;

/**
 * @author liuzhenli
 */
public class SharedPreferencesUtil {

    private volatile static SharedPreferencesUtil prefsUtil;
    public Application context;
    public MMKV prefs;
    public SharedPreferences.Editor editor;

    public synchronized static SharedPreferencesUtil getInstance() {
        return prefsUtil;
    }

    public static void init(Application context) {
        prefsUtil = new SharedPreferencesUtil();
        prefsUtil.context = context;
        prefsUtil.prefs = MMKV.mmkvWithID("config");
        // 迁移旧数据
        {
            SharedPreferences old_man = prefsUtil.context.getSharedPreferences("config", Context.MODE_PRIVATE);
            prefsUtil.prefs.importFromSharedPreferences(old_man);
            old_man.edit().clear().apply();
        }
        prefsUtil.editor = prefsUtil.prefs.edit();
    }

    private SharedPreferencesUtil() {
    }


    public boolean getBoolean(String key, boolean defaultVal) {
        return this.prefs.getBoolean(key, defaultVal) || this.prefs.getInt(key, 0) == 1;
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }


    public String getString(String key, String defaultVal) {
        return this.prefs.getString(key, defaultVal);
    }

    public String getString(String key) {
        return this.prefs.getString(key, null);
    }

    public int getInt(String key, int defaultVal) {
        return this.prefs.getInt(key, defaultVal);
    }

    public int getInt(String key) {
        return this.prefs.getInt(key, 0);
    }


    public float getFloat(String key, float defaultVal) {
        return this.prefs.getFloat(key, defaultVal);
    }

    public float getFloat(String key) {
        return this.prefs.getFloat(key, 0f);
    }

    public long getLong(String key, long defaultVal) {
        return this.prefs.getLong(key, defaultVal);
    }

    public long getLong(String key) {
        return this.prefs.getLong(key, 0l);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultVal) {
        return this.prefs.getStringSet(key, defaultVal);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key) {
        return this.prefs.getStringSet(key, null);
    }

    public Map<String, ?> getAll() {
        return this.prefs.getAll();
    }


    public SharedPreferencesUtil putString(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    public SharedPreferencesUtil putInt(String key, int value) {
        editor.putInt(key, value);
        return this;
    }

    public SharedPreferencesUtil putFloat(String key, float value) {
        editor.putFloat(key, value);
        return this;
    }

    public SharedPreferencesUtil putDouble(String key, double value) {
        prefs.encode(key, value);
        return this;
    }

    public double getDouble(String key, double value) {
        return prefs.decodeDouble(key);
    }

    public SharedPreferencesUtil putLong(String key, long value) {
        editor.putLong(key, value);
        return this;
    }

    public SharedPreferencesUtil putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return this;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SharedPreferencesUtil putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        return this;
    }

    public void putObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(key, objectVal);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (prefs.contains(key)) {
            String objectVal = prefs.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public SharedPreferencesUtil remove(String key) {
        editor.remove(key);
        return this;
    }

    public String[] getArray(String key) {
        String regularEx = "#";
        String[] str;
        String values;
        values = prefs.getString(key, "");
        str = values.split(regularEx);
        return str;
    }


    public void putArray(String key, String[] values) {
        String regularEx = "#";
        String str = "";
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            editor.putString(key, str);
        }
    }

    public int[] getIntArray(String key) {
        String regularEx = "#";
        String values;
        values = prefs.getString(key, "-1");
        String[] strings = values.split(regularEx);
        int[] str = new int[strings.length];
        for (int i = 0; i < str.length && str.length > 0; i++) {
            try {
                str[i] = Integer.parseInt(strings[i]);
            } catch (Exception e) {
                e.printStackTrace();
                return str;
            }
        }
        return str;
    }

    public void putArray(String key, int[] values) {
        String regularEx = "#";
        String str = "";
        if (values != null && values.length > 0) {
            for (int value : values) {
                str += value;
                str += regularEx;
            }
            editor.putString(key, str);
        }
    }


    public Object getObjectValue(String key) {
        // 因为其他基础类型value会读成空字符串,所以不是空字符串即为string or string-set类型
        String value = prefs.decodeString(key);
        if (!TextUtils.isEmpty(value)) {
            // 判断 string or string-set
            if (value.charAt(0) == 0x01) {
                return prefs.decodeStringSet(key);
            } else {
                return value;
            }
        }
        // float double类型可通过string-set配合判断
        // 通过数据分析可以看到类型为float或double时string类型为空字符串且string-set类型读出空数组
        // 最后判断float为0或NAN的时候可以直接读成double类型,否则读float类型
        // 该判断方法对于非常小的double类型数据 (0d < value <= 1.0569021313E-314) 不生效
        Set<String> set = prefs.decodeStringSet(key);
        if (set != null && set.size() == 0) {
            Float valueFloat = prefs.decodeFloat(key);
            Double valueDouble = prefs.decodeDouble(key);
            if (Float.compare(valueFloat, 0f) == 0 || Float.compare(valueFloat, Float.NaN) == 0) {
                return valueDouble;
            } else {
                return valueFloat;
            }
        }
        // int long bool 类型的处理放在一起, int类型1和0等价于bool类型true和false
        // 判断long或int类型时, 如果数据长度超出int的最大长度, 则long与int读出的数据不等, 可确定为long类型
        int valueInt = prefs.decodeInt(key);
        long valueLong = prefs.decodeLong(key);
        if (valueInt != valueLong) {
            return valueLong;
        } else {
            return valueInt;
        }
    }

}
