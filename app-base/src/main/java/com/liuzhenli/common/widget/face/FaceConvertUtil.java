package com.liuzhenli.common.widget.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import com.liuzhenli.common.R;
import com.liuzhenli.common.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * *****************************************
 *
 * @author 廖乃波
 * @文件名称 : FaceConversionUtil.java
 * @创建时间 : 2013-1-27 下午02:34:09
 * @文件描述 : 表情轉換工具
 * *****************************************
 */
public class FaceConvertUtil {

    /**
     * 每一页表情的个数
     */
    private int faceNumPerPage = 23;

    private static FaceConvertUtil mFaceConversionUtil;

    /**
     * 保存于内存中的表情HashMap
     */
    private HashMap<String, String> faceMap = new HashMap<String, String>();

    /**
     * 保存于内存中的表情集合
     */
    private List<ChatFace> faces = new ArrayList<ChatFace>();

    /**
     * 表情分页的结果集合
     */
    public List<List<ChatFace>> faceLists = new ArrayList<List<ChatFace>>();

    private FaceConvertUtil() {

    }

    public static FaceConvertUtil getInstace() {
        if (mFaceConversionUtil == null) {
            mFaceConversionUtil = new FaceConvertUtil();
        }
        return mFaceConversionUtil;
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public SpannableString getExpressionString(Context context, String str) {
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    /**
     * 添加表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString addFace(Context context, int imgId,
                                   String spannableString, EditText editText) {
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                imgId);
        bitmap = Bitmap.createScaledBitmap(bitmap, editText.getLineHeight(), editText.getLineHeight(), true);
        VerticalImageSpan imageSpan = new VerticalImageSpan(context, bitmap, ImageSpan.ALIGN_BASELINE);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     */
    private void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            String value = faceMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());
            // 通过上面匹配得到的字符串来生成图片资源id
            // Field field=R.drawable.class.getDeclaredField(value);
            // int resId=Integer.parseInt(field.get(null).toString());
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resId);
                //TODO 宽高设置为LineHeight图片会很小很小
                DisplayMetrics dm = new DisplayMetrics();
                dm = context.getApplicationContext().getResources().getDisplayMetrics();
                float dpi = dm.densityDpi;
                //根据DPI设置不同的大小
                int mapScale = 0;
                if (DisplayMetrics.DENSITY_LOW <= dpi && dpi < DisplayMetrics.DENSITY_MEDIUM) {
                    mapScale = 30;//120-160
                } else if (DisplayMetrics.DENSITY_MEDIUM <= dpi && dpi < DisplayMetrics.DENSITY_HIGH) {
                    mapScale = 40;//160-240
                } else if (DisplayMetrics.DENSITY_HIGH <= dpi && dpi < DisplayMetrics.DENSITY_XHIGH) {
                    mapScale = 60;//240-320
                } else if (DisplayMetrics.DENSITY_XHIGH <= dpi && dpi < DisplayMetrics.DENSITY_XXHIGH) {
                    mapScale = 80;//320-480
                } else if (DisplayMetrics.DENSITY_XXHIGH <= dpi && dpi < DisplayMetrics.DENSITY_XXXHIGH) {
                    mapScale = 120;//480-640
                } else if (dpi >= DisplayMetrics.DENSITY_XXXHIGH) {
                    mapScale = 320;//大于640
                } else if (dpi < DisplayMetrics.DENSITY_LOW) {
                    mapScale = 20;//小于120
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, mapScale, mapScale, true);
                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                VerticalImageSpan imageSpan = new VerticalImageSpan(bitmap);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    public void getFileText(Context context) {
        ParseData(FileUtils.getFacesFile(context), context);
    }

    /**
     * 解析字符
     *
     * @param data
     */
    private void ParseData(List<String> data, Context context) {
        if (data == null) {
            return;
        }
        ChatFace faceEentry;
        try {
            for (String str : data) {
                String[] text = str.split(",");
                String fileName = text[0]
                        .substring(0, text[0].lastIndexOf("."));
                faceMap.put(text[1], fileName);
                int resID = context.getResources().getIdentifier(fileName,
                        "drawable", context.getPackageName());

                if (resID != 0) {
                    faceEentry = new ChatFace();
                    faceEentry.setId(resID);
                    faceEentry.setCharacter(text[1]);
                    faceEentry.setFaceName(fileName);
                    faces.add(faceEentry);
                }
            }
            int pageCount = (int) Math.ceil(faces.size() / faceNumPerPage);

            for (int i = 0; i < pageCount; i++) {
                faceLists.add(getData(i));
            }
        } catch (Exception e) {
//			ErrorReporter.log(e);
        }
    }

    /**
     * 获取分页数据
     *
     * @param page
     * @return
     */
    private List<ChatFace> getData(int page) {
        int startIndex = page * faceNumPerPage;
        int endIndex = startIndex + faceNumPerPage;

        if (endIndex > faces.size()) {
            endIndex = faces.size();
        }
        // 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
        List<ChatFace> list = new ArrayList<ChatFace>();
        list.addAll(faces.subList(startIndex, endIndex));
        if (list.size() < faceNumPerPage) {
            for (int i = list.size(); i < faceNumPerPage; i++) {
                ChatFace object = new ChatFace();
                list.add(object);
            }
        }
        if (list.size() == faceNumPerPage) {
            ChatFace object = new ChatFace();
            object.setId(R.drawable.publish_delete_btn);
            list.add(object);
        }
        return list;
    }
}
