package com.liuzhenli.common;

import java.util.HashMap;
import java.util.Map;

public class BitIntentDataManager {
    public static final String DATA_KEY = "dataKey";
    private static Map<String, Object> bigData;

    private static BitIntentDataManager instance = null;

    private BitIntentDataManager() {
        bigData = new HashMap<>();
    }

    public static BitIntentDataManager getInstance() {
        if (instance == null) {
            synchronized (BitIntentDataManager.class) {
                if (instance == null) {
                    instance = new BitIntentDataManager();
                }
            }
        }
        return instance;
    }

    public Object getData(String key) {
        Object object = bigData.get(key);
        bigData.remove(key);
        return object;
    }

    public void putData(String key, Object data) {
        bigData.put(key, data);
    }

}
