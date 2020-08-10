package com.liuzhenli.reader.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.liuzhenli.reader.utils.face.FaceConvertUtil;
import com.microedu.reader.R;


/**
 * 识别文字中的书名
 *
 * @author liuzhenli.
 * @date 2019/9/2.
 */
public class BookContentTextView extends AppCompatTextView {

    public BookContentTextView(Context context) {
        this(context, null);
    }

    public BookContentTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text) {
        int startIndex = 0;

        if (length() > 0) {
            if (getEditableText() != null) {
                getEditableText().clear();
            }
        }
        while (true) {


            int start = text.indexOf("《");
            int end = text.indexOf("》");
            if (start < 0 || end < 0) {


                addEmoji(text.substring(startIndex));
                break;
            }

//            append(text.substring(startIndex, start));
            addEmoji(text.substring(startIndex, start));

            SpannableString spanableInfo = new SpannableString(text.substring(start, end + 1));
            spanableInfo.setSpan(new Clickable(spanableInfo.toString()), 0, end + 1 - start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            append(spanableInfo);
            //setMovementMethod()该方法必须调用，否则点击事件不响应
            setMovementMethod(LinkMovementMethod.getInstance());
            text = text.substring(end + 1);


        }
    }

    private void addEmoji(String substring) {
//        String substring = text.substring(startIndex);
        SpannableStringBuilder atAndEmoji = new SpannableStringBuilder(substring);
        atAndEmoji.replace(0, atAndEmoji.length(), FaceConvertUtil.getInstace().getExpressionString(getContext(), substring));
        append(atAndEmoji);
    }

    class Clickable extends ClickableSpan {
        private String name;

        public Clickable(String name) {
            super();
            this.name = name;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            if (getContext()!=null) {
                ds.setColor(ContextCompat.getColor(getContext(), R.color.main));
            }else {
                ds.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            }
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View v) {
            //ToDO  处理点击事件
        }
    }
}
