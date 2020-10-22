package com.liuzhenli.reader.utils;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/17
 * Email: 848808263@qq.com
 */
public class ApiManager {
    @Retention(RetentionPolicy.SOURCE)
    public @interface apiName {
        /***书源**/
        String BOOK_SOURCE = "book_source";
    }

    private static ApiManager manager = new ApiManager();
    //private String bookSource = "http://yck.mumuceo.com/yuedu/shuyuan/jsons?id=2824-2823-2822-2821-2820-2819-2818-2817-2816-2815-2784-2545-2355-2814-2813-2812-2811-2810-2809-2808-2807-2806-2805-2804-2801-2803-2802-2793-2800-2799-2798-2538-2797-2296-2796-2795-2794-2792-2791-2790-2789-2788-2787-2786-2785-2783-2782-2781-2780-2779";
    private String bookSource = "https://oss.soft404.cn/miiread/source/200928.json";

    private ApiManager() {

    }

    public static ApiManager getInstance() {
        return manager;
    }

    public String getBookSource() {
        return bookSource;
    }

    /**
     * 根据接口名字获取接口
     *
     * @param name 接口名字
     */
    public String getUrl(String name) {
        String url = "";
        switch (name) {
            case apiName.BOOK_SOURCE:
                return bookSource;

        }
        return url;
    }
}
