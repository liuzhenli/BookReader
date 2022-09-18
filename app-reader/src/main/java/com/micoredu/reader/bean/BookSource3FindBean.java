package com.micoredu.reader.bean;

/**
 * Description:3.0 书源 发现
 *
 * @author liuzhenli 2022/9/17
 * Email: 848808263@qq.com
 */
public class BookSource3FindBean {
    public String title;
    public String url;
    public Style style;

    public static class Style {
        public float layout_flexGrow = 0F;
        public float layout_flexShrink = 1F;
        public String layout_alignSelf = "auto";
        public float layout_flexBasisPercent = -1F;
        public boolean layout_wrapBefore = false;
    }


    public String toBookSource2Find() {
        return String.format("%s::%s\n", title, url);
    }
}
