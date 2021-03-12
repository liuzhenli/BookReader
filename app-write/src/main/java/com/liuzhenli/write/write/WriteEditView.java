package com.liuzhenli.write.write;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/20
 * Email: 848808263@qq.com
 */
public class WriteEditView extends AppCompatEditText {

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

    public void backStep() {

    }

    public void forewordStep() {

    }

}
