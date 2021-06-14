package com.liuzhenli.write.write;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/23
 * Email: 848808263@qq.com
 */
public interface IWriteEditView {
    public String getContent(String title, long timeStamp, String authorPostscript);

    int getWordCount();

    int getImageCount();

    void stepForward();

    void stepBack();
}
