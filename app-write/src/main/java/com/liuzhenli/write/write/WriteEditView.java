package com.liuzhenli.write.write;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.liuzhenli.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/20
 * Email: 848808263@qq.com
 */
public class WriteEditView extends AppCompatEditText implements IWriteEditView {

    private List<String> mHistory;

    public WriteEditView(@NonNull Context context) {
        this(context, null);
    }

    public WriteEditView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public WriteEditView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mHistory = new ArrayList<>();
    }

    /***快捷插入*/
    public void insertSymbol(String str) {
        Editable edit = getText();
        if (edit == null) {
            setText(str);
        } else {
            int index = getSelectionStart();// 获取光标所在位置
            if (index < 0 || index >= edit.length()) {
                edit.append(str);
                setSelection(edit.length());
            } else {
                edit.insert(index, str); // 光标所在位置插入文字
            }
        }
    }

    @Override
    public String getContent(String title, long timeStamp, String authorPostscript) {

        StringBuilder sb = new StringBuilder("<html><head>");
        sb.append("<meta content=\"").append(timeStamp).append("\" name=\"chapter:timestamp\" />");
        if (authorPostscript != null) {
            sb.append("<meta content=\"").append(authorPostscript).append("\" name=\"chapter:authorspeak\" />");
        }
        sb.append("<title>").append(title).append("</title>");
        sb.append("</head><body>");

        sb.append(escapeInputContent());

        sb.append("</body></html>");
        return sb.toString().replaceAll("\n", "<br/>");
    }

    @Override
    public int getWordCount() {
        String rawContent = getText().toString();
        // 过滤掉图片img 标签
        String imgTag = "<\\s*img\\s+([^>]*)\\s*>";
        Pattern patternImg = Pattern.compile(imgTag);
        Matcher marcherImg = patternImg.matcher(rawContent);
        rawContent = marcherImg.replaceAll("");
        return StringUtils.getWordLength(rawContent);
    }

    @Override
    public int getImageCount() {
        return 0;
    }

    @Override
    public void stepForward() {

    }

    @Override
    public void stepBack() {

    }

    public String escapeInputContent() {
        String rawContent = getText() == null ? "" : getText().toString();
        int handleBeginPosition = 0;
        StringBuilder escapeContent = new StringBuilder("");
        if (handleBeginPosition < rawContent.length() - 1) {
            escapeContent.append(StringUtils.encodeHTMLEntity(rawContent.substring(handleBeginPosition, rawContent.length())));
        }
        return escapeContent.toString();
    }
}
