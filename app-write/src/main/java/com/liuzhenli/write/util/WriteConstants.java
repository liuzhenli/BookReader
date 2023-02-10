package com.liuzhenli.write.util;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.utils.Constant;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/30
 * Email: 848808263@qq.com
 */
public class WriteConstants extends Constant {
    public interface HistoryType {
        int HISTORY = 0;
        int DRAFT = 0;
        int CONFLICT = 0;
    }
    public static String PATH_WRITE_BOOK = BaseApplication.Companion.getInstance().getFilesDir().getPath() + "/write/";
}
