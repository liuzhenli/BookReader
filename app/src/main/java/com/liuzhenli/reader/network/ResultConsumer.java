package com.liuzhenli.reader.network;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.function.Consumer;

/**
 * @author Liuzhenli
 * @since 2019-07-07 16:50
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public abstract class ResultConsumer<T> implements Consumer<T> {
    @Override
    public void accept(T o) {

    }
}
