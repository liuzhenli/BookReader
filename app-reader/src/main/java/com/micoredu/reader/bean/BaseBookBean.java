package com.micoredu.reader.bean;

import java.io.Serializable;
import java.util.Map;

public interface BaseBookBean extends Serializable {

    String getTag();

    String getNoteUrl();

    void setNoteUrl(String noteUrl);

    String getVariable();

    void setVariable(String variable);

    void putVariable(String key, String value);

    Map<String, String> getVariableMap();
}
